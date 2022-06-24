package me.rubix327.antiitemmove.storage;

import com.google.common.annotations.Beta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents groups to organize {@link MoveOption MoveOptions}.
 * Inside a group there may be not only MoveOption but another {@link Group},
 * referring to the items that it does contain.<br>
 * Groups recursion is not allowed - repeated groups will be removed.
 */
@AllArgsConstructor
public enum Group implements IOption {
    ALL(Arrays.stream(MoveOption.values()).collect(Collectors.toList()), CompMaterial.DIAMOND_BLOCK, 10),
    CONTAINERS(toList(MoveOption.CHEST, MoveOption.ENDER_CHEST, MoveOption.SHULKER_BOX, MoveOption.BARREL), CompMaterial.CHEST_MINECART, 1),
    TOOL_BLOCKS(toList(MoveOption.DISPENSER, MoveOption.DROPPER, MoveOption.FURNACE, MoveOption.WORKBENCH, MoveOption.ENCHANTING,
            MoveOption.BREWING, MoveOption.ANVIL, MoveOption.SMITHING, MoveOption.BEACON, MoveOption.HOPPER,
            MoveOption.BLAST_FURNACE, MoveOption.LECTERN, MoveOption.SMOKER, MoveOption.LOOM, MoveOption.CARTOGRAPHY,
            MoveOption.GRINDSTONE, MoveOption.STONECUTTER, MoveOption.COMPOSTER), CompMaterial.CRAFTING_TABLE, 2),
    SAFE(toList(MoveOption.WORKBENCH, MoveOption.MERCHANT, MoveOption.ENDER_CHEST, MoveOption.CRAFTING, MoveOption.PLAYER,
            MoveOption.CREATIVE, MoveOption.ENCHANTING), CompMaterial.FIREWORK_STAR, 3),
    NOT_SAFE(getNotSafe(), CompMaterial.FIRE_CHARGE, 4),
    OTHER(toList(MoveOption.PLACE, MoveOption.DROP, MoveOption.ITEM_FRAME, MoveOption.DIE), CompMaterial.BLAZE_POWDER, 5),
    DEFAULT(new ArrayList<>(), toList(Group.NOT_SAFE), CompMaterial.WHITE_WOOL, 6),
    CUSTOM_1(toList(MoveOption.DISPENSER, MoveOption.DROPPER, MoveOption.FURNACE), toList(Group.DEFAULT), CompMaterial.LIGHT_BLUE_WOOL, 7),
    CUSTOM_2(toList(MoveOption.LOOM, MoveOption.BREWING, MoveOption.ENCHANTING), toList(Group.CUSTOM_1), CompMaterial.MAGENTA_WOOL, 8),
    CUSTOM_3(toList(MoveOption.MERCHANT, MoveOption.ANVIL, MoveOption.BARREL), toList(Group.CUSTOM_2), CompMaterial.YELLOW_WOOL, 9);

    @Getter @Setter
    private List<MoveOption> options;
    @Getter @Setter
    private List<Group> groups;
    @Getter
    private final List<MoveOption> defaultOptions;
    @Getter
    private final List<Group> defaultGroups;
    @Getter
    private final CompMaterial material;
    @Getter
    private final int priority;

    Group(List<MoveOption> options, CompMaterial material, int priority){
        this(options, new ArrayList<>(), options, new ArrayList<>(), material, priority);
    }

    Group(List<MoveOption> options, List<Group> groups, CompMaterial material, int priority){
        this(options, groups, options, groups, material, priority);
    }

    /**
     * Get the string list containing all available MoveOptions.
     * @Beta Unstable - works differently depending on the situtation.
     */
    @Beta
    public List<String> getOptionsStringList(){
        List<String> list = new ArrayList<>();
        for (MoveOption option : getOptions()){
            list.add(option.toString());
        }
        return list;
    }

    /**
     * Transform MoveOption[] array to List(MoveOption) list.
     */
    public static List<MoveOption> toList(MoveOption... array){
        return new ArrayList<>(Arrays.asList(array));
    }

    /**
     * Transform Group[] array to List(Group) list.
     */
    public static List<Group> toList(Group... array){
        return new ArrayList<>(Arrays.asList(array));
    }

    private static List<MoveOption> getNotSafe(){
        List<MoveOption> first = ALL.options;
        List<MoveOption> second = SAFE.options;
        return first.stream().filter(e -> !second.contains(e)).collect(Collectors.toList());

    }

    public static List<Group> getList(){
        return Arrays.stream(values()).collect(Collectors.toList());
    }

    /**
     * Get the string list containing all available groups.
     */
    public static List<String> getStringList(){
        return Arrays.stream(values()).map(Enum::toString).collect(Collectors.toList());
    }

    /**
     * Get the Group list of user-defined groups.
     */
    public static List<Group> getUserDefined(){
        return Arrays.asList(DEFAULT, CUSTOM_1, CUSTOM_2, CUSTOM_3);
    }

    /**
     * Get the Group list of predefined groups.
     */
    public static List<Group> getPredefined(){
        return Arrays.asList(ALL, CONTAINERS, TOOL_BLOCKS, SAFE, NOT_SAFE, OTHER);
    }

    /**
     * Get the string list of user defined groups.
     */
    public static List<String> getUserDefinedString(){
        return Stream.of(DEFAULT, CUSTOM_1, CUSTOM_2, CUSTOM_3).map(Enum::toString).collect(Collectors.toList());
    }

    /**
     * Get the user-defined groups and their IOptions.
     */
    public static HashMap<Group, List<String>> getUserAll(){
        HashMap<Group, List<String>> map = new HashMap<>();
        for (Group group : getUserDefined()){
            map.put(group, group.getIOptionsStringList());
        }
        return map;
    }

    public List<IOption> getIOptions(){
        List<IOption> list = new ArrayList<>();
        list.addAll(this.getGroups());
        list.addAll(this.getOptions());
        return list;
    }

    /**
     * Get the combined list of IOptions inside this group.
     * @return options and groups
     */
    public List<String> getIOptionsStringList(){
        return getIOptions().stream().map(IOption::toString).collect(Collectors.toList());
//        List<String> list = new ArrayList<>();
//        list.addAll(getGroups().stream().map(Enum::toString).collect(Collectors.toList()));
//        list.addAll(getOptions().stream().map(Enum::toString).collect(Collectors.toList()));
//        return list;
    }

    /**
     * Get the display names of all the options and groups bound to this group.
     * @return the names
     */
    public List<String> getAllDisplayNames(){
        List<String> list = new ArrayList<>();
        for (String s : getIOptionsStringList()){
            IOption option = IOption.getOrNull(s);
            if (option instanceof MoveOption){
                list.add(((MoveOption) option).getDisplayName());
            }
            else{
                list.add(s);
            }
        }
        return list;
    }

    @Nullable
    public static Group getOrNull(String name){
        try {
            return Group.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    /**
     * Add this group to the given one.
     * @param group the given group
     */
    public void addTo(Group group){
        group.getGroups().add(this);
    }

    /**
     * Remove this group from the given one.
     * @param group the given group
     */
    public void removeFrom(Group group){
        group.getGroups().remove(this);
    }

    /**
     * Reset this group's IOptions to the default values.
     */
    public void reset(){
        this.setGroups(this.getDefaultGroups());
        this.setOptions(this.getDefaultOptions());
    }
}
