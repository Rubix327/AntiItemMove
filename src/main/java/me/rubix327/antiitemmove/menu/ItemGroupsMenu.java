package me.rubix327.antiitemmove.menu;

import me.rubix327.antiitemmove.Settings;
import me.rubix327.antiitemmove.Util;
import me.rubix327.antiitemmove.storage.BansStorage;
import me.rubix327.antiitemmove.storage.Group;
import me.rubix327.antiitemmove.storage.IOption;
import me.rubix327.antiitemmove.storage.ItemsStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
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

public class ItemGroupsMenu extends MenuPaggedInterlayer<Group> {

    private final int id;
    private final List<IOption> bannedOptions;
    private final ItemStack item;
    private boolean showDescription = false;
    private final int itemButtonSlot = 49;

    public ItemGroupsMenu(Player player, int id) {
        super(player);
        this.id = id;
        bannedOptions = BansStorage.getInstance().getBans(id);
        item = ItemsStorage.getInstance().getItem(id);
    }

    @Override
    protected void setup() {
        setTitle("Item Groups");
        setSize(54);
        setUnlockedSlots(11, 12, 13, 14, 15, 20, 21, 22, 23, 24);
        addButton(45, getOptionsMenuButton());
        addButton(47, getShowDescriptionButton());
        addButton(itemButtonSlot, getItemButton());
        addButton(53, getSaveExitButton());
    }

    private Button getOptionsMenuButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                new ItemOptionsMenu(player, id).display();
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
                if (clickType == ClickType.LEFT){
                    getPlayer().getInventory().addItem(item);
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

    @Override
    protected List<Group> getElements() {
        return Arrays.stream(Group.values())
                .sorted(Comparator.comparingInt(Group::getPriority)).collect(Collectors.toList());
    }

    private List<String> getLore(Group group, String toggleLabel){
        List<String> lore = new ArrayList<>();

        if (group == Group.DEFAULT && !showDescription){
            lore.addAll(Arrays.asList("",
                    "This group will be replaced with its",
                    "correspondent groups and options",
                    "for this item after server reload",
                    "or /aim reload."));
        }
        if (showDescription){
            lore.add("");
            for (String s : group.getAllDisplayNames()){
                lore.add("&l- &7" + Util.capitalizeString(s));
            }
        }
        lore.addAll(Arrays.asList("", "&7▶ Click to " + toggleLabel));
        return lore;
    }

    @Override
    protected ItemStack convertToItemStack(Group group) {
        boolean isSelected = bannedOptions.contains(group);
        String toggleLabel = (isSelected ? "disable" : "enable");
        return ItemCreator.of(group.getMaterial())
                .name(Util.capitalizeString(group.toString()))
                .lore(getLore(group, toggleLabel))
                .glow(isSelected)
                .hideTags(true)
                .make();
    }

    @Override
    protected void onElementClick(Player player, Group object, int slot, ClickType clickType) {
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
