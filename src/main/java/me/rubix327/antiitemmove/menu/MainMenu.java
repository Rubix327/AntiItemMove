package me.rubix327.antiitemmove.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.MenuUtil;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class MainMenu extends AdvancedMenu {
    public MainMenu(Player player) {
        super(player);
    }

    @Override
    protected void setup() {
        setTitle("AntiItemMove Main Menu");
        setSize(27);
        addButton(11, getMenuButton(ItemsMenu.class,
                ItemCreator.of(CompMaterial.GRASS_BLOCK)
                        .name(" ")
                        .lore("&7 ▶ &dRestricted Items ", "         &8&m&l----")
                        .make()));
        addButton(13, getMenuButton(GroupsMenu.class,
                ItemCreator.of(CompMaterial.HAY_BLOCK)
                        .name(" ")
                        .lore("&7 ▶ &bGroups ", "     &8&m&l--")
                        .make()));
        addButton(15, getReloadButton());
        setTitleAnimationDurationTicks(30);
    }

    private Button getReloadButton(){
        return new Button() {
            private ItemStack item = ItemCreator.of(CompMaterial.CLOCK).name(" ").lore("&7 ▶ &aReload the plugin ", "          &8&m&l----").make();
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                Common.dispatchCommandAsPlayer(player, "aim reload");
                animateTitle("&0Plugin reloaded!");
                item = ItemCreator.of(CompMaterial.LIME_WOOL).name("&aPlugin reloaded!").make();
                refreshMenu();
                Common.runLater(30, () -> {
                    item = ItemCreator.of(CompMaterial.CLOCK).name(" ").lore("&7 ▶ &aReload the plugin ", "          &8&m&l----").make();
                    refreshMenu();
                });
            }

            @Override
            public ItemStack getItem() {
//                return ItemCreator.of(CompMaterial.CLOCK).name(" ").lore("&7 ▶ &aReload the plugin ", "    &8&m&l---").make();
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
