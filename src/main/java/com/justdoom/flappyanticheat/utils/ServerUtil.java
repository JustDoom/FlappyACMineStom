package com.justdoom.flappyanticheat.utils;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import io.github.retrooper.packetevents.PacketEvents;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.thread.ThreadProvider;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

public class ServerUtil {

    public static boolean lowTPS(String path){

        if(MinecraftServer.TICK_PER_SECOND <= FlappyAnticheat.getInstance().getConfig().getDouble(path + ".min-tps"))
            return true;
        return false;


    }
}
