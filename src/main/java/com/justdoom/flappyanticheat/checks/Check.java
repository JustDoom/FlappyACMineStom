package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.customevents.FlagEvent;
import com.justdoom.flappyanticheat.customevents.PunishEvent;
import com.justdoom.flappyanticheat.utils.ColorUtil;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import org.spongepowered.configurate.serialize.SerializationException;

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
        MinecraftServer.getGlobalEventHandler().call(flagEvent);
        if(flagEvent.isCancelled()) {
            return;
        }

        if (player.hasPermission("flappyanticheat.bypass") || player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE)
            return;

        FlappyAnticheat.getInstance().violationHandler.addViolation(this, player);

        String flagmsg = FlappyAnticheat.getInstance().root.node("prefix").getString() + FlappyAnticheat.getInstance().root.node("messages", "failed-check").getString();
        flagmsg = flagmsg.replace("{player}", player.getUsername()).replace("{check}", this.check + " " + checkType).replace("{vl}", String.valueOf(FlappyAnticheat.getInstance().violationHandler.getViolations(this, player)));
        //String hover = FlappyAnticheat.getInstance().root.node("messages", "hover").getString().replace("{ping}", String.valueOf(PlayerUtil.getPing(player))).replace("{debug}", debug).replace("{tps}", String.valueOf(MinecraftServer.TICK_PER_SECOND));

        if(experimental){
            flagmsg = flagmsg+"&r*";
        }

        //TextComponent component = new TextComponent(ColorUtil.translate(flagmsg));
        //component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtil.translate(hover)).create()));
        //component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/flappyacflagclick " + player.getName()));

        String finalFlagmsg = flagmsg;
        /**FlappyAnticheat.getInstance().dataManager.dataMap.values()
                .stream().filter(playerData -> player.hasPermission("flappyanticheat.alerts") && !FlappyAnticheat.getInstance().dataManager.alertsDisabled.contains(player))
                .forEach(playerData -> playerData.**/player.sendMessage(ColorUtil.translate(finalFlagmsg));

        if (FlappyAnticheat.getInstance().root.node("messages", "flag-to-console").getBoolean()) {
            System.out.println(ColorUtil.translate(flagmsg));
        }

        FlappyAnticheat.getInstance().fileData.addToFile("violations.txt", "\n" + ColorUtil.translate(flagmsg + " " + debug));
    }

    public void punish(Player player, String path) {

        String[] arrayPath = path.split("\\.");

        PunishEvent punishEvent = new PunishEvent(player, this);
        MinecraftServer.getGlobalEventHandler().call(punishEvent);
        if(punishEvent.isCancelled()) {
            return;
        }

        try {
            for (String command : FlappyAnticheat.getInstance().root.node(arrayPath, "punish-commands").getList(String.class)) {
                command = command.replace("{player}", player.getUsername());
                String finalCommand = command;
                MinecraftServer.getCommandManager().execute(MinecraftServer.getCommandManager().getConsoleSender(), finalCommand);
            }
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        if(FlappyAnticheat.getInstance().root.node(path, "broadcast-punishment").getBoolean()) {
            for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                p.sendMessage(ColorUtil.translate(FlappyAnticheat.getInstance().root.node("messages", "punish").getString().replace("{player}", player.getUsername())));
            }
        }
    }

    public void sync(Runnable runnable) {
        AtomicBoolean waiting = new AtomicBoolean(true);
        runnable.run();
        waiting.set(false);
        while (waiting.get()) {
        }
    }
}