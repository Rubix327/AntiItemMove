package me.rubix327.antiitemmove.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rubix327.antiitemmove.Settings;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Logger;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BansStorage extends YamlConfig {

    @Getter
    private static final BansStorage instance = new BansStorage();

    /**
     * Contains all banned MoveOptions and Groups of an item.
     */
    private final LinkedHashMap<Integer, List<IOption>> bans = new LinkedHashMap<>();
    /**
     * Contains only banned MoveOptions of an item. Groups are already replaced with MoveOptions here.
     */
    @Getter
    private final HashMap<ItemStack, List<IOption>> cachedBans = new HashMap<>();

    public void init(){
        setHeader(Settings.BANS_DESCRIPTION);
        this.loadConfiguration(NO_DEFAULT, "bans.yml");
    }

    /**
    Get data from bans.yml and replace Default group to its {@link IOption IOptions}.
    */
    @Override
    protected void onLoad() {
        /*
        Get data from bans.yml
         */
        LinkedHashMap<Integer, List<IOption>> map = new LinkedHashMap<>();
        for (String key : getKeys(true)){
            String name = key.replace("Bans.", "").replace("Bans", "");
            if (name.isEmpty()) continue;

            int id = Integer.parseInt(name);
            List<IOption> elements = getStringList(key).stream().map(IOption::getOrNull).collect(Collectors.toList());
            map.put(id, elements);
        }
        this.bans.putAll(map);

        /*
        If elements contains Default group then replace it with its options and groups.
         */
        for (List<IOption> list : bans.values()){
            if (list.contains(Group.DEFAULT)){
                list.remove(Group.DEFAULT);
                list.addAll(Group.DEFAULT.getIOptions());
            }
        }

        /*
        Update cached data and save to the file
         */
        updateCachedBans();
        save();
    }

    /**
     * Fully refresh the {@link #cachedBans}.
     */
    private void updateCachedBans(){
        for (Map.Entry<Integer, List<IOption>> entry : bans.entrySet()){
            ItemStack item = ItemsStorage.getInstance().getItem(entry.getKey());
            if (item == null) continue;
            updateCachedBansFor(item);
        }
    }

    /**
     * Refresh the {@link #cachedBans} for this particular item.
     * @param item the item
     */
    public void updateCachedBansFor(ItemStack item){
        cachedBans.put(item, getReplacedBans(item));
    }

    /**
     * Save data to the file
     */
    @Override
    protected void onSave() {
        this.set("Bans", this.bans);
    }

    /**
     * Add new {@link IOption IOptions} to the item with the given id
     * @param id the id
     * @param options the IOptions to add
     */
    public void addOptions(int id, List<IOption> options){
        if (this.bans.containsKey(id)){
            this.bans.get(id).addAll(options);

        }
        else{
            this.bans.put(id, options);
        }
        ItemStack item = ItemsStorage.getInstance().getItem(id);
        updateCachedBansFor(item);
        this.save();
    }

    /**
     * Add new {@link IOption IOptions} to the item with the given id
     * @param id the id
     * @param options the IOptions to add
     */
    public void addOptions(int id, IOption... options){
        addOptions(id, Arrays.stream(options).collect(Collectors.toList()));
    }

    /**
     * Remove the given {@link IOption IOptions} from the item with the given id
     * @param id the id
     * @param options the IOptions to remove
     */
    public void removeOptions(int id, IOption... options){
        List<IOption> invs = Arrays.stream(options).collect(Collectors.toList());
        if (this.bans.containsKey(id)){
            this.bans.get(id).removeAll(invs);
        }
        this.save();
    }

    /**
     * Remove the item from the bans list.
     * Consider removing it from {@link ItemsStorage} too.
     * @param id the id of the item
     */
    public void remove(int id){
        this.bans.remove(id);
        updateCachedBans();
        this.save();
    }

    /**
     * Get the raw bans of the item with the given id.<br>
     * May contain {@link MoveOption MoveOptions} as well as {@link Group Groups} names.
     * @param id the id
     * @return the IOptions of the item
     */
    @Nullable
    public List<IOption> getBans(int id){
        return bans.get(id);
    }

    /**
     * Get the cleaned list of the item bans.<br>
     * May only contain {@link MoveOption MoveOptions} names.
     * @param item the item
     * @return the MoveOptions
     * @throws NullPointerException if the given item is not restricted or has no bans associated.
     */
    private List<IOption> getReplacedBans(ItemStack item) throws NullPointerException {
        Integer itemKey = ItemsStorage.getInstance().getKey(item);
        if (itemKey == null) throw new NullPointerException("This item is not restricted.");

        List<IOption> bans = BansStorage.getInstance().getBans(itemKey);
        if (bans == null) throw new NullPointerException("This item has no bans associated.");
        if (bans.isEmpty()) return new ArrayList<>();
        bans = new ArrayList<>(replaceToOptions(new HashSet<>(bans)));

        return bans;
    }

    /**
     * Replace groups with their corresponding options.<br>
     * Example: CONTAINERS -> "CHEST", "ENDER_CHEST", "SHULKER_BOX", "BARREL".<br>
     * Values cannot be repeated.
     */
    private HashSet<IOption> replaceToOptions(HashSet<IOption> bannedOptions){
        for (Group group : Group.getUserDefined()){
            if (!bannedOptions.contains(group)) continue;
            for (String element : group.getIOptionsStringList()){
                if (Group.getUserDefinedString().contains(element)) {
                    if (Group.valueOf(element).getIOptionsStringList().contains(group.toString())){
                        Logger.warning("An error occurred when moving an item. Groups " + group + " and " + element
                                + " are referring to each other. Key " + element + " in group " + group + " has been removed.");
                        GroupsStorage.getInstance().removeElement(group, IOption.valueOf(element));
                        return new HashSet<>(Collections.singletonList(Group.ALL));
                    }
                }
            }
        }

        HashSet<IOption> set = new HashSet<>(bannedOptions);
        for (IOption current : bannedOptions){
            if (Group.ALL.equals(current)) continue;
            for (Group predefined : Group.getList()){
                if (predefined.equals(current)){
                    set.remove(predefined);
                    set.addAll(predefined.getIOptions());
                    set = replaceToOptions(set);
                }
            }
        }
        return set;
    }

}
