package com.justdoom.flappyanticheat.checks.player.blockplace;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;

public class BlockPlaceB extends Check {

    public BlockPlaceB() {
        super("BlockPlace", "B", true);

        GlobalEventHandler node = MinecraftServer.getGlobalEventHandler();
        node.addListener(PlayerBlockPlaceEvent.class, event -> {

            Player player = event.getPlayer();

            Block block = player.getInstance().getBlock(event.getBlockPosition());

            ItemStack mainHand = player.getItemInMainHand();
            ItemStack offHand = player.getItemInOffHand();

            if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            if (block.id() != mainHand.getMaterial().getId() && block.id() != offHand.getMaterial().getId()) {

                fail("hand=" + mainHand.getMaterial().getId() + " placed=" + block.id(), player);
            }
        });
    }
}