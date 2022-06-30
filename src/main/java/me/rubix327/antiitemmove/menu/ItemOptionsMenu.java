package me.rubix327.antiitemmove.menu;

import me.rubix327.antiitemmove.Settings;
import me.rubix327.antiitemmove.Util;
import me.rubix327.antiitemmove.storage.BansStorage;
import me.rubix327.antiitemmove.storage.IOption;
import me.rubix327.antiitemmove.storage.ItemsStorage;
import me.rubix327.antiitemmove.storage.MoveOption;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemOptionsMenu extends MenuPaggedInterlayer<MoveOption> {

    private final int id;
    private final List<IOption> bannedOptions;
    private final ItemStack item;
    private final int itemButtonSlot = 49;

    public ItemOptionsMenu(Player player, int id) {
        super(player);
        this.id = id;
        bannedOptions = BansStorage.getInstance().getBans(id);
        item = ItemsStorage.getInstance().getItem(id);
    }

    @Override
    protected void setup() {
        setTitle("Item Options");
        setSize(54);
        setLockedSlots(0, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52);
        addButton(45, getGroupsMenuButton());
        addButton(itemButtonSlot, getItemButton());
        addButton(53, getSaveExitButton());
    }

    private Button getSaveExitButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                new ItemsMenu(player).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.OAK_TRAPDOOR)
                        .name(" ").lore("&7 ▶ &aSave and go back ", "          &8&m&l----")
                        .make();
            }
        };
    }

    private Button getGroupsMenuButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                new ItemGroupsMenu(player, id).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.COMPARATOR)
                        .name(" ").lore("&7 ▶ &bConfigure groups... ", "           &8&m&l----")
                        .make();
            }
        };
    }

    private Button getItemButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                if (clickType == ClickType.LEFT){
                    if (!Util.hasPermissionMenu(player, Settings.Permissions.GET)) return;
                    getPlayer().getInventory().addItem(item);
                    CompSound.CHICKEN_EGG_POP.play(player);
                } else if (clickType == ClickType.RIGHT){
                    if (!Util.hasPermissionMenu(player, Settings.Permissions.REMOVE)){
                        showNoPermission(itemButtonSlot);
                        return;
                    }
                    new RemoveItemConfirmMenu(player, id).display();
                }
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(item)
                        .lore("",
                                " &8You're currently editing this item (id: " + id + ")",
                                "",
                                " &8▶ &8Click LMB &8to &aget &8the item",
                                " &8▶ &8Click RMB &8to &cremove &8the item from restricted")
                        .make();
            }
        };
    }

    @Override
    protected List<MoveOption> getElements() {
        return Arrays.stream(MoveOption.values()).collect(Collectors.toList());
    }

    @Override
    protected ItemStack convertToItemStack(MoveOption option) {
        boolean isSelected = bannedOptions.contains(option);
        String toggleLabel = (isSelected ? "disable" : "enable");
        return ItemCreator.of(option.getMaterial())
                .name((isSelected ? "&b" : "&f") + option.getDisplayName())
                .lore("", "&7▶ Click to " + toggleLabel)
                .glow(isSelected)
                .hideTags(true)
                .make();
    }

    @Override
    protected void onElementClick(Player player, MoveOption object, int slot, ClickType clickType) {
        if (!Util.hasPermissionMenu(player, Settings.Permissions.GUI_EDIT_ITEMS)) {
            showNoPermission(slot);
            return;
        }
        IOption option = this.getElementsSlots().get(slot);
        if (bannedOptions.contains(option)){
            BansStorage.getInstance().removeOptions(id, option);
            CompSound.NOTE_PLING.play(player, 0.5F, 0.6F);
        } else {
            BansStorage.getInstance().addOptions(id, option);
            CompSound.NOTE_PLING.play(player, 0.5F, 1.19F);
        }
        BansStorage.getInstance().updateCachedBansFor(item);
        refreshMenu();
    }
}
