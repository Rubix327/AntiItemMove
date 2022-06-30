package me.rubix327.antiitemmove;

import me.rubix327.antiitemmove.menu.MainMenu;
import me.rubix327.antiitemmove.storage.BansStorage;
import me.rubix327.antiitemmove.storage.Group;
import me.rubix327.antiitemmove.storage.ItemsStorage;
import me.rubix327.itemslangapi.ItemsLangAPI;
import me.rubix327.itemslangapi.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Commands extends SimpleCommand {
    protected Commands() {
        super("antiitemmove|aim");
        setAutoHandleHelp(false);
        setPermission(null);
    }

    @Override
    protected void onCommand() {
        String syntax = "&7Syntax: &e/aim <gui | save | remove | get | help | reload>";
        Common.setTellPrefix(Settings.PREFIX);
        checkConsole();

        if (args.length == 0){
            Util.checkPermissionCommand(getPlayer(), Settings.Permissions.GUI_OPEN_MAIN);
            openMainMenu(getPlayer());
        }
        else if ("gui".equalsIgnoreCase(args[0]) || "menu".equalsIgnoreCase(args[0])){
            Util.checkPermissionCommand(getPlayer(), Settings.Permissions.GUI_OPEN_MAIN);
            openMainMenu(getPlayer());
        }
        else if ("save".equalsIgnoreCase(args[0])){
            Util.checkPermissionCommand(getPlayer(), Settings.Permissions.SAVE);
            ItemStack item = getPlayer().getInventory().getItemInHand().clone();
            if (item.getType() == Material.AIR) returnTell("You must hold an item to save it as restricted.");

            item.setAmount(1);
            if (ItemsStorage.getInstance().getItemsMap().containsValue(item)){
                returnTell("&cThis item is already added as restricted.");
            }
            ItemsStorage.getInstance().add(item);
            int id = ItemsStorage.getInstance().getMaxId();
            BansStorage.getInstance().addOptions(id, Group.DEFAULT.getIOptions());

            String translated = null;
            if (DependencyManager.ITEMS_LANG_API.isLoaded()){
                translated = ItemsLangAPI.getApi().translate(item, Lang.EN_US);
            }
            String name = (translated == null ? Util.capitalizeString(item.getType().toString()) : translated);
            Messenger.success(getPlayer(), "&7You have successfully added &e" +
                    name + "&7 to the restricted items by id &e#" + id + "&7.");
        }
        else if ("remove".equalsIgnoreCase(args[0])){
            Util.checkPermissionCommand(getPlayer(), Settings.Permissions.REMOVE);
            checkArgs(2, "&7Syntax: &e/aim remove <item_id>");
            int id = findNumber(1, "&7Item ID must be whole number.");

            ItemStack item = ItemsStorage.getInstance().getItem(id);
            if (item == null){
                returnTell("&7There is no restricted item with id " + id + ".");
            }

            ItemsStorage.getInstance().remove(id);
            BansStorage.getInstance().remove(id);

            String translated = null;
            if (DependencyManager.ITEMS_LANG_API.isLoaded()){
                translated = ItemsLangAPI.getApi().translate(item, Lang.EN_US);
            }
            String name = (translated == null ? Util.capitalizeString(item.getType().toString()) : translated);
            Messenger.success(getPlayer(), "&7You have successfully removed &e" +
                    name + "&7 (id: " + id + ") from the restricted items.");
        }
        else if ("get".equalsIgnoreCase(args[0])){
            Util.checkPermissionCommand(getPlayer(), Settings.Permissions.GET);
            checkArgs(2, "&7Syntax: &e/aim get <item_id>");
            int number = findNumber(1, "&7Item ID must be whole number.");
            ItemStack item = null;
            try {
                item = ItemsStorage.getInstance().getItemsMap().get(number);
            } catch (IllegalArgumentException e) {
                returnTell("&cFailed to get the item from the file. Check that its encoded data is correct.");
            } catch (ClassCastException e){
                returnTell("&cFailed to get the item from the file. Probably this item is from 1.13+ " +
                        "version that is not supported on " + MinecraftVersion.getCurrent().toString() + " (current).");
            } catch (NullPointerException e){
                returnTell("&cThis item id does not exist.");
            }
            if (item == null) returnTell("&cUnable to get this item.");
            getPlayer().getInventory().addItem(item);

            String translated = null;
            if (DependencyManager.ITEMS_LANG_API.isLoaded()){
                translated = ItemsLangAPI.getApi().translate(item, Lang.EN_US);
            }
            String name = (translated == null ? Util.capitalizeString(item.getType().toString()) : translated);
            tellSuccess("&7You were given the &e" + name + "&7 with id &e#" + number + "&7.");
        }
        else if ("?".equalsIgnoreCase(args[0]) || "help".equalsIgnoreCase(args[0])){
            Util.checkPermissionCommand(getPlayer(), Settings.Permissions.HELP);
            Common.tellNoPrefix(getPlayer(), Settings.Messages.HELP);
        }
        else if ("reload".equalsIgnoreCase(args[0])){
            Util.checkPermissionCommand(getPlayer(), Settings.Permissions.RELOAD);
            AntiItemMoveMain.getInstance().loadFiles();
            Common.tell(getPlayer(), "&7Configuration reloaded!");
        }
        else{
            Util.checkPermissionCommand(getPlayer(), Settings.Permissions.SYNTAX);
            Messenger.error(getPlayer(), syntax);
        }

    }

    private void openMainMenu(Player player){
        new MainMenu(player).display();
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1){
            return new ArrayList<>(Arrays.asList("gui", "save", "remove", "get", "help", "reload"));
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("remove")){
            return Collections.singletonList("<item_id>");
        }
        return Collections.singletonList("");
    }
}
