package me.rubix327.antiitemmove.menu;

import me.rubix327.antiitemmove.Settings;
import me.rubix327.antiitemmove.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.MenuUtil;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

public class MainMenu extends MenuInterlayer {

    int restrictedItemsButtonSlot = 11;
    int groupsButtonSlot = 13;
    int reloadButtonSlot = 15;

    public MainMenu(Player player) {
        super(player);
    }

    @Override
    protected void setup() {
        Common.setTellPrefix(Settings.PREFIX);
        setTitle("AntiItemMove Main Menu");
        setSize(27);
        addButton(restrictedItemsButtonSlot, getRestrictedItemsButton());
        addButton(groupsButtonSlot, getGroupsButton());
        addButton(reloadButtonSlot, getReloadButton());
    }

    private Button getRestrictedItemsButton(){
        return new Button() {
            private final ItemStack item = ItemCreator.of(CompMaterial.GRASS_BLOCK).name(" ").lore("&7 ▶ &dRestricted Items ", "         &8&m&l----").make();
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                if (!Util.hasPermissionMenu(player, Settings.Permissions.GUI_OPEN_ITEMS)) {
                    showNoPermission(restrictedItemsButtonSlot);
                    return;
                }
                new ItemsMenu(player).display();
            }

            @Override
            public ItemStack getItem() {
                return item;
            }
        };
    }

    private Button getGroupsButton(){
        return new Button() {
            private final ItemStack item = ItemCreator.of(CompMaterial.HAY_BLOCK).name(" ").lore("&7 ▶ &bGroups ", "     &8&m&l--").make();
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                if (!Util.hasPermissionMenu(player, Settings.Permissions.GUI_OPEN_GROUPS)) {
                    showNoPermission(groupsButtonSlot);
                    return;
                }
                new GroupsMenu(player).display();
            }

            @Override
            public ItemStack getItem() {
                return item;
            }
        };
    }

    private Button getReloadButton(){
        return new Button() {
            private final ItemStack originalItem = ItemCreator.of(CompMaterial.CLOCK).name(" ").lore("&7 ▶ &aReload the plugin ", "          &8&m&l----").make();
            private ItemStack item = originalItem;
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                if (!Util.hasPermissionMenu(player, Settings.Permissions.RELOAD)){
                    showNoPermission(reloadButtonSlot);
                    return;
                }
                Common.dispatchCommandAsPlayer(player, "aim reload");
                CompSound.LEVEL_UP.play(player);
                item = ItemCreator.of(CompMaterial.LIME_WOOL).name("&aPlugin reloaded!").make();
                refreshMenu();
                Common.runLater(30, () -> {
                    if (isViewing(getPlayer())){
                        item = originalItem;
                        refreshMenu();
                    }
                });
            }

            @Override
            public ItemStack getItem() {
                return item;
            }
        };
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (getButtons().containsKey(slot)) return getButtons().get(slot).getItem();
        return MenuUtil.defaultWrapperItem;
    }
}
