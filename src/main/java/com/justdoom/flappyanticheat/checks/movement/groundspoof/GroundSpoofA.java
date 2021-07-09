package com.justdoom.flappyanticheat.checks.movement.groundspoof;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.gamedata.tags.Tag;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.ClientPlayPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.minestom.server.utils.Position;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GroundSpoofA extends Check {

    private int buffer = 0;

    private int justJoined = 0;

    public GroundSpoofA(){
        super("GroundSpoof", "A", false);

        GlobalEventHandler node = MinecraftServer.getGlobalEventHandler();
        node.addListener(PlayerPacketEvent.class, event -> {
            Player player = event.getPlayer();
            if (event.getPacket() instanceof ClientPlayerPositionPacket) {
                if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()) || player.getPosition().getY() < 1 || player.isDead()) {
                    return;
                }

                ClientPlayerPositionPacket packet = ((ClientPlayerPositionPacket) event.getPacket());

                double groundY = 0.015625;
                boolean client = packet.onGround, server = packet.y % groundY < 0.0001;

                System.out.println("Client: " + client);
                System.out.println("Server: " + server);
                if (client != server/** && !PlayerUtil.isOnClimbable(player)**/) {
                    //if (++buffer > 1) {

                        boolean boat = false;
                        boolean shulker = false;
                        boolean pistonHead = false;

                        /**AtomicReference<List<Entity>> nearby = new AtomicReference<>();
                        //sync(() -> nearby.set(player.getNearbyEntities(1.5, 10, 1.5)));

                        for (Entity entity : nearby.get()) {
                            if (entity.getEntityId() == EntityType.BOAT.getId() && player.getPosition().getY() > entity.getPosition().getY()) {
                                boat = true;
                                break;
                            }

                            if (entity.getEntityId() == EntityType.SHULKER.getId() && player.getPosition().getY() > entity.getBoundingBox().getMinY()) {
                                shulker = true;
                                break;
                            }
                        }

                        for (Block block : PlayerUtil.getNearbyBlocks(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()), 2, player.getInstance())) {

                            if (block.getBlockId() == Block.SHULKER_BOX.getBlockId()) {
                                shulker = true;
                                break;
                            }

                            if (block.getBlockId() == Block.PISTON_HEAD.getBlockId()) {
                                pistonHead = true;
                                break;
                            }
                        }**/

                        //if (!boat && !shulker && !pistonHead) {
                            System.out.println("5");
                            String suspectedHack;
                            if(player.getPosition().getY() % groundY == 0.0){
                                suspectedHack = "Criticals/Anti Hunger";
                            } else {
                                suspectedHack = "NoFall";
                            }
                            System.out.println("FAIL");
                            fail("mod=" + player.getPosition().getY() % groundY + " &7Client: &2" + client + " &7Server: &2" + server + " &7Suspected Hack: &2" + suspectedHack, player);
                        //}
                    //}
                } else if (buffer > 0) buffer--;
            }
        });
    }
}