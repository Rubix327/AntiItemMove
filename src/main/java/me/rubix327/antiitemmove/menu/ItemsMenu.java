package me.rubix327.antiitemmove.menu;

import me.rubix327.antiitemmove.storage.IDItemStack;
import me.rubix327.antiitemmove.storage.ItemsStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.AdvancedMenuPagged;
import org.mineacademy.fo.menu.LockedSlotsFigure;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemsMenu extends AdvancedMenuPagged<IDItemStack> {

    private final ItemStack invalidItem = ItemCreator.of(CompMaterial.BARRIER).name("&cThis item is failed to be decoded.").make();

    public ItemsMenu(Player player) {
        super(player);
    }

    @Override
    protected void setup() {
        setTitle("Restricted Items");
        setSize(getElements().size() == 0 ? 9 * 5 : 9 * 6);
        setParent(MainMenu.class);
        setLockedSlots(LockedSlotsFigure.Raw.BOUNDS, getSize());
        addButton(getElements().size() == 0 ? 40 : 49, getReturnBackButton(getReturnItem()));
        if (getElements().size() == 0){
            addItem(22, getEmptyItem());
        }
    }

    private ItemStack getEmptyItem(){
        return ItemCreator.of(CompMaterial.CLOCK)
                .name("It's cold and empty here...")
                .lore("", "&7You haven't added any item yet.",
                        "&7To do it, type '/aim save' while holding",
                        "&7an item you want to restrict in hand.")
                .make();
    }

    @Override
    protected List<IDItemStack> getElements() {
        List<IDItemStack> list = new ArrayList<>();
        for (Map.Entry<Integer, ItemStack> entry : ItemsStorage.getInstance().getItemsMap().entrySet()){
            if (entry.getValue() != null){
                list.add(new IDItemStack(entry.getKey(), entry.getValue()));
            } else {
                list.add(new IDItemStack(entry.getKey(), invalidItem));
            }
        }
        return list;
    }

    @Override
    protected ItemStack convertToItemStack(IDItemStack element) {
        ItemStack item;
        if (isInvalidItem(element)){
            item = ItemCreator.of(element)
                    .lore("&8This may be due to the fact that the item was created",
                    "&8on a server version newer than the current one or",
                    "&8it was not saved through '/aim save' at all.",
                    "&8Please remove this object and add the item again.", "",
                    "&7Click RMB to &cremove &7this object.").make();
        }
        else{
            item = ItemCreator.of(element).lore("", "&8Click to open this item settings.").make();
        }
        return ItemCreator.of(item).lore("&8Config ID: " + element.getId()).make();
    }

    @Override
    protected void onElementClick(Player player, IDItemStack idItem, int slot, ClickType clickType) {
        if (isInvalidItem(idItem)) {
            CompSound.VILLAGER_NO.play(player);
            return;
        }
        new ItemOptionsMenu(player, idItem.getId()).display();
    }

    private boolean isInvalidItem(IDItemStack item){
        return invalidItem.getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName());
    }

    private ItemStack getReturnItem(){
        return ItemCreator.of(CompMaterial.OAK_TRAPDOOR).name(" ")
                .lore("&7 ▶ &aGo back ", "    &8&m&l----")
                .make();
    }
}
