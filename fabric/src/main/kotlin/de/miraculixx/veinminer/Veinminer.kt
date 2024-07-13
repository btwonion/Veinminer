package de.miraculixx.veinminer

import de.miraculixx.veinminer.command.VeinminerCommand
import de.miraculixx.veinminer.config.ConfigManager
import de.miraculixx.veinminer.config.permissionVeinmine
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseFireBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.silkmc.silk.core.kotlin.ticks
import net.silkmc.silk.core.task.mcCoroutineTask
import java.util.*
import java.util.logging.Logger


class Veinminer : ModInitializer {
    companion object {
        const val MOD_ID = "veinminer"
        lateinit var INSTANCE: ModContainer
        var active = true
    }

    private lateinit var fabricLoader: FabricLoader

    private val cooldown = mutableSetOf<UUID>()

    override fun onInitialize() {
        fabricLoader = FabricLoader.getInstance()
        INSTANCE = fabricLoader.getModContainer(MOD_ID).get()
        LOGGER.info("Veinminer Version: ${INSTANCE.metadata.version} (fabric)")

        VeinminerCommand

        PlayerBlockBreakEvents.BEFORE.register { world, player, pos, state, _ ->
            fun groupedBlocks(id: String): Set<String> = ConfigManager.groups.filter { it.blocks.contains(id) }.map { it.blocks }.flatten().toSet()

            if (!active) return@register true
            val material = state.block.descriptionId

            val settings = ConfigManager.settings
            if (settings.permissionRestricted && !Permissions.check(player, permissionVeinmine)) return@register true
            val groupBlocks = groupedBlocks(material)
            if (ConfigManager.veinBlocks.contains(material) || groupBlocks.isNotEmpty()) {
                // Check for sneak config
                if (settings.mustSneak && !player.isCrouching) return@register true
                // Check for cooldown
                if (cooldown.contains(player.uuid)) return@register true

                // Check for correct tool
                val mainHandItem = player.mainHandItem
                if (settings.needCorrectTool && (state.requiresCorrectToolForDrops() && !mainHandItem.isCorrectToolForDrops(state))) return@register true

                // Perform veinminer
                breakAdjusted(state, groupBlocks, mainHandItem, settings.delay, settings.maxChain, mutableSetOf(), world, pos, player, settings.searchRadius, pos)

                // Check for cooldown config
                val cooldownTime = settings.cooldown
                if (cooldownTime > 0) {
                    cooldown.add(player.uuid)

                    mcCoroutineTask(delay = cooldownTime.ticks) {
                        cooldown.remove(player.uuid)
                    }
                }
            }
            return@register true
        }
    }

    /**
     * Recursively break blocks around the source block until vein stops
     * @return the number of blocks broken
     */
    private fun breakAdjusted(
        source: BlockState,
        target: Set<String>,
        item: ItemStack,
        delay: Int,
        max: Int,
        processedBlocks: MutableSet<BlockPos>,
        world: Level,
        position: BlockPos,
        player: Player,
        searchRadius: Int,
        initialSource: BlockPos
    ): Int {
        if (!target.contains(source.block.descriptionId) || processedBlocks.contains(position)) return 0
        val size = processedBlocks.size
        if (size >= max) return 0
        if (item.isEmpty) return 0
        if (size != 0) {
            source.destroyBlock(item, world, position, player, initialSource)
            damageItem(item, player)
        }
        processedBlocks.add(position)
        (-searchRadius..searchRadius).forEach { x ->
            (-searchRadius..searchRadius).forEach { y ->
                (-searchRadius..searchRadius).forEach z@{ z ->
                    if (x == 0 && y == 0 && z == 0) return@z
                    val newPos = BlockPos(position.x + x, position.y + y, position.z + z)
                    val block = world.getBlockState(newPos)
                    if (delay == 0) breakAdjusted(block, target, item, delay, max, processedBlocks, world, newPos, player, searchRadius, initialSource)
                    else mcCoroutineTask(delay = delay.ticks) {
                        if (breakAdjusted(block, target, item, delay, max, processedBlocks, world, newPos, player, searchRadius, initialSource) == 0)
                            return@mcCoroutineTask
                    }
                }
            }
        }
        return processedBlocks.size
    }

    private fun BlockState.destroyBlock(
        item: ItemStack,
        world: Level,
        position: BlockPos,
        player: Player,
        initialSource: BlockPos
    ) {
        val block = block
        if (block !== Blocks.AIR && (!requiresCorrectToolForDrops() || item.isCorrectToolForDrops(this))) {
            improvedDropResources(this, world, position, world.getBlockEntity(position), player, item, initialSource)

            if (block is BaseFireBlock) {
                world.levelEvent(1009, position, 0)
            } else {
                world.levelEvent(2001, position, Block.getId(this))
            }
        }

        val destroyed = world.removeBlock(position, false)
        if (destroyed) {
            block.destroy(world, position, this)
        }
    }

    /**
     * Used to extract the drop logic from [Block.dropResources] to allow for custom handling of drops
     */
    private fun improvedDropResources(
        blockState: BlockState,
        world: Level,
        blockPos: BlockPos,
        blockEntity: BlockEntity?,
        breaker: Entity,
        tool: ItemStack,
        initialSource: BlockPos
    ) {
        val serverLevel = world as? ServerLevel ?: return
        Block.getDrops(blockState, serverLevel, blockPos, blockEntity, breaker, tool).forEach { drop: ItemStack ->
            val dropPos = if (ConfigManager.settings.mergeItemDrops) initialSource else blockPos
            Block.popResource(world, dropPos, drop)
        }
        blockState.spawnAfterBreak(serverLevel, blockPos, tool, true)
    }

    private fun damageItem(item: ItemStack, player: Player) {
        item.hurtAndBreak(1, player, EquipmentSlot.MAINHAND)
    }
}

val LOGGER: Logger = Logger.getLogger(Veinminer.MOD_ID)
