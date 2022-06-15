package me.rubix327.antiitemmove;

public class Settings {

    public static final String[] ITEMS_DESCRIPTION = new String[]{
            "It is not recommended to edit this file manually.",
            "Please use '/aim save' command to add an item here.",
            "And use '/aim gui' to fully control everything in the plugin.",
            "",
            "Here you can add your restricted items and assign it a unique number.",
            "Item info must match exactly with the real game item that is why it is",
            "recommended to use automated system via GUI or '/aim save' command.",
            "",
            "For more detailed info please visit github.com/Rubix327/AntiItemMove"
    };

    public static final String[] BANS_DESCRIPTION = new String[]{
            "It is not recommended to edit this file manually.",
            "Please use '/aim gui' command to do it.",
            "",
            "There are several predefined groups available.",
            "Please see groups.yml for more detailed info about groups.",
            "",
            "You can use these groups like any other option, just type it inside the list.",
            "",
            "For more detailed info please visit github.com/Rubix327/AntiItemMove"
    };

    public static final String[] GROUPS_DESCRIPTION = new String[]{
            "It is not recommended to edit this file manually.",
            "Please use '/aim gui' command to do it.",
            "",
            "Here you can edit the contents of the inventory groups.",
            "- Default - the group that item acquires when it is just added to the restricted items;",
            "- Custom_1(2,3) - your custom groups. You can add anything you like here.",
            "",
            "Available inventories are:",
            "CHEST, DISPENSER, DROPPER, FURNACE, WORKBENCH, CRAFTING, ENCHANTING,",
            "BREWING, PLAYER, CREATIVE, MERCHANT, ENDER_CHEST, ANVIL, SMITHING, BEACON, HOPPER, SHULKER_BOX,",
            "BARREL, BLAST_FURNACE, LECTERN, SMOKER, LOOM, CARTOGRAPHY, GRINDSTONE, STONECUTTER, COMPOSTER.",
            "",
            "Available other options: PLACE, DROP, DIE.",
            "Available predefined groups: ALL, CONTAINERS, TOOLS, SAFE, NOT_SAFE, OTHER.",
            "",
            "All options are NOT case-sensitive.",
            "You can use groups inside other groups but try not to use that for the sake of performance.",
            "And avoid referring to the groups from each other (recursion) - it will break the plugin.",
            "",
            "For more detailed info please visit github.com/Rubix327/AntiItemMove"
    };

}
