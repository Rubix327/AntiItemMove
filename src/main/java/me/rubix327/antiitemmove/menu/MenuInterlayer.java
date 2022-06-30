package me.rubix327.antiitemmove.menu;

import me.rubix327.antiitemmove.Settings;
import me.rubix327.antiitemmove.Util;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.button.Button;

public abstract class MenuInterlayer extends AdvancedMenu {

    public MenuInterlayer(Player player) {
        super(player);
        Common.setTellPrefix(Settings.PREFIX);
    }

    protected void showNoPermission(int slot){
        Button originalButton = this.getButtons().get(slot);
        this.getButtons().put(slot, Button.makeDummy(Util.NO_PERMISSIONS_ITEM));
        refreshMenu();
        Common.runLater(30, () -> {
            if (isViewing(getPlayer())){
                this.getButtons().put(slot, originalButton);
                refreshMenu();
            }
        });
    }
}
