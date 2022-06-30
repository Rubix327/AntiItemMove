package me.rubix327.antiitemmove.menu;

import me.rubix327.antiitemmove.Util;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.AdvancedMenuPagged;
import org.mineacademy.fo.menu.button.Button;

public abstract class MenuPaggedInterlayer<T> extends AdvancedMenuPagged<T> {
    public MenuPaggedInterlayer(Player player) {
        super(player);
    }

    protected void showNoPermission(int slot){
        Button originalButton = this.getButtons().get(slot);
        this.getButtons().put(slot, Button.makeDummy(Util.NO_PERMISSIONS_ITEM));
        refreshMenu();
        Common.runLater(30, () -> {
            if (isViewing(getPlayer())){
                if (originalButton == null){
                    this.getButtons().remove(slot);
                } else {
                    this.getButtons().put(slot, originalButton);
                }
                refreshMenu();
            }
        });
    }
}
