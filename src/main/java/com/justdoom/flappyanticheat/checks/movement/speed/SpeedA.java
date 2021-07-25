package com.justdoom.flappyanticheat.checks.movement.speed;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpeedA extends Check {

    private Map<UUID, Double> buffer = new HashMap<>();
    private Map<UUID, Boolean> lastOnGround = new HashMap<>();

    private Map<UUID, Double> lastDist = new HashMap<>();

    public SpeedA() {
        super("Speed", "A", true);

        GlobalEventHandler node = MinecraftServer.getGlobalEventHandler();
        node.addListener(PlayerMoveEvent.class, event -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUuid();

            if (ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            Pos to = new Pos(event.getNewPosition().x(), event.getNewPosition().y(), event.getNewPosition().z());
            float friction = 0.91f;

            double distX = to.x() - player.getPosition().x();
            double distZ = to.z() - player.getPosition().z();
            double dist = Math.sqrt((distX * distX) + (distZ * distZ));
            double lastDist = this.lastDist.getOrDefault(uuid, 0.0);

            this.lastDist.put(uuid, dist);
            boolean onGround = player.isOnGround();
            boolean lastOnGround = this.lastOnGround.getOrDefault(uuid, true);
            this.lastOnGround.put(uuid, onGround);
            double shiftedLastDist = lastDist * friction;
            double equalness = dist - shiftedLastDist;

            double bufferOrDefault = this.buffer.getOrDefault(uuid, 0.0);

            //? new EntityFinder()
            if (!PlayerUtil.isOnClimbable(player) && !onGround && !lastOnGround /**&& !(player.getNearbyEntities(1.5, 10, 1.5).size() > 0)**/ && this.buffer.put(uuid, ++bufferOrDefault) > 2) {

                boolean pistonHead = false;

                for (Block block : PlayerUtil.getNearbyBlocks(
                        new Pos(player.getPosition().x(), player.getPosition().y(), player.getPosition().z()), 2, player.getInstance())) {
                    if (block.id() == Block.PISTON_HEAD.id()) {
                        pistonHead = true;
                        break;
                    }
                }

                if (equalness > 0.027 && !pistonHead) {
                    fail("e=" + equalness, player);
                }
            } else if(this.buffer.getOrDefault(uuid, 0.0) > 0) {
                bufferOrDefault = this.buffer.getOrDefault(uuid, 0.0);
                this.buffer.put(uuid, bufferOrDefault -= 0.5);
            }
        });
    }
}
