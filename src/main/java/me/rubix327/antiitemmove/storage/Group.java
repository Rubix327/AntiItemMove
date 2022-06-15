package me.rubix327.antiitemmove.storage;

import com.google.common.annotations.Beta;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Logger;

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
public enum Group implements IOption {
    ALL(Arrays.stream(MoveOption.values()).collect(Collectors.toList())),
    CONTAINERS(toList(MoveOption.CHEST, MoveOption.ENDER_CHEST, MoveOption.SHULKER_BOX, MoveOption.BARREL)),
    TOOLS(toList(MoveOption.DISPENSER, MoveOption.DROPPER, MoveOption.FURNACE, MoveOption.WORKBENCH, MoveOption.ENCHANTING,
            MoveOption.BREWING, MoveOption.ANVIL, MoveOption.SMITHING, MoveOption.BEACON, MoveOption.HOPPER,
            MoveOption.BLAST_FURNACE, MoveOption.LECTERN, MoveOption.SMOKER, MoveOption.LOOM, MoveOption.CARTOGRAPHY,
            MoveOption.GRINDSTONE, MoveOption.STONECUTTER, MoveOption.COMPOSTER)),
    SAFE(toList(MoveOption.WORKBENCH, MoveOption.MERCHANT, MoveOption.ENDER_CHEST, MoveOption.CRAFTING, MoveOption.PLAYER,
            MoveOption.CREATIVE, MoveOption.ENCHANTING)),
    NOT_SAFE(getNotSafe()),
    OTHER(toList(MoveOption.PLACE, MoveOption.DROP, MoveOption.DIE)),
    DEFAULT(new ArrayList<>(), toList(Group.NOT_SAFE)),
    CUSTOM_1(toList(MoveOption.DISPENSER, MoveOption.DROPPER, MoveOption.FURNACE), toList(Group.DEFAULT)),
    CUSTOM_2(toList(MoveOption.LOOM, MoveOption.BREWING, MoveOption.ENCHANTING), toList(Group.CUSTOM_1)),
    CUSTOM_3(toList(MoveOption.MERCHANT, MoveOption.ANVIL, MoveOption.BARREL), toList(Group.CUSTOM_2));

    @Getter @Setter
    private List<MoveOption> options;
    @Getter @Setter
    private List<Group> groups;

    Group(List<MoveOption> options){
        this.options = options;
        this.groups = new ArrayList<>();
    }

    Group(List<MoveOption> options, List<Group> groups){
        this.options = options;
        this.groups = groups;
        getOptionsStringList();
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
        return Arrays.asList(ALL, CONTAINERS, TOOLS, SAFE, NOT_SAFE, OTHER);
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
            map.put(group, group.getOptionsAndGroups());
        }
        return map;
    }

    /**
     * Get the combined list of IOptions inside this group.
     * @return options and groups
     */
    public List<String> getOptionsAndGroups(){
        Logger.warning(this.toString());
        List<String> list = new ArrayList<>();
        list.addAll(getGroups().stream().map(Enum::toString).collect(Collectors.toList()));
        list.addAll(getOptions().stream().map(Enum::toString).collect(Collectors.toList()));
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

    public void add(Group group){
        group.getGroups().add(this);
    }

    public void remove(Group group){
        group.getGroups().remove(this);
    }
}
