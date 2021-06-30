package com.justdoom.flappyanticheat.violations;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.customevents.ViolationResetEvent;
import com.justdoom.flappyanticheat.utils.Color;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

import java.util.*;

public class ViolationHandler {

    private Map<UUID, Map<Check, Integer>> violations = new HashMap<>();

    public ViolationHandler(){
        int delay = FlappyAnticheat.getInstance().root.node("violation-reset-delay").getInt() * 20;

        Bukkit.getScheduler().runTaskTimerAsynchronously(FlappyAnticheat.getInstance(), () -> {
            ViolationResetEvent violationResetEvent = new ViolationResetEvent();
            Bukkit.getPluginManager().callEvent(violationResetEvent);
            if(!violationResetEvent.isCancelled() && MinecraftServer.getConnectionManager().getOnlinePlayers().size() > 0) {
                clearAllViolations();
                for(Player p: MinecraftServer.getConnectionManager().getOnlinePlayers()){
                    if(p.hasPermission("flappyanticheat.alerts")){
                        p.sendMessage(Color.translate(FlappyAnticheat.getInstance().root.node("prefix").getString() + FlappyAnticheat.getInstance().root.node("messages", "violation-reset", "all").getString()));
                    }
                }
                if(FlappyAnticheat.getInstance().root.node("messages", "flag-to-console").getBoolean()) {
                    System.out.println(Color.translate(FlappyAnticheat.getInstance().root.node("prefix").getString() + FlappyAnticheat.getInstance().root.node("messages", "violation-reset", "all").getString()));
                }
            }
        }, delay, delay);
    }

    public void addViolation(Check check, Player p){
        String path = "checks." + check.check.toLowerCase() + "." + check.checkType.toLowerCase();

        if(!FlappyAnticheat.getInstance().root.node("checks", check.check.toLowerCase(), check.checkType.toLowerCase(), "enabled").getBoolean()){
            return;
        }

        int violation = FlappyAnticheat.getInstance().root.node("checks", check.check.toLowerCase(), check.checkType.toLowerCase(), "vl").getInt();
        Map<Check, Integer> vl = new HashMap<>();
        if (this.violations.containsKey(p.getUuid())) {
            vl = this.violations.get(p.getUuid());
        }
        if (!vl.containsKey(check)) {
            vl.put(check, violation);
        } else {
            vl.put(check, vl.get(check) + violation);
        }
        this.violations.put(p.getUuid(), vl);
        if(getViolations(check, p) >= FlappyAnticheat.getInstance().root.node("checks", check.check.toLowerCase(), check.checkType.toLowerCase(), "punish-vl").getInt()){
            if(FlappyAnticheat.getInstance().root.node("checks", check.check.toLowerCase(), check.checkType.toLowerCase(), "punishable").getBoolean()){
                check.punish(p, path);
                FlappyAnticheat.getInstance().fileData.addToFile("punishments.txt", "\n" + p.getName() + " has been punished for " + check.check.toLowerCase() + " " + check.checkType.toLowerCase());
            }
        }
    }

    public Integer getViolations(Check check, Player p) {
        if (this.violations.containsKey(p.getUuid())) {
            if (this.violations.get(p.getUuid()).containsKey(check)) {
                return this.violations.get(p.getUuid()).get(check);
            }
        }
        return 0;
    }

    public void clearViolations(Player p) {
        this.violations.remove(p.getUuid());
    }

    public void clearAllViolations() {
        this.violations.clear();
    }
}