package me.rubix327.antiitemmove.menu;

import me.rubix327.antiitemmove.Util;
import me.rubix327.antiitemmove.storage.Group;
import me.rubix327.antiitemmove.storage.GroupsStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.MenuUtil;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

public class ResetGroupConfirmMenu extends MenuInterlayer {

    private final Group group;

    public ResetGroupConfirmMenu(Player player, Group group) {
        super(player);
        this.group = group;
    }

    @Override
    protected void setup() {
        setTitle("&lConfirm resetting?");
        addItem(4, getItemButton());
        addButton(11, getConfirmButton());
        addButton(15, getCancelButton());
        addButton(22, getInfoButton());
    }

    private Button getConfirmButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                GroupsStorage.getInstance().reset(group);
                CompSound.NOTE_PLING.play(player, 0.5F, 0.6F);
                new GroupsMenu(player).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.LIME_WOOL).name("&a&lCONFIRM").lore("", "&7This group will be reset.").make();
            }
        };
    }

    private Button getCancelButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                new GroupsMenu(player).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.RED_WOOL).name("&c&lCANCEL").lore("", "&7Return to the previous menu.").make();
            }
        };
    }

    private ItemStack getItemButton(){
        return ItemCreator.of(this.group.getMaterial()).name(Util.capitalizeString(this.group.toString())).make();
    }

    @Override
    protected String getInfoName() {
        return "             &c&l&oWARNING!";
    }

    @Override
    protected String[] getInfoLore() {
        return new String[]{
                "                 &7&m---",
                "&7You're about to reset the group.",
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
