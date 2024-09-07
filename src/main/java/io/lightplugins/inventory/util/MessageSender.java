package io.lightplugins.inventory.util;
import io.lightplugins.inventory.LightMaster;
import io.lightplugins.inventory.module.LightInv;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class MessageSender {

    public void sendPlayerMessage(String message, Player player) {
        Bukkit.getScheduler().runTask(LightMaster.instance, () -> {
            Audience audience = (Audience) player;
            Component component = LightMaster.instance.colorTranslation.universalColor(LightInv.getMessageParams().prefix() + message, player);
            audience.sendMessage(component);
        });
    }

    public void sendPlayerTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Player player) {
        Bukkit.getScheduler().runTask(LightMaster.instance, () -> {
            Audience audience = (Audience) player;
            Component titleComponent = LightMaster.instance.colorTranslation.universalColor(title, player);
            Component subtitleComponent = LightMaster.instance.colorTranslation.universalColor(subtitle, player);
            Duration fadeInDuration = Duration.ofMillis(fadeIn * 50L); // Convert ticks to milliseconds
            Duration stayDuration = Duration.ofMillis(stay * 50L); // Convert ticks to milliseconds
            Duration fadeOutDuration = Duration.ofMillis(fadeOut * 50L); // Convert ticks to milliseconds
            Title.Times times = Title.Times.times(fadeInDuration, stayDuration, fadeOutDuration);
            Title titleObject = Title.title(titleComponent, subtitleComponent, times);
            audience.showTitle(titleObject);
        });
    }
    public void sendSound(String sound, float volume, float pitch, Player player) {
        Bukkit.getScheduler().runTask(LightMaster.instance, () -> {
            player.playSound(player.getLocation(), sound, volume, pitch);
        });
    }

}
