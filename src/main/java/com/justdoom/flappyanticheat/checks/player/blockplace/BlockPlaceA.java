package com.justdoom.flappyanticheat.checks.player.blockplace;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class BlockPlaceA extends Check implements Listener {

    public BlockPlaceA() {
        super("BlockPlace", "A", true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
            return;

        Player player = event.getPlayer();

        Block block = event.getBlock();
        boolean placedOnAir = true;

        List<Material> blockFace = new ArrayList<Material>() {{
            add(block.getRelative(BlockFace.TOP).getType());
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
        }
    }
}
