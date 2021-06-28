package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.customevents.FlagEvent;
import com.justdoom.flappyanticheat.customevents.PunishEvent;
import com.justdoom.flappyanticheat.utils.Color;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

import java.util.concurrent.atomic.AtomicBoolean;

public class Check {

    public String check, checkType;
    public boolean experimental;

    public Check(String check, String checkType, boolean experimental) {

        this.check = check;
        this.checkType = checkType;
        this.experimental = experimental;
    }

    public void fail(String debug, Player player) {
        FlagEvent flagEvent = new FlagEvent(player, this);
        EventNode<Event> node = EventNode.all("demo");
        node.call(flagEvent);
        if(flagEvent.isCancelled()) {
            return;
        }

        if (player.hasPermission("flappyanticheat.bypass") || player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE)
            return;

        FlappyAnticheat.getInstance().violationHandler.addViolation(this, player);

        String flagmsg = FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.failed-check");
        flagmsg = flagmsg.replace("{player}", player.getName()).replace("{check}", this.check + " " + checkType).replace("{vl}", String.valueOf(FlappyAnticheat.getInstance().violationHandler.getViolations(this, player)));
        String hover = FlappyAnticheat.getInstance().getConfig().getString("messages.hover").replace("{ping}", String.valueOf(PlayerUtil.getPing(player))).replace("{debug}", debug).replace("{tps}", String.valueOf(PacketEvents.get().getServerUtils().getTPS()));

        if(experimental){
            flagmsg = flagmsg+"&r*";
        }

        TextComponent component = new TextComponent(Color.translate(flagmsg));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Color.translate(hover)).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/flappyacflagclick " + player.getName()));

        FlappyAnticheat.getInstance().dataManager.dataMap.values()
                .stream().filter(playerData -> player.hasPermission("flappyanticheat.alerts") && !FlappyAnticheat.getInstance().dataManager.alertsDisabled.contains(player))
                .forEach(playerData -> playerData.player.spigot().sendMessage(component));

        if (FlappyAnticheat.getInstance().getConfig().getBoolean("messages.flag-to-console")) {
            Bukkit.getConsoleSender().sendMessage(Color.translate(flagmsg));
        }

        FlappyAnticheat.getInstance().fileData.addToFile("violations.txt", "\n" + Color.translate(flagmsg + " " + debug));
    }

    public void punish(Player player, String path){
        PunishEvent punishEvent = new PunishEvent(player, this);
        Bukkit.getPluginManager().callEvent(punishEvent);
        if(punishEvent.isCancelled()) {
            return;
        }

        for(String command: FlappyAnticheat.getInstance().getConfig().getStringList(path + ".punish-commands")) {
            command = command.replace("{player}", player.getName());
            String finalCommand = command;
            Bukkit.getScheduler().runTask(FlappyAnticheat.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand));
        }
        if(FlappyAnticheat.getInstance().getConfig().getBoolean(path + ".broadcast-punishment")) {
            for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                p.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("messages.punish")).replace("{player}", player.getName()));
            }
        }
    }

    public void sync(Runnable runnable) {
        AtomicBoolean waiting = new AtomicBoolean(true);
        if (FlappyAnticheat.getInstance().isEnabled()) {
            Bukkit.getScheduler().runTask(FlappyAnticheat.getInstance(), () -> {
                runnable.run();
                waiting.set(false);
            });
        }
        while (waiting.get()) {
        }
    }
}