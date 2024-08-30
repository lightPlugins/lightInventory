package io.lightplugins.inventory.util.dependencies;

import io.lightplugins.inventory.inv.LightInv;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HeadDatabase implements Listener {

    @EventHandler
    public void onHeadDatabaseSuccess(DatabaseLoadEvent event) {
        LightInv.instance.setHeadDatabaseReady(true);
    }
}
