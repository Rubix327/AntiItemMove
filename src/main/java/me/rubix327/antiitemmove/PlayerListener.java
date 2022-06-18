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
import org.mineacademy.fo.Logger;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.*;

public class PlayerListener implements Listener {

    /**
     * Represents players and the restricted items they must have dropped.
     * This map only stores data between player death and respawn.
     */
    private static final HashMap<UUID, List<ItemStack>> droppedItems = new HashMap<>();

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        try{
            List<String> bans = BansStorage.getInstance().getCachedBans().get(event.getItemDrop().getItemStack());
            if (bans.contains(Group.ALL.toString()) || bans.contains(MoveOption.DROP.toString())) event.setCancelled(true);
        } catch (NullPointerException ignored){ }
    }

    @EventHandler
    public void onItemMove(InventoryClickEvent event){
        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        try{
            List<String> bans = BansStorage.getInstance().getCachedBans().get(item);
            Logger.info(bans.contains(Group.ALL.toString()) || bans.contains(event.getInventory().getType().toString()));
            if (bans.contains(Group.ALL.toString()) || bans.contains(event.getInventory().getType().toString())) event.setCancelled(true);
        } catch (NullPointerException ignored){ }
    }

    @EventHandler
    public void onItemPlace(PlayerInteractEvent event){
        ItemStack item = event.getItem();
        if (!event.hasBlock()) return;
        if (item == null) return;

        try{
            List<String> bans = BansStorage.getInstance().getCachedBans().get(item);
            if (bans.contains(Group.ALL.toString()) || bans.contains(MoveOption.PLACE.toString())) event.setCancelled(true);
        } catch (NullPointerException ignored){ }
    }

    @EventHandler
    public void onItemFramePlace(PlayerInteractEntityEvent event){
        ItemStack item = event.getPlayer().getItemInHand();
        if (CompMaterial.isAir(item)) return;

        List<String> bans = BansStorage.getInstance().getCachedBans().get(item);
        if (bans.contains(Group.ALL.toString()) || bans.contains(MoveOption.ITEM_FRAME.toString())){
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
                    List<String> bans = BansStorage.getInstance().getCachedBans().get(itemStack);
                    if (!bans.contains(MoveOption.DIE.toString())) continue;
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
