package me.rubix327.antiitemmove.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rubix327.antiitemmove.Settings;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupsStorage extends YamlConfig {

    @Getter
    private static final GroupsStorage instance = new GroupsStorage();

    public void init(){
        setHeader(Settings.GROUPS_DESCRIPTION);
        this.loadConfiguration(NO_DEFAULT, "groups.yml");
        save();
    }

    /**
     * Get data from groups.yml
     */
    @Override
    protected void onLoad() {
        HashMap<Group, List<Group>> groups = new HashMap<>();
        HashMap<Group, List<MoveOption>> options = new HashMap<>();

        /*
        Loop through all the Groups keys
         */
        for (String key : getKeys(true)){
            String name = key.replace("Groups.", "").replace("Groups", "");
            if (name.isEmpty()) continue;

            /*
             Get the current group key and its elements
             */
            Group group = Group.valueOf(name);
            List<String> elements = getStringList(key);

            /*
            If this element is a group then add it to groups map
            If this element is an option then add it to options map
             */
            groups.put(group, new ArrayList<>());
            options.put(group, new ArrayList<>());
            for (String object : elements){
                if (Group.getStringList().contains(object)){
                    groups.get(group).add(Group.valueOf(object));
                }
                else if (MoveOption.getStringList().contains(object)){
                    options.get(group).add(MoveOption.valueOf(object));
                }
            }

            /*
             If there is at least one element in these groups then replace the enum maps.
             If there's no elements in both lists then default values will be applied to this group.
             */
            if (!groups.get(group).isEmpty() || !options.get(group).isEmpty()){
                group.setGroups(groups.get(group));
                group.setOptions(options.get(group));
            }
        }

        /*
         Save to the file
         */
        save();
    }

    /**
     * Save data to the file
     */
    @Override
    protected void onSave() {
        this.set("Groups", Group.getUserAll());
    }

    /**
     * Add an IOption (a MoveOption or a Group) to the given group.
     * @param group the group
     * @param option the IOption
     */
    public void addElement(Group group, IOption option){
        option.addTo(group);
        this.save();
    }

    /**
     * Return this group to its default values.
     */
    public void reset(Group group){
        group.reset();
        this.save();
    }

    /**
     * Make this group empty.
     */
    public void clear(Group group){
        group.setGroups(new ArrayList<>());
        group.setOptions(new ArrayList<>());
        this.save();
    }

    /**
     * Remove the element from the groups or options of this group.
     */
    public void removeElement(Group group, IOption element) throws NullPointerException{
        if (!group.getIOptionsStringList().contains(element.toString())) {
            throw new NullPointerException("This group does not contain this option.");
        }
        element.removeFrom(group);
        this.save();
    }

}
