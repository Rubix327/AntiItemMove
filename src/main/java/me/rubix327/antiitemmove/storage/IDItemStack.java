package me.rubix327.antiitemmove.storage;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class IDItemStack extends ItemStack {

    @Getter
    private final int id;

    public IDItemStack(int id, ItemStack item){
        super(item);
        this.id = id;
    }

}
