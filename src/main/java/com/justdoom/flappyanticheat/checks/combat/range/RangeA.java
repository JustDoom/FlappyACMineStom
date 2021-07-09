package com.justdoom.flappyanticheat.checks.combat.range;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.data.PlayerData;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPacketEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RangeA extends Check {

    private long lastKP;
    private int preVL;


    private List<BoundingBox> rayBB(PlayerData data, int ticks) {
        List<BoundingBox> toReturn = new ArrayList<>();
        if (data.pastVictimBoxes.size() > 4) {
            for (int range = 0; range < 2; range++) {
                toReturn.add(data.pastVictimBoxes.get(ticks + range));
            }
        }
        return toReturn;



    }

    public RangeA() {
        super("Range", "A", false);

        /**GlobalEventHandler node = MinecraftServer.getGlobalEventHandler();
        node.addListener(PlayerPacketEvent.class, event -> {

            PlayerData data = FlappyAnticheat.getInstance().dataManager.getData(event.getPlayer().getUuid());

            if (event.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
                if (packet.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                    data.vicitm = (LivingEntity) packet.getEntity();
                    if (data.pastVictimBoxes.size() != 0) {
                        int ticks = data.ping / 50;
                        List<BoundingBox> fromRay = this.rayBB(data, ticks);
                        Vector attacker = data.player.getEyeLocation().toVector();
                        double distance = fromRay.stream().mapToDouble(bb -> PlayerUtil.getDistanceBB(bb, attacker)).min().orElse(0);
                        if (distance > 3.2D) {
                            if (this.preVL++ > 4) {
                                fail("d=" + (float) distance, data.player);
                            }
                        } else this.preVL *= 0.975;
                        //Bukkit.broadcastMessage("distance=" + distance);


                    }

                }

            } else if (event.getPacketId() == PacketType.Play.Client.KEEP_ALIVE) {
                data.ping = (int) (System.currentTimeMillis() - this.lastKP);
            }
        });**/
    }

    /**@Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        if (event.getPacketId() == PacketType.Play.Server.KEEP_ALIVE) {
            this.lastKP = System.currentTimeMillis();

        }
    }**/
}
