package com.justdoom.flappyanticheat.listener;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.utils.BrandMessageUtil;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;

public class PlayerConnectionListener {

    public final FlappyAnticheat flappyAnticheat;

    public PlayerConnectionListener(FlappyAnticheat flappyAnticheat) {
        this.flappyAnticheat = flappyAnticheat;

        EventNode<Event> node = EventNode.all("demo");
        node.addListener(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();

            flappyAnticheat.dataManager.addPlayer(player.getUuid());

            //ClientVersion clientVersion = PacketEvents.get().getPlayerUtils().getClientVersion(player);
            //WrappedGameProfile e = PacketEvents.get().getPlayerUtils().getGameProfile(player);
            //event.getPlayer().sendMessage(String.valueOf(e.getName()));

            BrandMessageUtil.addChannel(player, "minecraft:brand");
        });

        node.addListener(PlayerDisconnectEvent.class, event -> {
            Player player = event.getPlayer();

            flappyAnticheat.violationHandler.clearViolations(player);
            flappyAnticheat.dataManager.removePlayer(player.getUuid());
            flappyAnticheat.dataManager.disabledAlertsRemove(player);
        });
    }
}