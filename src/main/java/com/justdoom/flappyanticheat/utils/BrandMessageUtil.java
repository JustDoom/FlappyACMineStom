package com.justdoom.flappyanticheat.utils;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import net.minestom.server.entity.Player;
import net.minestom.server.listener.PluginMessageListener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

public class BrandMessageUtil extends PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] msg) {
        try {
            String message = FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.client-brand");
            message = Color.translate(message);
            message = message.replace("{player}", p.getUsername()).replace("{brand}", new String(msg, "UTF-8").substring(1));
            String finalMessage = message;
            FlappyAnticheat.getInstance().dataManager.dataMap.values()
                    .stream().filter(playerData -> p.hasPermission("flappyanticheat.alerts") && !FlappyAnticheat.getInstance().dataManager.alertsDisabled.contains(p))
                    .forEach(playerData -> playerData.player.sendMessage(finalMessage));

            if (FlappyAnticheat.getInstance().getConfig().getBoolean("messages.flag-to-console")) {
                //Bukkit.getConsoleSender().sendMessage(Color.translate(message));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void addChannel(Player p, String channel) {
        try {
            p.getClass().getMethod("addChannel", String.class).invoke(p, channel);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            e.printStackTrace();
        }
    }
}