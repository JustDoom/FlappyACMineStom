package com.justdoom.flappyanticheat.data;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.CheckManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;

import java.util.*;

public class PlayerData {

    public final UUID uuid;
    public Player player;
    public final List<BoundingBox> pastVictimBoxes;
    public LivingEntity vicitm;
    public int ping;

    public CheckManager checkManager;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.player = MinecraftServer.getConnectionManager().getPlayer(uuid);
        this.pastVictimBoxes = new ArrayList<>();

        this.checkManager = new CheckManager(FlappyAnticheat.getInstance());
        new BukkitRunnable() {

            @Override
            public void run() {
                if(vicitm != null) {
                    if(pastVictimBoxes.size() > 20) pastVictimBoxes.clear();
                    pastVictimBoxes.add(vicitm.getBoundingBox());
                }
            }
        }.runTaskTimerAsynchronously(FlappyAnticheat.getInstance(), 0L, 1L);
    }
}