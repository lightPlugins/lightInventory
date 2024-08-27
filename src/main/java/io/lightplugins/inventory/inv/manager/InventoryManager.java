package io.lightplugins.inventory.inv.manager;

import io.lightplugins.inventory.inv.constructor.InvConstructor;
import io.lightplugins.inventory.inv.constructor.InvCreator;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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

    // generate and open the inventor for the player
    // needs a player object, because PlaceholderAPI is used.
    private void generateInventory(String guiName, Player player) {

        InvConstructor invConstructor = getInvConstructors().get(guiName);
        InvCreator invCreator = new InvCreator(invConstructor, player);
        invCreator.openInventory();

    }

}
