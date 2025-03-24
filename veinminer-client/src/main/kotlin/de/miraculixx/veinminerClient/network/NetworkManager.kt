package de.miraculixx.veinminerClient.network

import de.miraculixx.veinminer.config.extensions.toVeinminer
import de.miraculixx.veinminer.config.network.JoinInformation
import de.miraculixx.veinminer.config.network.KeyPress
import de.miraculixx.veinminer.config.network.RequestBlockVein
import de.miraculixx.veinminer.config.pattern.Pattern
import de.miraculixx.veinminerClient.VeinminerClient
import de.miraculixx.veinminerClient.constants.*
import de.miraculixx.veinminerClient.render.BlockHighlightingRenderer
import de.miraculixx.veinminerClient.render.HUDRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.toasts.SystemToast
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component

object NetworkManager {
    // Client info
    var isVeinminerActive = false
        private set
    var selectedPattern = Pattern.DEFAULT

    // Server info
    var mustSneak = false
        private set
    var cooldown = 0
        private set
    var translucentBlockHighlight = true
        private set

    private val onConfiguration = PACKET_CONFIGURATION.receiveOnClient { packet, context ->
        VeinminerClient.LOGGER.info("Server configuration: $packet")
        if (packet.outdated) {
            context.client.toastManager.addToast(
                SystemToast(SystemToast.SystemToastId.PERIODIC_NOTIFICATION, Component.literal("Veinminer Outdated"), Component.literal("Please update Veinminer"))
            )
        }

        isVeinminerActive = true
        mustSneak = packet.mustSneak
        cooldown = packet.cooldown
        translucentBlockHighlight = packet.translucentBlockHighlight
    }

    private val onHighlight = PACKET_HIGHLIGHT.receiveOnClient { packet, context ->
        VeinminerClient.LOGGER.info("Received block highlight: $packet")
        if (!packet.allowed) {
            HUDRenderer.updateTarget("forbidden")
            BlockHighlightingRenderer.setShape(emptyList())
            return@receiveOnClient
        }

        HUDRenderer.updateTarget(packet.icon)
        BlockHighlightingRenderer.setShape(packet.blocks)
    }

    fun onDisconnect() {
        isVeinminerActive = false
    }

    fun requestBlockInfo(position: BlockPos, direction: Direction) {
        VeinminerClient.LOGGER.info("Sending veinmine request: ($position, $direction)")
        PACKET_MINE.send(RequestBlockVein(position.toVeinminer(), direction.toVeinminer(), selectedPattern))
    }

    fun sendJoin(version: String) {
        VeinminerClient.LOGGER.info("Sending join: ($version)")
        PACKET_JOIN.send(JoinInformation(version))
    }

    fun sendKeyPress(pressed: Boolean) {
        VeinminerClient.LOGGER.info("Sending key press: $pressed")
        PACKET_KEY_PRESS.send(KeyPress(pressed))
    }
}