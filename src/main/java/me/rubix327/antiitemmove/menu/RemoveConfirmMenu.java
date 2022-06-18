package me.rubix327.antiitemmove.menu;

import me.rubix327.antiitemmove.storage.ItemsStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.MenuUtil;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class RemoveConfirmMenu extends AdvancedMenu {

    private final int id;
    private final AdvancedMenu parent;

    public RemoveConfirmMenu(Player player, int id, AdvancedMenu parent) {
        super(player);
        this.id = id;
        this.parent = parent;
    }

    @Override
    protected void setup() {
        setTitle("&lConfirm removal?");
        addItem(4, getItemButton());
        addButton(11, getConfirmButton());
        addButton(15, getCancelButton());
        addButton(22, getInfoButton());
    }

    private Button getConfirmButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {

            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.LIME_WOOL).name("&a&lCONFIRM").lore("", "&7Your item will be removed.").make();
            }
        };
    }

    private Button getCancelButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                parent.display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.RED_WOOL).name("&c&lCANCEL").lore("", "&7Return to the previous menu.").make();
            }
        };
    }

    private ItemStack getItemButton(){
        return ItemCreator.of(ItemsStorage.getInstance().getItem(id)).make();
    }

    @Override
    protected String getInfoName() {
        return "             &c&l&oWARNING!";
    }

    @Override
    protected String[] getInfoLore() {
        return new String[]{
                "                 &7&m---",
                "&7You're about to remove the item",
                "above from the restricted items."
        };
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (!getButtons().containsKey(slot) && !getItems().containsKey(slot)){
            return MenuUtil.defaultWrapperItem;
        }
        return super.getItemAt(slot);
    }
}
