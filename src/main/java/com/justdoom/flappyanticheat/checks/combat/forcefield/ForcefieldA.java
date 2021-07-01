package com.justdoom.flappyanticheat.checks.combat.forcefield;

import com.justdoom.flappyanticheat.checks.Check;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.play.ClientEntityActionPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;

public class ForcefieldA extends Check {

    private long lastFlying; //when the last flying packet was sent
    private int buffer; //for prevent falses

    public ForcefieldA() {
        super("Forcefield", "A", false);

        EventNode<Event> node = EventNode.all("demo");
        node.addListener(PlayerPacketEvent.class, event -> {
            if(event.getPacket() instanceof ClientPlayerPositionPacket ||
                    event.getPacket() instanceof ClientPlayerPositionAndRotationPacket) { //packets sended every ticks

                this.lastFlying = System.currentTimeMillis();  //setting the value when a flying packet is sent

            }else if(event.getPacket() instanceof ClientEntityActionPacket)  {
                WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(event.getNMSPacket());
                if(wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                    if((System.currentTimeMillis() - this.lastFlying) < 10L) /** this value should be around 40**/ {
                        if(++this.buffer > 5) {
                            fail("dF=" +(System.currentTimeMillis() - this.lastFlying),event.getPlayer());
                        }


                    }else buffer -= buffer > 0 ? 0.25 : 0;
                }
            }
        });
    }
}
