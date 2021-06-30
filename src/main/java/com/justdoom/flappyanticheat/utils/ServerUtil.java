package com.justdoom.flappyanticheat.utils;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import net.minestom.server.MinecraftServer;

public class ServerUtil {

    public static boolean lowTPS(String path){
        String[] arrayPath = path.split("\\.");

        if(MinecraftServer.TICK_PER_SECOND <= FlappyAnticheat.getInstance().root.node(arrayPath, "min-tps").getDouble())
            return true;
        return false;
    }
}
