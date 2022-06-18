package me.rubix327.antiitemmove;

import lombok.Getter;
import me.rubix327.antiitemmove.storage.BansStorage;
import me.rubix327.antiitemmove.storage.GroupsStorage;
import me.rubix327.antiitemmove.storage.ItemsStorage;
import org.mineacademy.fo.plugin.SimplePlugin;

public class AntiItemMoveMain extends SimplePlugin {

    @Getter
    private static AntiItemMoveMain instance;

    @Override
    protected void onPluginStart() {
        instance = this;

        registerCommand(new Commands());
        registerEvents(new PlayerListener());
        loadFiles();
    }

    public void loadFiles(){
        ItemsStorage.getInstance().init();
        BansStorage.getInstance().init();
        GroupsStorage.getInstance().init();
    }
}
