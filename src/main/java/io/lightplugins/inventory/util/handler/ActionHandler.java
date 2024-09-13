package io.lightplugins.inventory.util.handler;


import io.lightplugins.inventory.LightMaster;
import io.lightplugins.inventory.module.LightInv;
import io.lightplugins.inventory.module.constructor.InvCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ActionHandler {

    public Player player;
    public String actionData;
    public String[] actionDataArray;

    public ActionHandler(Player player, String actionData) {
        this.player = player;
        this.actionData = actionData;
        this.actionDataArray = actionData.split(";");
    }

    public void handleAction() {

        if(actionDataArray == null) {
            LightMaster.getDebugPrinting().print("Config error: No action data found.");
            return;
        }

        switch (actionDataArray[0]) {
            case "message":
                sendMessage();
                break;
            case "title":
                sendTitle();
                break;
            case "sound":
                sendSound();
                break;
            case "inventory":
                openInventory();
                break;
            case "player-command":
                executePlayerCommand();
                break;
            case "console-command":
                executeConsoleCommand();
                break;
            case "close-inventory":
                closeInventory();
                break;
        }
    }

    private void sendMessage() {

        if(actionDataArray[1] == null) {
            player.sendMessage("Config error: No message to send.");
            return;
        }

        LightMaster.getMessageSender().sendPlayerMessage(actionDataArray[1], player);
    }

    private void sendTitle() {

        if(actionDataArray.length < 5) {
            player.sendMessage("Config error: Not enough arguments for title.");
            return;
        }

        LightMaster.getMessageSender().sendPlayerTitle(
                actionDataArray[1],
                actionDataArray[2],
                Integer.parseInt(actionDataArray[3]),
                Integer.parseInt(actionDataArray[4]),
                Integer.parseInt(actionDataArray[5]),
                player);

    }

    private void sendSound() {
        if(actionDataArray.length < 4) {
            player.sendMessage("Config error: Not enough arguments for sound.");
            return;
        }

        LightMaster.getMessageSender().sendSound(
                actionDataArray[1],
                Float.parseFloat(actionDataArray[2]),
                Float.parseFloat(actionDataArray[3]),
                player);
    }

    private void openInventory() {
        if(actionDataArray.length < 2) {
            player.sendMessage("Config error: Not enough arguments for inventory.");
            return;
        }
        InvCreator invCreator = LightInv.instance.getInventoryManager().generateInventory(actionDataArray[1], player);

        if(invCreator == null) {
            player.sendMessage("Config error: Inventory name not found.");
            return;
        }

        invCreator.openInventory();
    }

    private void executePlayerCommand() {
        if(actionDataArray.length < 2) {
            player.sendMessage("Config error: Not enough arguments for player command.");
            return;
        }

        Bukkit.getScheduler().runTask(LightMaster.instance, () -> {
            if(player.performCommand(actionDataArray[1])) {
                player.sendMessage(ChatColor.GREEN + "Player Command executed successfully.");
            } else {
                player.sendMessage(ChatColor.RED + "Player Command failed to execute.");
            }
        });
    }

    private void executeConsoleCommand() {
        if(actionDataArray.length < 2) {
            player.sendMessage("Config error: Not enough arguments for console command.");
            return;
        }

        Bukkit.getScheduler().runTask(LightMaster.instance, () -> {
            if (Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), actionDataArray[1])) {
                player.sendMessage(ChatColor.GREEN + "Console Command executed successfully.");
            } else {
                player.sendMessage(ChatColor.RED + "Console Command failed to execute.");
            }
        });
    }

    private void closeInventory() {
        player.closeInventory();
    }
}