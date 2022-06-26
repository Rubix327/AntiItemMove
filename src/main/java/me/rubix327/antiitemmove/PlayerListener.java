package me.rubix327.antiitemmove;

import me.rubix327.antiitemmove.storage.*;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.*;

public class PlayerListener implements Listener {

    /**
     * Represents players and the restricted items they must have dropped.
     * This map only stores data between player death and respawn.
     */
    private static final HashMap<UUID, List<ItemStack>> droppedItems = new HashMap<>();
    private static final String optionPerm = "antiitemmove.bypass.option.";
    private static final String itemPerm = "antiitemmove.bypass.item.";

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        ItemStack item = event.getItemDrop().getItemStack();
        item = item.clone();
        item.setAmount(1);

        Integer itemId = ItemsStorage.getInstance().getKey(item);
        if (itemId == null || event.getPlayer().hasPermission(optionPerm + "drop")
                || event.getPlayer().hasPermission(itemPerm + itemId + ".drop")
                ){
            return;
        }

        try{
            List<IOption> bans = BansStorage.getInstance().getCachedBans().get(item);
            if (bans.contains(Group.ALL) || bans.contains(MoveOption.DROP)) event.setCancelled(true);
        } catch (NullPointerException ignored){ }
    }

    @EventHandler
    public void onItemMove(InventoryClickEvent event){
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        item = item.clone();
        item.setAmount(1);
        String inventory = event.getInventory().getType().toString();

        Integer itemId = ItemsStorage.getInstance().getKey(item);
        if (itemId == null || event.getWhoClicked().hasPermission(optionPerm + inventory)
                || event.getWhoClicked().hasPermission(itemPerm + itemId + "." + inventory)){
            return;
        }

        try{
            List<IOption> bans = BansStorage.getInstance().getCachedBans().get(item);
            if (bans.contains(Group.ALL) || bans.contains(IOption.getOrNull(inventory))) {
                event.setCancelled(true);
            }
        } catch (NullPointerException ignored){ }
    }

    @EventHandler
    public void onItemPlace(PlayerInteractEvent event){
        ItemStack item = event.getItem();
        if (!event.hasBlock()) return;
        if (item == null) return;
        item = item.clone();
        item.setAmount(1);

        Integer itemId = ItemsStorage.getInstance().getKey(item);
        if (itemId == null || event.getPlayer().hasPermission(optionPerm + "place")
                || event.getPlayer().hasPermission(itemPerm + itemId + ".place")){
            return;
        }

        try{
            List<IOption> bans = BansStorage.getInstance().getCachedBans().get(item);
            if (bans.contains(Group.ALL) || bans.contains(MoveOption.PLACE)) event.setCancelled(true);
        } catch (NullPointerException ignored){ }
    }

    @EventHandler
    public void onItemFramePlace(PlayerInteractEntityEvent event){
        ItemStack item = event.getPlayer().getItemInHand();
        if (CompMaterial.isAir(item)) return;
        item = item.clone();
        item.setAmount(1);

        Integer itemId = ItemsStorage.getInstance().getKey(item);
        if (itemId == null || event.getPlayer().hasPermission(optionPerm + "item_frame")
                || event.getPlayer().hasPermission(itemPerm + itemId + ".item_frame")){
            return;
        }

        List<IOption> bans = BansStorage.getInstance().getCachedBans().get(item);
        if (bans.contains(Group.ALL) || bans.contains(MoveOption.ITEM_FRAME)){
            if (event.getRightClicked() instanceof ItemFrame){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemWasteDeath(PlayerDeathEvent event) {
        if (event.getDrops().isEmpty()) return;
        for (ItemStack itemStack : ItemsStorage.getInstance().getItemsMap().values()){
            for (ItemStack drop : event.getDrops()){
                if (drop.isSimilar(itemStack)){
                    List<IOption> bans = BansStorage.getInstance().getCachedBans().get(itemStack);
                    if (!bans.contains(MoveOption.DIE)) continue;

                    Integer itemId = ItemsStorage.getInstance().getKey(itemStack);
                    if (itemId == null || event.getEntity().hasPermission(optionPerm + "die")
                            || event.getEntity().hasPermission(itemPerm + itemId + ".die")){
                        return;
                    }

                    if (droppedItems.containsKey(event.getEntity().getUniqueId())){
                        droppedItems.get(event.getEntity().getUniqueId()).add(drop.clone());
                    }
                    else{
                        droppedItems.put(event.getEntity().getUniqueId(), new ArrayList<>(Collections.singletonList(drop.clone())));
                    }
                    drop.setType(Material.AIR);
                }
            }
        }

    }

    @EventHandler
    public void onItemWasteRespawn(PlayerRespawnEvent event){
        if (droppedItems.isEmpty()) return;
        Player player = event.getPlayer();
        if (droppedItems.containsKey(player.getUniqueId())){
            for (ItemStack item : droppedItems.get(player.getUniqueId())){
                player.getInventory().addItem(item);
            }
        }
        droppedItems.get(player.getUniqueId()).clear();
    }

}
