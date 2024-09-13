package io.lightplugins.inventory.module.constructor;


import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PatternPane;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import io.lightplugins.inventory.util.handler.ClickItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

public class InvCreator {

    private final ChestGui gui = new ChestGui(6, "Init");
    private final InvConstructor invConstructor;
    private final Player player;
    private BukkitTask bukkitTask;

    public InvCreator(InvConstructor invConstructor, Player player) {
        this.invConstructor = invConstructor;
        this.player = player;
    }

    public void openInventory() {

        gui.setRows(invConstructor.getRows());
        gui.setTitle(invConstructor.getGuiTitle());
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        String[] patternList = invConstructor.getPattern().toArray(new String[0]);

        Pattern pattern = new Pattern(patternList);
        PatternPane patternPane = new PatternPane(0, 0, 9, invConstructor.getRows(), pattern);

        for(String patternIdentifier : invConstructor.getClickItemHandlersSection().getKeys(false)) {

            ClickItemHandler clickItemHandler = new ClickItemHandler(
                    Objects.requireNonNull(invConstructor.getClickItemHandlersSection().getConfigurationSection(
                            patternIdentifier)), player);

            ItemStack itemStack = clickItemHandler.getGuiItem();

            patternPane.bindItem(patternIdentifier.charAt(0), new GuiItem(itemStack, inventoryClickEvent -> {

            }));
        }

        gui.setOnClose(event -> {
            if(bukkitTask != null) {
                bukkitTask.cancel();
            }
        });

        gui.addPane(patternPane);
        gui.show(player);
    }
}
