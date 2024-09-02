package io.lightplugins.inventory.util;
import io.lightplugins.inventory.LightMaster;
import io.lightplugins.inventory.module.LightInv;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageSender {

    public void sendPlayerMessage(String message, Player player) {
        Bukkit.getScheduler().runTask(LightMaster.instance, () -> {
            Audience audience = (Audience) player;
            Component component = LightMaster.instance.colorTranslation.universalColor(LightInv.getMessageParams().prefix() + message, player);
            audience.sendMessage(component);
        });
    }
}
