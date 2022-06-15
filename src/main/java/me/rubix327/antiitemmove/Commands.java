package me.rubix327.antiitemmove;

import me.rubix327.antiitemmove.storage.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Logger;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.command.SimpleCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commands extends SimpleCommand {
    protected Commands() {
        super("antiitemmove|aim");
        setAutoHandleHelp(false);
    }

    @Override
    protected void onCommand() {
        String syntax = "&7Syntax: &e/aim <gui | save | get | removeGroup | help>";
        Common.setTellPrefix("");
        checkConsole();
        checkArgs(1, syntax);

        if ("save".equalsIgnoreCase(args[0])){
            ItemStack item = getPlayer().getInventory().getItemInHand();
            if (item.getType() == Material.AIR) returnTell("You must hold an item to save it as restricted.");

            ItemsStorage.getInstance().add(ItemsStorage.getStringFromItem(item));
            int id = ItemsStorage.getInstance().getMaxId();
            BansStorage.getInstance().add(id, Group.DEFAULT.getOptionsAndGroups());

            item.setItemMeta(Bukkit.getItemFactory().getItemMeta(Material.STONE));

            String name = getItemName(item);
            Messenger.success(getPlayer(), "&7You have successfully added &e" +
                    name + "&7 to the restricted items by id &e#" + id + "&7.");
        }
        else if ("get".equalsIgnoreCase(args[0])){
            checkArgs(2, "&7Syntax: &e/aim get <item_id>");
            int number = findNumber(1, "&7Item ID must be whole number.");
            ItemStack item = null;
            try {
                item = ItemsStorage.getItemFromString(ItemsStorage.getInstance().getItems().get(number));
            } catch (IOException | IllegalArgumentException e) {
                returnTell("&cFailed to get the item from the file. Check that its encoded data is correct.");
            } catch (ClassCastException e){
                returnTell("&cFailed to get the item from the file. Probably this item is from 1.13+ " +
                        "version that is not supported on " + MinecraftVersion.getCurrent().toString() + " (current).");
            } catch (NullPointerException e){
                returnTell("&cThis item id does not exist.");
            }
            if (item == null) returnTell("&cUnable to get this item.");
            getPlayer().getInventory().addItem(item);

            String name = getItemName(item);
            tellSuccess("&7You were given the &e" + name + "&7 with id &e#" + number + "&7.");
        }
        else if ("removeGroup".equalsIgnoreCase(args[0])){
            checkArgs(3, "&7Syntax: &e/aim remove <from_group> <option>");
            Logger.info(Group.CUSTOM_1.getOptionsAndGroups());
            Group group = Group.getOrNull(args[1]);
            if (group == null) returnTell("&cThis group does not exist.");
            IOption option = IOption.getOrNull(args[2]);
            if (option == null) returnTell("&cThis MoveOption or Group does not exist.");

            try{
                GroupsStorage.getInstance().removeElement(group, option);
            } catch (NullPointerException e){
                returnTell("&cThis group does not have this option.");
            }

            tellSuccess("Option &e" + option + "&7 has been successfully removed from group &e" + group + "&7.");
            Logger.info(Group.CUSTOM_1.getOptionsAndGroups());
        }
        else if ("?".equalsIgnoreCase(args[0]) || "help".equalsIgnoreCase(args[0])){

            List<String> messages = Arrays.asList(
                    "&8" + Common.chatLineSmooth(),
                    "&7 Help for &dAntiItemMove v" + AntiItemMoveMain.getVersion() + " &7by Rubix327",
                    "&7",
                    "&7 &d/aim gui &7- Open the plugin GUI",
                    "&7 &d/aim info &7- Show info about the item in your hand",
                    "&7 &d/aim save &7- Save this item as restricted",
                    "&7 &d/aim help &7- Open this page",
                    "&8" + Common.chatLineSmooth()
            );
            Common.tell(getPlayer(), messages);
        }
        else{
            Messenger.error(getPlayer(), syntax);
        }

    }

    @Override
    protected List<String> tabComplete() {
        return new ArrayList<>(Arrays.asList("gui", "save", "get", "removeGroup", "help"));
    }

    private String getItemName(ItemStack item){
        if (MinecraftVersion.olderThan(MinecraftVersion.V.v1_13)){
            if (item.getData().getData() != 0){
                return item.getType() + ":" + item.getData().getData();
            }
        }
        return item.getType().toString();
    }
}
