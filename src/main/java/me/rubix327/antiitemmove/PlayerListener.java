package me.rubix327.antiitemmove;

import me.rubix327.antiitemmove.storage.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Logger;

import java.io.IOException;
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
            List<String> bans = getBans(event.getItemDrop().getItemStack());
            if (bans.contains(Group.ALL.toString()) || bans.contains("DROP")) event.setCancelled(true);
        } catch (NullPointerException ignored){ }
    }

    @EventHandler
    public void onItemMove(InventoryClickEvent event){
        if (event.getCurrentItem() == null) return;

        try{
            List<String> bans = getBans(event.getCurrentItem());
            if (bans.contains(Group.ALL.toString()) || bans.contains(event.getInventory().getType().toString())) event.setCancelled(true);
        } catch (NullPointerException ignored){ }
    }

    @EventHandler
    public void onItemPlace(PlayerInteractEvent event){
        if (!event.hasBlock()) return;
        if (event.getItem() == null) return;
        if (event.getItem().getType() == Material.AIR) return;

        try{
            List<String> bans = getBans(event.getItem());
            if (bans.contains(Group.ALL.toString()) || bans.contains(MoveOption.PLACE.toString())) event.setCancelled(true);
        } catch (NullPointerException ignored){ }
    }

    @EventHandler
    public void onItemWasteDeath(PlayerDeathEvent event) throws IOException {
        if (event.getDrops().isEmpty()) return;
        for (String itemString : ItemsStorage.getInstance().getItems().values()){
            for (ItemStack drop : event.getDrops()){
                if (drop.isSimilar(ItemsStorage.getItemFromString(itemString))){
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

    public List<String> getBans(ItemStack item) throws NullPointerException {
        ItemStack newItem = item.clone();

        Integer itemKey = ItemsStorage.getInstance().getKey(newItem);
        if (itemKey == null) throw new NullPointerException("This item is not restricted.");

        List<String> bans = BansStorage.getInstance().get(itemKey);
        if (bans == null || bans.isEmpty()) throw new NullPointerException("This item has no bans associated.");
        bans.replaceAll(String::toUpperCase);
        bans = new ArrayList<>(replaceBanGroups(new HashSet<>(bans)));

        return bans;
    }

    /**
     * Replace groups with their corresponding options.<br>
     * Example: CONTAINERS -> "CHEST", "ENDER_CHEST", "SHULKER_BOX", "BARREL".<br>
     * Values cannot be repeated.
     */
    private HashSet<String> replaceBanGroups(HashSet<String> bannedOptions){
        for (Group group : Group.getUserDefined()){
            if (!bannedOptions.contains(group.toString())) continue;
            for (String element : group.getOptionsAndGroups()){
                if (Group.getUserDefinedString().contains(element)) {
                    if (Group.valueOf(element).getOptionsAndGroups().contains(group.toString())){
                        Logger.warning("An error occurred when moving an item. Groups " + group + " and " + element
                        + " are referring to each other. Key " + element + " in group " + group + " has been removed.");
                        GroupsStorage.getInstance().removeElement(group, IOption.valueOf(element));
                        return new HashSet<>(Collections.singletonList(Group.ALL.toString()));
                    }
                }
            }
        }

        HashSet<String> set = new HashSet<>(bannedOptions);
        for (String current : bannedOptions){
            if (current.equals(Group.ALL.toString())) continue;
            for (String predefined : Group.getStringList()){
                if (current.equals(predefined)){
                    set.remove(predefined);
                    set.addAll(Group.valueOf(predefined.toUpperCase()).getOptionsStringList());
                    set = replaceBanGroups(set);
                }
            }
        }
        return set;
    }

}
