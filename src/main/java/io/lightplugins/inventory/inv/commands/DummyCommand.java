package io.lightplugins.inventory.inv.commands;

import io.lightplugins.inventory.LightMaster;
import io.lightplugins.inventory.inv.LightInv;
import io.lightplugins.inventory.util.NumberFormatter;
import io.lightplugins.inventory.util.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DummyCommand extends SubCommand {
    @Override
    public List<String> getName() {
        return Arrays.asList("give", "add");
    }

    @Override
    public String getDescription() {
        return "Gives the target player an amount of money.";
    }

    @Override
    public String getSyntax() {
        return "/eco [give,add] <player> <amount>";
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
                return Arrays.asList("give", "add");
            }

            if (args.length == 2) {
                List<String> offlinePlayerNames = new ArrayList<>();
                for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                    offlinePlayerNames.add(player.getName());
                }
                return offlinePlayerNames;
            }

            if(args.length == 3) {
                return List.of("<amount>");
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

        BigDecimal bg = NumberFormatter.parseMoney(args[2]);

        if(bg == null) {
            LightMaster.getMessageSender().sendPlayerMessage(LightInv.getMessageParams().noNumber(), player);
            return false;
        }

        if(!NumberFormatter.isPositiveNumber(bg.doubleValue())) {
            LightMaster.getMessageSender().sendPlayerMessage(LightInv.getMessageParams().onlyPositive(), player);
            return false;
        }

        LightInv.economyVaultyService.depositPlayerAsync(target.getUniqueId(), bg)
                .thenAcceptAsync(depositResult -> {
                    if(depositResult.transactionSuccess()) {
                        LightMaster.getMessageSender().sendPlayerMessage(LightInv.getMessageParams().depositSuccess()
                                .replace("#player#", target.getName())
                                .replace("#amount#", NumberFormatter.formatForMessages(bg)), player);
                        return;
                    }
                    LightMaster.getMessageSender().sendPlayerMessage(LightInv.getMessageParams().depositFailed()
                            .replace("#player#", target.getName())
                            .replace("#reason#", depositResult.errorMessage())
                            .replace("#amount#", NumberFormatter.formatForMessages(bg)), player);
                });

        return false;
    }

    @Override
    public boolean performAsConsole(ConsoleCommandSender sender, String[] args) throws ExecutionException, InterruptedException {
        return false;
    }
}
