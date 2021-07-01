package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.combat.forcefield.ForcefieldA;
import com.justdoom.flappyanticheat.checks.combat.range.RangeA;
import com.justdoom.flappyanticheat.checks.movement.fly.FlyA;
import com.justdoom.flappyanticheat.checks.movement.groundspoof.GroundSpoofA;
import com.justdoom.flappyanticheat.checks.movement.noslow.NoSlowA;
import com.justdoom.flappyanticheat.checks.movement.speed.SpeedA;
import com.justdoom.flappyanticheat.checks.player.anticactus.AntiCactusA;
import com.justdoom.flappyanticheat.checks.player.blockplace.BlockPlaceA;
import com.justdoom.flappyanticheat.checks.player.badpackets.BadPacketsA;
import com.justdoom.flappyanticheat.checks.player.badpackets.BadPacketsB;
import com.justdoom.flappyanticheat.checks.player.blockplace.BlockPlaceB;
import com.justdoom.flappyanticheat.checks.player.skinblinker.SkinBlinkerA;
import com.justdoom.flappyanticheat.checks.player.timer.TimerA;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

public class CheckManager {

    private final FlappyAnticheat plugin;

    public CheckManager(FlappyAnticheat plugin) {
        this.plugin = plugin;
    }

    public void loadChecks(){
        new GroundSpoofA();
        //PacketEvents.get().registerListener(new FlyA());
        new BadPacketsA();
        //PacketEvents.get().registerListener(new BadPacketsB());
        //PacketEvents.get().registerListener(new NoSlowA());
        new SkinBlinkerA();
        new AntiCactusA();
        new TimerA();
        new ForcefieldA();
        new RangeA();

        new BlockPlaceA();
        new BlockPlaceB();
        new SpeedA();
        //Bukkit.getPluginManager().registerEvents(new InventoryA(), plugin);
    }
}