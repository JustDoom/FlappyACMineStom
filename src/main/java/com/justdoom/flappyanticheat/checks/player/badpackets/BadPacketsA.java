package com.justdoom.flappyanticheat.checks.player.badpackets;

import com.justdoom.flappyanticheat.checks.Check;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;

public class BadPacketsA extends Check {

    public BadPacketsA(){
        super("BadPackets", "A", false);

        GlobalEventHandler node = MinecraftServer.getGlobalEventHandler();
        node.addListener(PlayerPacketEvent.class, event -> {
            if (event.getPacket() instanceof ClientPlayerPositionPacket || event.getPacket() instanceof ClientPlayerPositionAndRotationPacket) {

                float pitch = event.getPlayer().getPosition().getPitch();
                if(Math.abs(pitch) > 90F || Math.abs(pitch) < -90F){
                    String suspectedHack = "Old/Bad KillAura (This cannot false)";
                    fail("&7pitch=&2" + pitch + " &7Suspected Hack: &2" + suspectedHack, event.getPlayer());
                }
            }
        });
    }
}