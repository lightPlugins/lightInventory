package io.lightplugins.inventory.inv;

import io.lightplugins.inventory.LightMaster;
import io.lightplugins.inventory.inv.commands.DummyCommand;
import io.lightplugins.inventory.inv.commands.ReloadCommand;
import io.lightplugins.inventory.inv.config.MessageParams;
import io.lightplugins.inventory.inv.config.SettingParams;
import io.lightplugins.inventory.inv.manager.InventoryManager;
import io.lightplugins.inventory.inv.manager.QueryManager;
import io.lightplugins.inventory.util.SubCommand;
import io.lightplugins.inventory.util.dependencies.HeadDatabase;
import io.lightplugins.inventory.util.interfaces.LightModule;
import io.lightplugins.inventory.util.manager.CommandManager;
import io.lightplugins.inventory.util.manager.FileManager;
import io.lightplugins.inventory.util.manager.MultiFileManager;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LightInv implements LightModule {

    public static LightInv instance;
    public boolean isModuleEnabled = false;
    public Economy economy = null;
    private net.milkbowl.vault.economy.Economy vaultProvider;
    public static Economy economyVaultService;

    public final String moduleName = "inventory";
    public final String adminPerm = "light." + moduleName + ".admin";
    public final static String tablePrefix = "lighteco_";
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    @Getter
    private QueryManager queryManager;
    @Getter
    private SettingParams settingParams;
    @Getter
    private static MessageParams messageParams;
    @Getter
    private MultiFileManager multiFileManager;
    @Getter
    private List<File> inventoriesFiles = new ArrayList<>();
    @Getter
    private FileManager settings;
    @Getter
    private FileManager language;
    @Getter
    private InventoryManager inventoryManager;

    @Getter
    @Setter
    public boolean headDatabaseReady = false;

    @Override
    public void enable() {
        LightMaster.getDebugPrinting().print(
                "Starting the core module " + this.moduleName);
        instance = this;
        LightMaster.getDebugPrinting().print(
                "Creating default files for core module " + this.moduleName);
        initFiles();
        this.settingParams = new SettingParams(this);
        LightMaster.getDebugPrinting().print(
                "Selecting module language for core module " + this.moduleName);
        selectLanguage();
        messageParams = new MessageParams(language);
        LightMaster.getDebugPrinting().print(
                "Registering subcommands for core module " + this.moduleName + "...");
        initSubCommands();
        this.isModuleEnabled = true;
        LightMaster.getDebugPrinting().print(
                "Successfully started core module " + this.moduleName + "!");

        // Register all Listener classes
        registerEvents();

        // Vault setup
        RegisteredServiceProvider<Economy> vaultRSP =
                Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

        if(vaultRSP == null) {
            LightMaster.getDebugPrinting().print("§4Failed to initialize start sequence while enabling module §c" + this.moduleName);
            LightMaster.getDebugPrinting().print("Could not find a RegisteredServiceProvider for Vault Economy");
            disable();
            return;
        }

        economyVaultService = vaultRSP.getProvider();

    }

    @Override
    public void disable() {
        this.isModuleEnabled = false;
        LightMaster.getDebugPrinting().print("Disabled module " + this.moduleName);
    }

    @Override
    public void reload() {
        initFiles(); // testing
        getSettings().reloadConfig(moduleName + "/settings.yml");
        LightMaster.getDebugPrinting().print(moduleName + "/settings.yml");
        selectLanguage();
        LightMaster.getDebugPrinting().print(moduleName + "/language/" + settingParams.getModuleLanguage() + ".yml");
        getLanguage().reloadConfig(moduleName + "/language/" + settingParams.getModuleLanguage() + ".yml");
    }

    @Override
    public boolean isEnabled() {
        return this.isModuleEnabled;
    }

    @Override
    public String getName() {
        return moduleName;
    }

    private void selectLanguage() {
        this.language = LightMaster.instance.selectLanguage(settingParams.getModuleLanguage(), moduleName);
    }

    private void initFiles() {
        this.settings = new FileManager(
                LightMaster.instance, moduleName + "/settings.yml", true);

        // creating default example inventory file
        new FileManager(LightMaster.instance, moduleName + "/inventories/_example.yml", true);

        // check for all custom inventory files on server startup
        try {
            this.multiFileManager = new MultiFileManager(
                     "plugins/lightInventory/" + moduleName + "/inventories/");
            inventoriesFiles = multiFileManager.getFiles();

            if(!inventoriesFiles.isEmpty()) {
                inventoriesFiles.forEach(file -> {
                    LightMaster.getDebugPrinting().print("Loaded inventory file: " + file.getName());
                });
            }

            // load all inventory files to memory
            this.inventoryManager = new InventoryManager(inventoriesFiles);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load inventory files", e);
        }
    }

    private void initSubCommands() {
        PluginCommand ecoCommand = Bukkit.getPluginCommand("li");
        subCommands.add(new DummyCommand());
        subCommands.add(new ReloadCommand());
        new CommandManager(ecoCommand, subCommands);

    }

    public void registerEvents() {
        // check if HeadDatabase is installed on the server
        if(Bukkit.getPluginManager().getPlugin("HeadDatabase") != null) {
            Bukkit.getPluginManager().registerEvents(new HeadDatabase(), LightMaster.instance);
        } else {
            LightMaster.getDebugPrinting().print("HeadDatabase not found. Disable HeadDB features.");
        }
    }

    // lightInventory currently does not need a database
    private boolean initDatabase() {
        this.queryManager = new QueryManager(LightMaster.instance.getConnection());
        queryManager.createEcoTable();
        return true;
    }
}
