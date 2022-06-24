package me.rubix327.antiitemmove.storage;

import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an option that can be passed as an element of each group in {@link Group}.
 * This may be {@link Group} itself (group referring on other groups) or {@link MoveOption}.
 */
public interface IOption {

    /**
     * Add this option to the specified group.
     * @param group the group
     */
    void addTo(Group group);

    /**
     * Remove this option from the specified group.
     * @param group the group
     */
    void removeFrom(Group group);

    /**
     * Get the display material of this IOption.
     * @return CompMaterial
     */
    CompMaterial getMaterial();

    /**
     * Get the instance of Group or MoveOption by its name.<br>
     * If the element with that name does not exist, throws IllegalArgumentException
     * @param name the name
     * @return Group or MoveOption instance
     * @throws IllegalArgumentException if the name is incorrect
     */
    static IOption valueOf(String name) throws IllegalArgumentException{
        try{
            return Group.valueOf(name);
        }
        catch (IllegalArgumentException e){
            try {
                return MoveOption.valueOf(name);
            }
            catch (IllegalArgumentException e1){
                throw new IllegalArgumentException("Element " + name + " was tried to be removed but it is not a MoveOption or a Group.");
            }
        }
    }

    /**
     * Get the option from its name (automatically uppercase).
     * If this option does not exist, returns null.
     * @param name the name
     * @return the correspondent option
     */
    static IOption getOrNull(String name){
        try{
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    /**
     * Get all the instances of {@link Group Groups} and {@link MoveOption MoveOptions}.
     */
    static List<IOption> getAll(){
        List<IOption> list = new ArrayList<>();
        list.addAll(Arrays.stream(Group.values()).collect(Collectors.toList()));
        list.addAll(Arrays.stream(MoveOption.values()).collect(Collectors.toList()));
        return list;
    }

}
