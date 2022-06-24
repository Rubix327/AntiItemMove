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
import org.mineacademy.fo.exception.CommandException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commands extends SimpleCommand {
    protected Commands() {
        super("antiitemmove|aim");
        setAutoHandleHelp(false);
        setPermission(null);
    }

    @Override
    protected void onCommand() {
        String syntax = "&7Syntax: &e/aim <gui | save | get | help | reload>";
        Common.setTellPrefix("&8[&4AntiItemMove&8] ");
        checkConsole();

        if (args.length == 0){
            checkPermission("antiitemmove.gui");
            openMainMenu(getPlayer());
        }
        else if ("gui".equalsIgnoreCase(args[0]) || "menu".equalsIgnoreCase(args[0])){
            checkPermission("antiitemmove.gui");
            openMainMenu(getPlayer());
        }
        else if ("save".equalsIgnoreCase(args[0])){
            checkPermission("antiitemmove.save");
            ItemStack item = getPlayer().getInventory().getItemInHand().clone();
            if (item.getType() == Material.AIR) returnTell("You must hold an item to save it as restricted.");

            item.setAmount(1);
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
        else if ("get".equalsIgnoreCase(args[0])){
            checkPermission("antiitemmove.get");
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
            checkPermission("antiitemmove.help");

            List<String> messages = Arrays.asList(
                    "&8" + Common.chatLineSmooth(),
                    "&7 Help for &dAntiItemMove v" + AntiItemMoveMain.getVersion() + " &7by Rubix327",
                    "&7",
                    "&7 &d/aim [gui] &7- Open the plugin's GUI",
                    "&7 &d/aim save &7- Save item in hand as restricted",
                    "&7 &d/aim get <item_id> &7- Get an item by id",
                    "&7 &d/aim help &7- Open this page",
                    "&7 &d/aim reload &7- Reload the plugin",
                    "&8" + Common.chatLineSmooth()
            );
            Common.tellNoPrefix(getPlayer(), messages);
        }
        else if ("reload".equalsIgnoreCase(args[0])){
            checkPermission("antiitemmove.reload");
            AntiItemMoveMain.getInstance().loadFiles();
            Common.tell(getPlayer(), "&aConfiguration reloaded!");
        }
        else{
            checkPermission("antiitemmove.syntax");
            Messenger.error(getPlayer(), syntax);
        }

    }

    private void openMainMenu(Player player){
        new MainMenu(player).display();
    }

    @Override
    protected List<String> tabComplete() {
        return new ArrayList<>(Arrays.asList("gui", "save", "get", "help", "reload"));
    }

    private void checkPermission(String permission){
        if (!getPlayer().hasPermission(permission)){
            Common.tell(getPlayer(), "&cYou don't have enough permissions.");
            throw new CommandException();
        }
    }
}
