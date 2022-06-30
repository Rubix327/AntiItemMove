package me.rubix327.antiitemmove;

import lombok.Getter;
import me.rubix327.antiitemmove.storage.BansStorage;
import me.rubix327.antiitemmove.storage.GroupsStorage;
import me.rubix327.antiitemmove.storage.ItemsStorage;
import me.rubix327.itemslangapi.ItemsLangAPI;
import me.rubix327.itemslangapi.Lang;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Logger;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.SimpleSettings;

public class AntiItemMoveMain extends SimplePlugin {

    @Getter
    private static AntiItemMoveMain instance;

    @Override
    protected void onPluginStart() {
        instance = this;

        SimpleSettings.HIDE_INCOMPATIBILITY_WARNINGS = true;
        SimpleSettings.HIDE_NASHORN_WARNINGS = true;
        Common.setLogPrefix("[AntiItemMove] ");
        Common.setTellPrefix(Settings.PREFIX);
        registerCommand(new Commands());
        registerEvents(new PlayerListener());
        loadFiles();

        if (DependencyManager.ITEMS_LANG_API.isLoaded()){
            Logger.info("Hooked into ItemsLangAPI.");
            ItemsLangAPI.getApi().load(Lang.EN_US);
        }
    }

    public void loadFiles(){
        ItemsStorage.getInstance().init();
        BansStorage.getInstance().init();
        GroupsStorage.getInstance().init();
    }

    @Override
    public int getMetricsPluginId() {
        return 15634;
    }
}
