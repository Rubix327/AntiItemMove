package me.rubix327.antiitemmove.storage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines all the possible ways to move (hand over) items to another player.
 * @see org.bukkit.event.inventory.InventoryType
 */
public enum MoveOption implements IOption{

    CHEST,
    DISPENSER,
    DROPPER,
    FURNACE,
    WORKBENCH,
    CRAFTING,
    ENCHANTING,
    BREWING,
    PLAYER,
    CREATIVE,
    MERCHANT,
    ENDER_CHEST,
    ANVIL,
    SMITHING,
    BEACON,
    HOPPER,
    SHULKER_BOX,
    BARREL,
    BLAST_FURNACE,
    LECTERN,
    SMOKER,
    LOOM,
    CARTOGRAPHY,
    GRINDSTONE,
    STONECUTTER,
    COMPOSTER,
    PLACE,
    DROP,
    DIE;

    /**
     * Get the string list of all available MoveOptions.
     */
    public static List<String> getStringList(){
        return Arrays.stream(values()).map(Enum::toString).collect(Collectors.toList());
    }

    public void add(Group group){
        group.getOptions().add(this);
    }

    public void remove(Group group){
        group.getOptions().remove(this);
    }
}
