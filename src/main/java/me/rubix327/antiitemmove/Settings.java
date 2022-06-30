package me.rubix327.antiitemmove;

import org.mineacademy.fo.Common;

public class Settings {

    public static String PREFIX = "&8[&cAntiItemMove&8] ";

    public static class Permissions{
        private static final String LABEL = "antiitemmove.";
        public static final String SAVE = LABEL + "save";
        public static final String REMOVE = LABEL + "remove";
        public static final String GET = LABEL + "get";
        public static final String HELP = LABEL + "help";
        public static final String SYNTAX = LABEL + "syntax";
        public static final String RELOAD = LABEL + "reload";
        public static final String GUI_OPEN_MAIN = LABEL + "gui.open.main";
        public static final String GUI_OPEN_ITEMS = LABEL + "gui.open.items";
        public static final String GUI_EDIT_ITEMS = LABEL + "gui.edit.items";
        public static final String GUI_OPEN_GROUPS = LABEL + "gui.open.groups";
        public static final String GUI_EDIT_GROUPS = LABEL + "gui.edit.groups";
        public static final String BYPASS_ITEM_ID_OPTION = LABEL + "bypass.item.$id.$option";
        public static final String BYPASS_OPTION = LABEL + "bypass.option.$option";
    }

    public static class Messages{
        public static final String NO_PERMISSION = "&cYou don't have enough permissions.";
        public static final String[] HELP = new String[]{
                "&8" + Common.chatLineSmooth(),
                "&7 Help for &dAntiItemMove v" + AntiItemMoveMain.getVersion() + " &7by Rubix327",
                "&7",
                "&7 &d/aim [gui] &7- Open the plugin's GUI",
                "&7 &d/aim save &7- Save item in hand as restricted",
                "&7 &d/aim get <item_id> &7- Get an item by id",
                "&7 &d/aim help &7- Open this page",
                "&7 &d/aim reload &7- Reload the plugin",
                "&8" + Common.chatLineSmooth()
        };
    }

    public static final String[] ITEMS_DESCRIPTION = new String[]{
            "It is not recommended to edit this file manually.",
            "Please use '/aim save' command to add an item here.",
            "And use '/aim gui' to fully control everything in the plugin.",
            "",
            "Here you can add your restricted items and assign them unique ids.",
            "Item info must match exactly with the real game item that is why it is",
            "recommended to save item via '/aim save' command.",
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
            "CHEST, DISPENSER, DROPPER, FURNACE, WORKBENCH, PLAYER_INVENTORY, ENCHANTING,",
            "BREWING, MERCHANT, ENDER_CHEST, ANVIL, SMITHING, BEACON, HOPPER, SHULKER_BOX, BARREL,",
            "BLAST_FURNACE, LECTERN, SMOKER, LOOM, CARTOGRAPHY, GRINDSTONE, STONECUTTER, COMPOSTER.",
            "",
            "Available other options: PLACE, DROP, ITEM_FRAME, DIE.",
            "Available predefined groups: ALL, CONTAINERS, TOOLS, SAFE, NOT_SAFE, OTHER.",
            "",
            "All options are NOT case-sensitive.",
            "You can use groups inside other groups but try not to use that for the sake of performance.",
            "And avoid referring to the groups from each other (recursion) - it will break the plugin.",
            "",
            "For more detailed info please visit github.com/Rubix327/AntiItemMove"
    };

}
