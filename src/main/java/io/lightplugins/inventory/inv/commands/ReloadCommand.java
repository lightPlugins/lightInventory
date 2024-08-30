package io.lightplugins.inventory.inv.commands;

import io.lightplugins.inventory.inv.LightInv;
import io.lightplugins.inventory.util.SubCommand;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReloadCommand extends SubCommand {
    @Override
    public List<String> getName() {
        return List.of("reload");
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin configurations";
    }

    @Override
    public String getSyntax() {
        return "/li reload";
    }

    @Override
    public int maxArgs() {
        return 1;
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public TabCompleter registerTabCompleter() {
        return null;
    }

    @Override
    public boolean performAsPlayer(Player player, String[] args) throws ExecutionException, InterruptedException {
        LightInv.instance.reload();
        return false;
    }

    @Override
    public boolean performAsConsole(ConsoleCommandSender sender, String[] args) throws ExecutionException, InterruptedException {
        return false;
    }
}
