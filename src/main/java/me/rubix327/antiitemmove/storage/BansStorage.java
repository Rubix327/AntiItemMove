package me.rubix327.antiitemmove.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rubix327.antiitemmove.Settings;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Logger;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.LinkedHashMap;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BansStorage extends YamlConfig {

    @Getter
    private static final BansStorage instance = new BansStorage();

    private LinkedHashMap<Integer, List> bans = new LinkedHashMap<>();

    public void init(){
        setHeader(Settings.BANS_DESCRIPTION);
        this.loadConfiguration(NO_DEFAULT, "bans.yml");
    }

    @Override
    protected void onLoad() {
        this.bans = getMap("Bans", Integer.class, List.class, null);
        Logger.info(bans.toString());
    }

    @Override
    protected void onSave() {
        this.set("Bans", this.bans);
    }

    public void add(int id, List<String> inventories){
        this.bans.put(id, inventories);
        this.save();
    }

    public void remove(int id){
        this.bans.remove(id);
        this.save();
    }

    @Nullable
    public List<String> get(int key){
        return bans.get(key);
    }

}
