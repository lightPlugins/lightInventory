package io.lightplugins.inventory.module.manager;

import io.lightplugins.inventory.module.constructor.InvConstructor;
import io.lightplugins.inventory.module.constructor.InvCreator;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class InventoryManager {

    private final List<File> files;
    @Getter           // invName, InvConstructor
    private final HashMap<String, InvConstructor> invConstructors = new HashMap<>();

    public InventoryManager(List<File> files) {
        this.files = files;
        generateInvConstructors();
    }

    private void generateInvConstructors() {

        // clear list on reload command
        if(!getInvConstructors().isEmpty()) {
            getInvConstructors().clear();
        }

        for (File file : files) {

            FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
            InvConstructor invConstructor = new InvConstructor();

            String guiName = file.getName().replace(".yml", "");
            String title = conf.getString("gui-title");
            int row = conf.getInt("rows");
            List<String> pattern = conf.getStringList("pattern");
            ConfigurationSection clickContent = conf.getConfigurationSection("contents");

            invConstructor.setGuiName(guiName);
            invConstructor.setGuiTitle(title);
            invConstructor.setRows(row);
            invConstructor.setPattern(pattern);
            invConstructor.setClickItemHandlersSection(clickContent);
            getInvConstructors().put(guiName, invConstructor);

        }
    }

    // generate and open the inventory for the player
    // needs a player object, because PlaceholderAPI is in used.
    public InvCreator generateInventory(String guiName, Player player) {

        InvConstructor invConstructor = getInvConstructors().get(guiName);
        return new InvCreator(invConstructor, player);

    }
}
