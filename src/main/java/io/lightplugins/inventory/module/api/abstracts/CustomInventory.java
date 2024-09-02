package io.lightplugins.inventory.module.api.abstracts;

public abstract class CustomInventory {

    public abstract void openInventory();

    public abstract void closeInventory();

    public abstract void updateInventory();

    public abstract void updateInventory(int delay);

    public abstract void updateInventory(boolean force);

    public abstract void updateInventory(int delay, boolean force);

    public abstract void updateInventory(boolean force, int delay, boolean async);
}
