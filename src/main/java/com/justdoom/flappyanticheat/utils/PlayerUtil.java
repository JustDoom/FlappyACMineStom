package com.justdoom.flappyanticheat.utils;

import net.minestom.server.collision.BoundingBox;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.utils.Position;
import net.minestom.server.utils.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class PlayerUtil {

    public static int getPing(Player player) {
        try {
            Method handle = player.getClass().getMethod("getHandle");
            Object nmsHandle = handle.invoke(player);
            Field pingField = nmsHandle.getClass().getField("ping");
            return pingField.getInt(nmsHandle);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
                | NoSuchFieldException e) {
            //Bukkit.getLogger().log(Level.SEVERE, "Exception while trying to get player ping.", e);
        }

        return -1;
    }

    public static boolean isInLiquid(Player player) {
        if(player.getInstance().getBlock(player.getPosition().toBlockPosition()).isLiquid() || player.getInstance().getBlock(player.getPosition().toBlockPosition()).getBlockId() == Block.LAVA.getBlockId()){
            return true;
        }
        return false;
    }

    public static boolean isOnClimbable(Player player) {
        Block block = player.getInstance().getBlock(player.getPosition().toBlockPosition());
        if(block.getBlockId() == Material.LADDER.getId() || block.getBlockId() == Material.VINE.getId() ){
            return true;
        }
        return false;
    }

    public static Set<Block> getNearbyBlocks(Position location, int radius, Instance world) {
        Set<Block> blocks = new HashSet<>();

        for(int x = location.toBlockPosition().getX() - radius; x <= location.toBlockPosition().getX() + radius; x++) {
            for(int y = location.toBlockPosition().getY() - radius; y <= location.toBlockPosition().getY() + radius; y++) {
                for(int z = location.toBlockPosition().getZ() - radius; z <= location.toBlockPosition().getZ() + radius; z++) {
                    blocks.add(world.getBlock(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static boolean isFlying(Player player){
        return player.isFlying();
    }

    public static double getDistanceBB(BoundingBox origin, Vector attacker) {
        double x = Math.min(Math.pow(attacker.getX() - origin.getMinX(),2.0), Math.pow(attacker.getX() - origin.getMaxX(),2.0));
        double z = Math.min(Math.pow(attacker.getZ() - origin.getMinZ(),2.0), Math.pow(attacker.getZ() - origin.getMaxZ(),2.0));
        return Math.sqrt(x + z);
    }
}
