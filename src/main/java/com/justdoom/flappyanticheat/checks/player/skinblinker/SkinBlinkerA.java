package com.justdoom.flappyanticheat.checks.player.skinblinker;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerSettingsChangeEvent;

public class SkinBlinkerA extends Check {

    private int lastSkin = -1;

    public SkinBlinkerA(){
        super("SkinBlinker", "A", false);

        //TODO finish skinblinker port

        /**EventNode<Event> node = EventNode.all("demo");
        node.addListener(PlayerSettingsChangeEvent.class, event -> {

                Player player = event.getPlayer();

                if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                    return;

                if (lastSkin == -1) {
                    player.getSkin().get
                    lastSkin = event.getDisplaySkinPartsMask();
                    return;
                }

                if ((player.isSprinting()
                        || player.isSneaking()
                        || player.isBlocking())
                        && lastSkin != packet.getDisplaySkinPartsMask()) {
                    fail("&7last=&2" + lastSkin + " &7current=&2" + packet.getDisplaySkinPartsMask(), player);
                }

                lastSkin = packet.getDisplaySkinPartsMask();
        });**/
    }
}