package com.justdoom.flappyanticheat.checks.player.blockplace;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class BlockPlaceA extends Check {

    public BlockPlaceA() {
        super("BlockPlace", "A", true);

        EventNode<Event> node = EventNode.all("demo");
        node.addListener(PlayerBlockPlaceEvent.class, event -> {

            if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            Player player = event.getPlayer();

            Block block = player.getInstance().getBlock(event.getBlockPosition());
            boolean placedOnAir = true;

            //todo finish port of blockplace a

            /**List<Material> blockFace = new ArrayList<Material>() {{
                add(block.getAlternative());
                add(block.getRelative(BlockFace.NORTH).getType());
                add(block.getRelative(BlockFace.EAST).getType());
                add(block.getRelative(BlockFace.SOUTH).getType());
                add(block.getRelative(BlockFace.WEST).getType());
                add(block.getRelative(BlockFace.BOTTOM).getType());
            }};

            for (Material material : blockFace) {
                if (material != Material.AIR && material != Material.LAVA && material != Material.WATER && material != Material.CAVE_AIR && material != Material.VOID_AIR) {
                    placedOnAir = false;
                    break;
                }
            }
            if (placedOnAir) {
                Bukkit.getScheduler().runTaskAsynchronously(FlappyAnticheat.getInstance(), () -> fail("faces=" + blockFace, player));
            }**/
        });
    }
}
