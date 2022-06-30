package me.rubix327.antiitemmove.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.rubix327.antiitemmove.Util;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines all the possible ways to move (hand over) items to another player.
 * @see org.bukkit.event.inventory.InventoryType
 */
@AllArgsConstructor
@RequiredArgsConstructor
public enum MoveOption implements IOption{

    CHEST(CompMaterial.CHEST),
    DISPENSER(CompMaterial.DISPENSER),
    DROPPER(CompMaterial.DROPPER),
    FURNACE(CompMaterial.FURNACE),
    WORKBENCH(CompMaterial.CRAFTING_TABLE, "Crafting Table"),
    PLAYER_INVENTORY(CompMaterial.PLAYER_HEAD, "Player Inventory"),
    ENCHANTING(CompMaterial.ENCHANTING_TABLE, "Enchantment Table"),
    BREWING(CompMaterial.BREWING_STAND, "Brewing Stand"),
    MERCHANT(CompMaterial.EMERALD_BLOCK, "Villager"),
    ENDER_CHEST(CompMaterial.ENDER_CHEST),
    ANVIL(CompMaterial.ANVIL),
    BEACON(CompMaterial.BEACON),
    HOPPER(CompMaterial.HOPPER),
    SMITHING(CompMaterial.CHAINMAIL_CHESTPLATE, CompMaterial.SMITHING_TABLE, MinecraftVersion.V.v1_14),
    SHULKER_BOX(CompMaterial.TNT_MINECART, CompMaterial.SHULKER_BOX, MinecraftVersion.V.v1_9),
    BARREL(CompMaterial.FURNACE_MINECART, CompMaterial.BARREL, MinecraftVersion.V.v1_14),
    BLAST_FURNACE(CompMaterial.GOLD_ORE, CompMaterial.BLAST_FURNACE, MinecraftVersion.V.v1_14),
    LECTERN(CompMaterial.OAK_SIGN, CompMaterial.LECTERN, MinecraftVersion.V.v1_14),
    SMOKER(CompMaterial.COOKED_BEEF, CompMaterial.SMOKER, MinecraftVersion.V.v1_14),
    LOOM(CompMaterial.BROWN_BANNER, CompMaterial.LOOM, MinecraftVersion.V.v1_14),
    CARTOGRAPHY(CompMaterial.MAP, CompMaterial.CARTOGRAPHY_TABLE, MinecraftVersion.V.v1_14),
    GRINDSTONE(CompMaterial.DIAMOND_PICKAXE, CompMaterial.GRINDSTONE, MinecraftVersion.V.v1_14),
    STONECUTTER(CompMaterial.COBBLESTONE_WALL, CompMaterial.STONECUTTER, MinecraftVersion.V.v1_14),
    COMPOSTER(CompMaterial.BONE_MEAL, CompMaterial.COMPOSTER, MinecraftVersion.V.v1_14),
    PLACE(CompMaterial.OAK_PLANKS),
    DROP(CompMaterial.CACTUS),
    ITEM_FRAME(CompMaterial.ITEM_FRAME),
    DIE(CompMaterial.SKELETON_SKULL);

    @Getter
    final CompMaterial legacy;
    @Getter
    final CompMaterial modern;
    @Getter
    final MinecraftVersion.V version;
    String displayName;

    MoveOption(CompMaterial legacy) {
        this(legacy, legacy, MinecraftVersion.getCurrent());
    }

    MoveOption(CompMaterial legacy, String displayName) {
        this(legacy, legacy, MinecraftVersion.getCurrent(), displayName);
    }

    /**
     * Get the string list of all available MoveOptions.
     */
    public static List<String> getStringList(){
        return Arrays.stream(values()).map(Enum::toString).collect(Collectors.toList());
    }

    public void addTo(Group group){
        group.getOptions().add(this);
    }

    public void removeFrom(Group group){
        group.getOptions().remove(this);
    }

    @NotNull
    public CompMaterial getMaterial(){
        if (this.modern != this.legacy){
            if (MinecraftVersion.atLeast(this.version)){
                return this.modern;
            }
        }
        return legacy;
    }

    @NotNull
    public String getDisplayName(){
        if (this.displayName != null) return this.displayName;
        return Util.capitalizeString(toString());
    }
}
