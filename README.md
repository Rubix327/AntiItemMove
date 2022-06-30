# AntiItemMove
This plugin forbids players to move specified items (vanilla and custom) in any way - this includes moving to any inventory (chest, furnace, etc.), placing, dropping, placing to item frame and dropping when dying.

#### NOTE that if you are OPped, restrictions will not apply to you because you will have all bypass permissions! To test the restricted item please deop yourself first!

## Showcase

### Convinient Easy To Use GUI
![Restricted Items](https://drive.google.com/u/0/uc?id=1gtUc0snJJcjmpaGTF0qhqfMuvEBke-BH)

### 28 Different Options
![Item Options](https://drive.google.com/u/0/uc?id=176Ok2E2g7SWZ140pF3Xz3LrCcCCNQUTi)

### Pre Created Groups
![a](https://drive.google.com/u/0/uc?id=1IF__5v5Pc9tNWTQt24UXe-S1yIJHuMxb)

## Available options
You can add these options to an item. Not case-sensitive.
<details>
  <summary>Click me</summary>

- Chest
- Dispenser
- Dropper
- Furnace
- Workbench - Disable moving an item to a Crafting Table
- Player_Inventory - Disable moving an item inside a Player inventory (except in Creative mode)
- Enchanting - An Enchantment Table
- Brewing - A Brewing Stand
- Merchant - A Villager
- Ender_Chest
- Anvil
- Beacon
- Hopper
- Smithing
- Shulker_Box
- Barrel
- Blast_Furnace
- Lectern
- Smoker
- Loom
- Cartography
- Grindstone
- Stonecutter
- Composter
- Item_Frame - Disable putting an item into an item frame
- Place - Disable placing an item on the ground (for blocks)
- Drop - Disable dropping an item (Q, Ctrl+Q, etc.)
- Die - Disable dropping an item as a loot when a player dies

</details>

## Predefined groups
These groups are unchangeable. You can only use them in items or other groups. Not case-sensitive.
<details>
  <summary>Click me</summary>

- ALL - Contains all available options
- Containers - [Chest, Ender_Chest, Shulker_Box, Barrel]
- Tool_Blocks - [Dispenser, Dropper, Furnace, Workbench, Enchanting, Brewing, Anvil, Smithing, Beacon, Hopper, Blast_Furnce, Lectern, Smoker, Loom, Cartography, Grindstone, Stonecutter, Composter]
- Safe - [Workbench, Merchant, Ender_Chest, Player_Inventory, Enchanting]
- Not_Safe - All except SAFE
- Other - [Place, Drop, Item_Frame, Die]

</details>

## User groups

You can customize all of these groups as you want. Not case-sensitive.
<details>
  <summary>Click me</summary>

- Default - When an item is added as restricted, it automatically acquires this group. By default this group contains NOT_SAFE inside itself.
- Custom_1 - [Dispenser, Dropper, Furnace] + Default group
- Custom_2 - [Loom, Brewing, Enchanting] + Custom_1 group
- Custom_3 - [Merchant, Anvil, Barrel] + Custom_2 group

</details>

## Commands (& permissions)

- /aim [gui] - Open the plugin’s GUI (`antiitemmove.gui`)
    - `antiitemmove.gui.open.main` - Open the main menu
    - `antiitemmove.gui.open.items` - Open the Items menu
    - `antiitemmove.gui.edit.items` - Edit options and groups of any item
    - `antiitemmove.gui.open.groups` - Open the Groups menu
    - `antiitemmove.gui.edit.groups` - Edit options of any group
- /aim save - Save an item in hand as a restricted (`antiitemmove.save`)
- /aim remove <id> - Remove an item by id (`antiitemmove.remove`)
- /aim get <id> - Get an item by id (`antiitemmove.get`)
- /aim help - Open help page (`antiitemmove.help`)
- /aim reload - Reload the plugin (`antiitemmove.reload`)

## Other permissions (all defaults to op)

- `antiitemmove.bypass.*` - bypass all the restrictions
- `antiitemmove.bypass.item.<id>.<option>` - bypass restrictions for item with id <id> for the selected <option>
    - `antiitemmove.bypass.item.1.*` - bypass all the restrictions for item with id 1
    - `antiitemmove.bypass.item.2.chest` - player can move item with id 2 to a chest
- `antiitemmove.bypass.option.<option>` - bypass restrictions for all items for the selected option
    - `antiitemmove.bypass.option.chest` - player can move all restricted items to a chest
- `antiitemmove.syntax` - Show syntax if the entered command is incorrect
- `antiitemmove.*` - All permissions at once

## Dependencies

We have only one soft-dependency - [ItemsLangAPI](https://www.spigotmc.org/resources/itemslangapi.102979/) - to format item names. Really not necessary.
