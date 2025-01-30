[![dono-badge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/donate/kofi-singular_vector.svg)](https://ko-fi.com/miraculixx/donate)
[![enchant-badge](https://i.imgur.com/dNlLO2m.png)](https://modrinth.com/datapack/veinminer-enchantment)
[![silk-badge](https://i.imgur.com/iSCVMEz.png)](https://modrinth.com/mod/silk)
[![fapi-badge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/fabric-api_vector.svg)](https://modrinth.com/mod/fabric-api)

<!-- modrinth_exclude.start -->
### - [DOWNLOAD](https://modrinth.com/project/veinminer) -
<!-- modrinth_exclude.end -->

## ⛏️ Veinminer
Mine a single ore to break the full vein of the same ore! 
Veinminer is a common feature in various modpacks or survival pvp game modes like UHC to speed up breaking whole veins.
Now you can use this feature as a datapack too!

> ### Use as Enchantment? <img src="https://i.imgur.com/JReqOrU.gif" width=25>
>  Add the [**Veinminer Enchantment Addon**](https://modrinth.com/datapack/veinminer-enchantment) to limit veinmining to only enchanted tools!

**Version 2.x** -> Full customizable mod & plugin<br>
**Version 1.x** -> Light DataPack with less settings<br>

## Customization & Settings
![](https://cdn-raw.modrinth.com/data/OhduvhIc/images/f4c0ad7fa3b8b579753c1f757e80151798717c68.gif)

**Veinminer comes in two different versions**

The DataPack (V1) version is a simple version that is usable on every server and world.

|                 Command                  | Permission  |                    Description                     |
|:----------------------------------------:|:-----------:|:--------------------------------------------------:|
| /function veinminer:settings/**pickaxe** | `OP/Cheats` |   Limit or grant the effect for certain pickaxes   |
| /function veinminer:settings/**general** | `OP/Cheats` | Change some general settings to balance the effect |

---
The Fabric & Paper (V2) version is a more advanced version that is only usable with Fabric/Quilt or Paper/PurPur servers.

|         Command         |      Permission      |                 Short Description                 |
|:-----------------------:|:--------------------:|:-------------------------------------------------:|
|  /veinminer **blocks**  |  `veinminer.blocks`  |         Edit blocks that are veinmineable         |
|  /veinminer **toggle**  |  `veinminer.toggle`  |            Completely toggle Veinminer            |
| /veinminer **settings** | `veinminer.settings` | Change settings like cooldown, max chain and more |
|    *Using Veinminer*    |   `veinminer.use`    | If perm-restriction is active, this is needed to veinmine |

<details><summary>General Settings</summary>

To change a setting, enter `/veinminer settings ... <new-value>`. To check the current state, leave out the new value argument.

| Setting | Description | Default |
|:-------:|:-----------:|:-------:|
| `mustSneak` | Players must sneak to veinmine | `false` |
| `cooldown` | Time between players are able to veinmine (in ticks) | `20` |
| `delay` | Time between each automated block breaking (in ticks) | `1` |
| `maxChain` | Max amount of blocks that can break from one source block | `100` |
| `needCorrectTool` | If blocks have a required tool, this must be used | `true` |
| `searchRadius` | Amount of blocks in between blocks to count connected | `1` |
| `permissionRestricted` | Only players with `veinminer.use` permission can veinmine | `false` |
| `mergeItemDrops` | All item drops are merged to the source block | `false` |
| `autoUpdate` | Check for updates and download new version if available | `false` |
| `durabilityDecrease` | If each mined block should reduce durability | `true` |

</details>
<details><summary>Block Groups - Advanced Settings</summary>
  
  Block groups can hold multiple blocks together that will be treated like the same block.<br>
  `/veinminer group create <name> [<block1>] [<block2>]`

  All blocks inside one group will be mined together. A block can be in multiple groups. New blocks can be added or removed from groups with the following commands:<br>
  `/veinminer group edit <name> add-block <block>`<br>
  `/veinminer group edit <name> remove-block <block>`

  Groups can be limited to certain tools, for example group `wood` can only be mined by axes. If no tool is added to a group, all tools are allowed. If a block is in multiple groups, all tools from those groups are allowed.<br>
  (If a block is in one unlimited tool group and one limited to axes, only axes work for this block)<br>
  `/veinminer group edit <name> add-tool <item>`<br>
  `/veinminer group edit <name> remove-tool <item>`
  
</details>

OP players will have all permissions. To manually grant permissions see [Luckperms](https://luckperms.net/).<br>
For Folia support you need to set delay to 0!

## Some Advice
- To veinmine, your pickaxe must be able to mine the ore in normal conditions (unless disabled in V2)
- While Veinminer is running very lightweight, mining unnatural big veins can lag the client and the server through the amount of items


If you need any help or want to share some ideas to add, just hop on our Discord ([dc.mutils.net](https://dc.mutils.net))

