package io.lightplugins.inventory.inv.commands;

import io.lightplugins.inventory.LightMaster;
import io.lightplugins.inventory.inv.LightInv;
import io.lightplugins.inventory.inv.constructor.InvConstructor;
import io.lightplugins.inventory.inv.constructor.InvCreator;
import io.lightplugins.inventory.util.NumberFormatter;
import io.lightplugins.inventory.util.SkullUtil;
import io.lightplugins.inventory.util.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DummyCommand extends SubCommand {
    @Override
    public List<String> getName() {
        return Arrays.asList("open", "inv");
    }

    @Override
    public String getDescription() {
        return "Open the specified Inventory";
    }

    @Override
    public String getSyntax() {
        return "/li [open,inv] <player> <inventory>";
    }

    @Override
    public int maxArgs() {
        return 3;
    }

    @Override
    public String getPermission() {
        return "lighteconomy.eco.command.give";
    }

    @Override
    public TabCompleter registerTabCompleter() {
        return (commandSender, command, s, args) -> {

            if(!commandSender.hasPermission(getPermission())) {
                return null;
            }

            if(args.length == 1) {
                return Arrays.asList("open", "inv");
            }

            if (args.length == 2) {
                List<String> offlinePlayerNames = new ArrayList<>();
                for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                    offlinePlayerNames.add(player.getName());
                }
                return offlinePlayerNames;
            }

            if(args.length == 3) {
                List<String> invNames = new ArrayList<>();
                LightInv.instance.getInventoryManager().getInvConstructors().forEach((key, value) -> {
                    invNames.add(key);
                });
                return invNames;
            }

            return null;
        };
    }

    @Override
    public boolean performAsPlayer(Player player, String[] args) throws ExecutionException, InterruptedException {

        OfflinePlayer target = Bukkit.getPlayer(args[1]);

        if(target == null) {
            LightMaster.getMessageSender().sendPlayerMessage(LightInv.getMessageParams().playerNotFound(), player);
            return false;
        }

        if(!NumberFormatter.isNumber(args[2])) {
            if(!NumberFormatter.isShortNumber(args[2])) {
                LightMaster.getMessageSender().sendPlayerMessage(LightInv.getMessageParams().noNumber(), player);
                return false;
            }
        }

        String invName = args[2];

        InvCreator invCreator = LightInv.instance.getInventoryManager().generateInventory(invName, player);

        if(invCreator == null) {
            LightMaster.getMessageSender().sendPlayerMessage("Inventory not found", player);
            return false;
        }

        invCreator.openInventory();

        return false;
    }

    @Override
    public boolean performAsConsole(ConsoleCommandSender sender, String[] args) throws ExecutionException, InterruptedException {
        return false;
    }
}
