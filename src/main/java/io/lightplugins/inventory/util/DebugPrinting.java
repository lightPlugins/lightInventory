package io.lightplugins.inventory.util;

import io.lightplugins.inventory.LightMaster;
import org.bukkit.Bukkit;

public class DebugPrinting {

    public void print(String message) {
        Bukkit.getConsoleSender().sendMessage(LightMaster.consolePrefix + message);
    }
}
