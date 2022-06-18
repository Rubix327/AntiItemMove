package me.rubix327.antiitemmove.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rubix327.antiitemmove.Settings;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemsStorage extends YamlConfig {

    @Getter
    private static final ItemsStorage instance = new ItemsStorage();

    @Getter
    private LinkedHashMap<Integer, ItemStack> itemsMap = new LinkedHashMap<>();

    public void init(){
        setHeader(Settings.ITEMS_DESCRIPTION);
        this.loadConfiguration(NO_DEFAULT, "items.yml");
    }

    /**
     * Get data from items.yml
     */
    @Override
    protected void onLoad() {
        itemsMap = getMap("Items", Integer.class, ItemStack.class);
    }

    /**
     * Save data to the file
     */
    @Override
    protected void onSave() {
        this.set("Items", this.itemsMap);
    }

    /**
     * Get the current maximum id of the items map.
     */
    public int getMaxId(){
        return this.itemsMap.keySet().stream().max(Integer::compareTo).orElse(0);
    }

    /**
     * Add a new item to the map and save it to the file.
     * @param item the item
     */
    public void add(ItemStack item){
        this.itemsMap.put(getMaxId() + 1, item);
        this.save();
    }

    /**
     * Remove an item by its id from the map and from the file.
     * @param id the id of the item
     */
    public void remove(int id){
        this.itemsMap.remove(id);
        this.save();
    }

    /**
     * Get the id of given item. If it is not restricted yet, it returns null.
     * @param item the item
     * @return the id
     */
    @Nullable
    public Integer getKey(ItemStack item) {
        for (Map.Entry<Integer, ItemStack> entry : itemsMap.entrySet()){
            if (item.isSimilar(entry.getValue())) return entry.getKey();
        }
        return null;
    }

    /**
     * Get the item by its id.
     * @param id the id of the item
     * @return the item
     */
    @Nullable
    public ItemStack getItem(Integer id){
        return this.itemsMap.get(id);
    }
}
