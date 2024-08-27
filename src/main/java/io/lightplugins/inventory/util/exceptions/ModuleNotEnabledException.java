package io.lightplugins.inventory.util.exceptions;

import io.lightplugins.inventory.LightMaster;
import io.lightplugins.inventory.util.interfaces.LightModule;

public class ModuleNotEnabledException extends Exception {

    public ModuleNotEnabledException(LightModule module) {
        super(LightMaster.consolePrefix + "The Module §e" + module.getName() + "§r is not enabled");
    }
}
