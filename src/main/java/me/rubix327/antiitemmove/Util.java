package me.rubix327.antiitemmove;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.exception.CommandException;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

public class Util {

    public static ItemStack NO_PERMISSIONS_ITEM = ItemCreator.of(CompMaterial.RED_WOOL).name("&cNo permission!").make();

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().replace("_", " ").toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') {
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static void checkPermissionCommand(Player player, String permission){
        if (!player.hasPermission(permission)){
            Common.tell(player, Settings.Messages.NO_PERMISSION);
            throw new CommandException();
        }
    }

    public static boolean hasPermissionMenu(Player player, String permission){
        if (!player.hasPermission(permission)){
            Common.tellNoPrefix(player, Settings.PREFIX + Settings.Messages.NO_PERMISSION);
            CompSound.VILLAGER_NO.play(player);
            return false;
        }
        return true;
    }

}
