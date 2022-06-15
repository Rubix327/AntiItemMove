package me.rubix327.antiitemmove.storage;

/**
 * Represents an option that can be passed as an element of each group in {@link Group}.
 * This may be {@link Group} itself (group referring on other groups) or {@link MoveOption}.
 */
public interface IOption {

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
     * Add this option to the specified group.
     * @param group the group
     */
    void add(Group group);

    /**
     * Remove this option from the specified group.
     * @param group the group
     */
    void remove(Group group);

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

}
