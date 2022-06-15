package me.rubix327.antiitemmove.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rubix327.antiitemmove.Settings;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.settings.YamlConfig;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemsStorage extends YamlConfig {

    @Getter
    private static final ItemsStorage instance = new ItemsStorage();

    @Getter
    private LinkedHashMap<Integer, String> items = new LinkedHashMap<>();

    public void init(){
        setHeader(Settings.ITEMS_DESCRIPTION);
        this.loadConfiguration(NO_DEFAULT, "items.yml");
    }

    @Override
    protected void onLoad() {
        this.items = getMap("Items", Integer.class, String.class, null);
    }

    @Override
    protected void onSave() {
        this.set("Items", this.items);
    }

    public int getMaxId(){
        return this.items.keySet().stream().max(Integer::compareTo).orElse(0);
    }

    public void add(String item){
        this.items.put(getMaxId() + 1, item);
        this.save();
    }

    public void remove(int id){
        this.items.remove(id);
        this.save();
    }

    @Nullable
    public Integer getKey(ItemStack item) {
        try{
            for (Map.Entry<Integer, String> entry : items.entrySet()){
                if (item.isSimilar(getItemFromString(entry.getValue()))) return entry.getKey();
            }
        } catch (IOException ignored) { }
        return null;
    }

    /**
     * Encode the item to string.
     * You can then decode it with {@link #getItemFromString}
     * @param item the item
     * @return the encoded string
     */
    public static String getStringFromItem(ItemStack item){
        String itemStackString;
        ItemStack newItem = item.clone();
        newItem.setAmount(1);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(newItem);
            dataOutput.close();
            itemStackString = Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
        return itemStackString;
    }

    /**
     * Decode item from the string got in {@link #getStringFromItem}.
     * @param itemString encoded string
     * @return itemstack
     * @throws IOException if failed to decode the string
     */
    @Nullable
    public static ItemStack getItemFromString(String itemString) throws IOException {
        ItemStack itemtoreturn;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(itemString));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            itemtoreturn = (ItemStack) dataInput.readObject();
            dataInput.close();
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        } catch (IllegalArgumentException e){
            return null;
        }
        return itemtoreturn;
    }
}
