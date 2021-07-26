package com.justdoom.flappyanticheat.utils;

import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

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
        if(player.getInstance().getBlock(player.getPosition()).isLiquid() || player.getInstance().getBlock(player.getPosition()).id() == Block.LAVA.id()){
            return true;
        }
        return false;
    }

    public static boolean isOnClimbable(Player player) {
        Block block = player.getInstance().getBlock(player.getPosition());
        if(block.id() == Material.LADDER.getId() || block.id() == Material.VINE.getId() ){
            return true;
        }
        return false;
    }

    public static Set<Block> getNearbyBlocks(Pos location, int radius, Instance world) {
        Set<Block> blocks = new HashSet<>();

        for(int x = (int) (location.x() - radius); x <= location.x() + radius; x++) {
            for(int y = (int) (location.y() - radius); y <= location.y() + radius; y++) {
                for(int z = (int) (location.z() - radius); z <= location.z() + radius; z++) {
                    blocks.add(world.getBlock(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static boolean isFlying(Player player){
        return player.isFlying();
    }

    public static double getDistanceBB(BoundingBox origin, Vec attacker) {
        double x = Math.min(Math.pow(attacker.x() - origin.getMinX(),2.0), Math.pow(attacker.x() - origin.getMaxX(),2.0));
        double z = Math.min(Math.pow(attacker.z() - origin.getMinZ(),2.0), Math.pow(attacker.z() - origin.getMaxZ(),2.0));
        return Math.sqrt(x + z);
    }
}
