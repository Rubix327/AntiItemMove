package me.rubix327.antiitemmove;

import org.bukkit.Bukkit;

public enum DependencyManager {

    ITEMS_LANG_API("ItemsLangAPI");

    private final String name;
    DependencyManager(String name){
        this.name = name;
    }

    public boolean isLoaded(){
        return isPluginLoaded(this.name);
    }

    private boolean isPluginLoaded(String plugin){
        return Bukkit.getPluginManager().getPlugin(plugin) != null;
    }

}
