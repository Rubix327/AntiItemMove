package me.rubix327.antiitemmove.menu;

import me.rubix327.antiitemmove.Util;
import me.rubix327.antiitemmove.storage.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GroupsMenu extends MenuPaggedInterlayer<Group> {
    public GroupsMenu(Player player) {
        super(player);
    }

    @Override
    protected void setup() {
        setParent(MainMenu.class);
        setSize(9 * 5);
        setTitle("Groups");
        setUnlockedSlots(11, 12, 13, 14, 15, 20, 21, 22, 23, 24);
        addButton(40, getReturnBackButton(ItemCreator.of(CompMaterial.OAK_TRAPDOOR)
                .name(" ")
                .lore("&7 ▶ &aGo back ", "    &8&m&l----")
                .make()));
    }

    @Override
    protected List<Group> getElements() {
        return Arrays.stream(Group.values())
                .sorted(Comparator.comparingInt(Group::getPriority)).collect(Collectors.toList());
    }

    @Override
    protected ItemStack convertToItemStack(Group group) {
        String lore = (Group.getPredefined().contains(group) ? "&7▶ Click to view" : " &7▶ Click to edit");
        return ItemCreator.of(group.getMaterial())
                .name(Util.capitalizeString(group.toString()))
                .lore("", lore)
                .hideTags(true)
                .make();
    }

    @Override
    protected void onElementClick(Player player, Group group, int slot, ClickType clickType) {
        if (Group.getPredefined().contains(group)){
            new GroupOptionsMenu(player, group, false).display();
        } else {
            new GroupOptionsMenu(player, group, true).display();
        }
    }
}
