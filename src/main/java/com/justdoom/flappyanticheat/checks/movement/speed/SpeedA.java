package com.justdoom.flappyanticheat.checks.movement.speed;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.utils.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class SpeedA extends Check {

    private Map<UUID, Double> buffer = new HashMap<>();
    private Map<UUID, Boolean> lastOnGround = new HashMap<>();

    private Map<UUID, Double> lastDist = new HashMap<>();

    public SpeedA() {
        super("Speed", "A", false);
    }

    public void SpeedA() {
        EventNode<Event> node = EventNode.all("demo");
        node.addListener(PlayerMoveEvent.class, event -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUuid();

            if (ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            Position to = new Position(event.getNewPosition().getX(), event.getNewPosition().getY(), event.getNewPosition().getZ());
            float friction = 0.91f;

            double distX = to.getX() - player.getPosition().getX();
            double distZ = to.getZ() - player.getPosition().getZ();
            double dist = Math.sqrt((distX * distX) + (distZ * distZ));
            double lastDist = this.lastDist.getOrDefault(uuid, 0.0);

            this.lastDist.put(uuid, dist);
            boolean onGround = player.isOnGround();
            boolean lastOnGround = this.lastOnGround.getOrDefault(uuid, true);
            this.lastOnGround.put(uuid, onGround);
            double shiftedLastDist = lastDist * friction;
            double equalness = dist - shiftedLastDist;

            double bufferOrDefault = this.buffer.getOrDefault(uuid, 0.0);

            if (!PlayerUtil.isOnClimbable(player) && !onGround && !lastOnGround && !(player.getNearbyEntities(1.5, 10, 1.5).size() > 0) && this.buffer.put(uuid, ++bufferOrDefault) > 2) {

                boolean pistonHead = false;

                for (Block block : PlayerUtil.getNearbyBlocks(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()), 2)) {
                    if (block.getBlockId() == Block.PISTON_HEAD.getBlockId()) {
                        pistonHead = true;
                        break;
                    }
                }

                if (equalness > 0.027 && !pistonHead) {
                    Bukkit.getScheduler().runTaskAsynchronously(FlappyAnticheat.getInstance(), () -> fail("e=" + equalness, player));
                }
            } else if(this.buffer.getOrDefault(uuid, 0.0) > 0) {
                bufferOrDefault = this.buffer.getOrDefault(uuid, 0.0);
                this.buffer.put(uuid, bufferOrDefault -= 0.5);
            }
        });
    }
}
