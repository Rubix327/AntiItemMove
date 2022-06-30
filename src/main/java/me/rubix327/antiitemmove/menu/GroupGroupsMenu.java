package me.rubix327.antiitemmove.menu;

import me.rubix327.antiitemmove.Settings;
import me.rubix327.antiitemmove.Util;
import me.rubix327.antiitemmove.storage.Group;
import me.rubix327.antiitemmove.storage.GroupsStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GroupGroupsMenu extends MenuPaggedInterlayer<Group> {

    private final Group group;
    private List<Group> bannedGroups;
    private final ItemStack item;
    private boolean showDescription = false;
    private final boolean canEdit;

    public GroupGroupsMenu(Player player, Group group, boolean canEdit) {
        super(player);
        this.group = group;
        updateBannedOptions();
        item = this.group.getMaterial().toItem();
        this.canEdit = canEdit;
    }

    private void updateBannedOptions(){
        this.bannedGroups = this.group.getGroups();
    }

    @Override
    protected void setup() {
        Common.setTellPrefix(Settings.PREFIX);
        setTitle("Item Groups");
        setSize(54);
        setUnlockedSlots(11, 12, 13, 14, 15, 20, 21, 22, 23, 24);
        addButton(45, getOptionsMenuButton());
        addButton(47, getShowDescriptionButton());
        addButton(49, getItemButton());
        addButton(53, getSaveExitButton());
    }

    private Button getOptionsMenuButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                new GroupOptionsMenu(player, group, canEdit).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.COMPARATOR)
                        .name(" ").lore("&7 ▶ &dConfigure options... ", "           &8&m&l----")
                        .make();
            }
        };
    }

    private Button getShowDescriptionButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                showDescription = !showDescription;
                CompSound.CHICKEN_EGG_POP.play(player, 0.5F, (showDescription ? 1.2F : 0.8F));
                refreshMenu();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.REDSTONE_TORCH).name(" ")
                        .lore("&f ▶ " + (showDescription ? "Hide" : "Show") + " full description ",
                                "           &8&m&l----")
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
                if (!GroupGroupsMenu.this.canEdit){
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
                return ItemCreator.of(item)
                        .name(Util.capitalizeString(group.toString()))
                        .lore(getLore())
                        .hideTags(true)
                        .make();
            }
        };
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

    @Override
    protected List<Group> getElements() {
        return Arrays.stream(Group.values())
                .sorted(Comparator.comparingInt(Group::getPriority)).collect(Collectors.toList());
    }

    private List<String> getLore(Group group, boolean isSelected){
        List<String> lore = new ArrayList<>();
        String toggleLabel = (isSelected ? "disable" : "enable");

        if (showDescription){
            lore.add("");
            for (String s : group.getAllDisplayNames()){
                lore.add("&l- &7" + Util.capitalizeString(s));
            }
        }
        if (canEdit) {
            lore.addAll(Arrays.asList("", "&7▶ Click to " + toggleLabel));
        }
        else{
            lore.addAll(Arrays.asList("", isSelected ? "&7✔ Enabled" : "&7✕ Disabled"));
        }
        return lore;
    }

    @Override
    protected ItemStack convertToItemStack(Group group) {
        boolean isSelected = bannedGroups.contains(group);
        return ItemCreator.of(group.getMaterial())
                .name(Util.capitalizeString(group.toString()))
                .lore(getLore(group, isSelected))
                .glow(isSelected)
                .hideTags(true)
                .make();
    }

    @Override
    protected void onElementClick(Player player, Group object, int slot, ClickType clickType) {
        if (!Util.hasPermissionMenu(player, Settings.Permissions.GUI_EDIT_GROUPS)) {
            showNoPermission(slot);
            return;
        }
        if (!checkCanEdit()) return;
        Group group = Group.getOrNull(this.getElementsSlots().get(slot).toString());
        if (group == null) return;
        if (group == this.group){
            CompSound.VILLAGER_NO.play(player, 0.5F, 0.7F);
            return;
        }
        player.sendMessage(bannedGroups.toString());
        if (bannedGroups.contains(group)){
            GroupsStorage.getInstance().removeElement(this.group, group);
            CompSound.NOTE_PLING.play(player, 0.5F, 0.6F);
        } else {
            GroupsStorage.getInstance().addElement(this.group, group);
            CompSound.NOTE_PLING.play(player, 0.5F, 1.19F);
        }
        updateBannedOptions();
        refreshMenu();
    }

    protected boolean checkCanEdit(){
        if (!this.canEdit){
            CompSound.VILLAGER_NO.play(getPlayer(), 0.5F, 0.7F);
            Common.tell(getPlayer(), "&cCan't edit predefined group!");
            return false;
        }
        return true;
    }

}
