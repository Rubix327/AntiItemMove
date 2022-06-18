package me.rubix327.antiitemmove.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.menu.AdvancedMenuPagged;
import org.mineacademy.fo.menu.LockedSlotsFigure;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlocksMenu extends AdvancedMenuPagged<Material> {
    public BlocksMenu(Player player) {
        super(player);
    }

    @Override
    protected void setup() {
        setParent(MainMenu.class);
        setTitle("Блоки");
        setSize(54);
        setPreviousButtonSlot(48);
        setNextButtonSlot(50);
        setPreviousButtonItem(ItemCreator.of(CompMaterial.SPECTRAL_ARROW, "&7Предыдущая страница").make());
        setNextButtonItem(ItemCreator.of(CompMaterial.TIPPED_ARROW, "&7Следующая страница").make());
        setLockedSlots(LockedSlotsFigure.CIRCLE_9X6);
        addButton(49, getReturnBackButton(
                ItemCreator.of(CompMaterial.BLUE_DYE, "&9Вернуться назад").glow(true).make()));
        setPrevNextButtonsEnabledNoPages(true);
    }

    @Override
    protected List<Material> getElements() {
        return Arrays.stream(Material.values())
                .collect(Collectors.toList());
    }

    @Override
    protected ItemStack convertToItemStack(Material element) {
        CompMaterial compMaterial = CompMaterial.fromMaterial(element);
        return ItemCreator.of(
                        compMaterial,
                        ItemUtil.bountifyCapitalized(compMaterial.toString()),
                        "",
                        "&7Нажмите ЛКМ, чтобы получить этот блок",
                        "&7Нажмите ПКМ, чтобы удалить этот блок")
                .make();
    }

    @Override
    protected void onElementClick(Player player, Material material, int slot, ClickType clickType) {
        ItemStack item = ItemCreator.of(CompMaterial.fromMaterial(material)).amount(1).make();
        if (clickType == ClickType.LEFT){
            player.getInventory().addItem(item);
        }
        else if (clickType == ClickType.RIGHT){
            player.getInventory().removeItem(item);
        }
    }
}
