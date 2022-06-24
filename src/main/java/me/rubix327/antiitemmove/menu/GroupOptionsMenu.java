package me.rubix327.antiitemmove.menu;

import me.rubix327.antiitemmove.Util;
import me.rubix327.antiitemmove.storage.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.AdvancedMenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GroupOptionsMenu extends AdvancedMenuPagged<MoveOption> {

    private final Group group;
    private List<IOption> includedOptions;
    private final boolean canEdit;

    public GroupOptionsMenu(Player player, Group group, boolean canEdit) {
        super(player);
        this.group = group;
        updateIncludedOptions();
        this.canEdit = canEdit;
    }

    private void updateIncludedOptions(){
        this.includedOptions = this.group.getIOptions();
    }

    @Override
    protected void setup() {
        setTitle("Group Options");
        setSize(54);
        setLockedSlots(0, 8, 9, 17, 18, 26, 27, 35, 36, 37, 39, 40, 41, 43, 44, 46, 47, 48, 50, 51, 52);
        addButton(45, getGroupsMenuButton());
        addButton(49, getItemButton());
        addButton(53, getSaveExitButton());
    }

    private Button getSaveExitButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                new GroupsMenu(player).display();
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
                new GroupGroupsMenu(player, group, canEdit).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.COMPARATOR)
                        .name(" ").lore("&7 ▶ &bConfigure nested groups... ", "              &8&m&l-------")
                        .make();
            }
        };
    }

    private Button getItemButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                if (!canEdit) return;
                if (clickType == ClickType.RIGHT){
                    new ResetGroupConfirmMenu(player, group).display();
                }
            }

            public String[] getLore(){
                if (!GroupOptionsMenu.this.canEdit){
                    return new String[]{
                            "",
                            " &8You're currently editing this group"
                    };
                }
                else{
                    return new String[]{
                            "",
                            " &8You're currently editing this group",
                            "",
                            " &8▶ &8Click RMB &8to &creset &8the group to the defaults"
                    };
                }
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(group.getMaterial())
                        .name(Util.capitalizeString(group.toString()))
                        .lore(getLore())
                        .hideTags(true)
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
        boolean isSelected = includedOptions.contains(option);
        String toggleLabel = (isSelected ? "disable" : "enable");
        String lore = (canEdit ? "&7▶ Click to " + toggleLabel : (isSelected ? "&7✔ Enabled" : "&7✕ Disabled"));
        return ItemCreator.of(option.getMaterial())
                .name((isSelected ? "&b" : "&f") + option.getDisplayName())
                .lore("", lore)
                .glow(isSelected)
                .hideTags(true)
                .make();
    }

    @Override
    protected void onElementClick(Player player, MoveOption object, int slot, ClickType clickType) {
        if (!checkCanEdit()) return;
        IOption option = this.getElementsSlots().get(slot);
        if (includedOptions.contains(option)){
            GroupsStorage.getInstance().removeElement(group, option);
            CompSound.NOTE_PLING.play(player, 0.5F, 0.6F);
        } else {
            GroupsStorage.getInstance().addElement(group, option);
            CompSound.NOTE_PLING.play(player, 0.5F, 1.19F);
        }
        updateIncludedOptions();
        refreshMenu();
    }

    protected boolean checkCanEdit(){
        if (!this.canEdit){
            CompSound.VILLAGER_NO.play(getPlayer(), 0.5F, 0.7F);
            animateTitle("&4Can't edit predefined group!");
            return false;
        }
        return true;
    }
}
