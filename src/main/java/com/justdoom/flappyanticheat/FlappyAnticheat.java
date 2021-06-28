package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.checks.CheckManager;
import com.justdoom.flappyanticheat.commands.FlagClickCommand;
import com.justdoom.flappyanticheat.commands.FlappyACCommand;
import com.justdoom.flappyanticheat.data.FileData;
import com.justdoom.flappyanticheat.data.PlayerDataManager;
import com.justdoom.flappyanticheat.commands.tabcomplete.FlappyAnticheatTabCompletion;
import com.justdoom.flappyanticheat.listener.PlayerConnectionListener;
import com.justdoom.flappyanticheat.utils.BrandMessageUtil;
import com.justdoom.flappyanticheat.violations.ViolationHandler;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

public class FlappyAnticheat extends Extension {

    private static FlappyAnticheat instance;

    public static FlappyAnticheat getInstance() {
        return instance;
    }

    public ViolationHandler violationHandler;
    public PlayerDataManager dataManager;
    public FileData fileData;

    public FlappyAnticheat(){
        instance = this;
    }

    @Override
    public void initialize() {

        //Register incoming plugin channel for client brand

        //Messenger messenger = Bukkit.getMessenger();
        //messenger.registerIncomingPluginChannel(FlappyAnticheat.getInstance(), "minecraft:brand", new BrandMessageUtil());

        //Register events
        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);

        //Register commands
        this.getCommand("flappyanticheat").setExecutor(new FlappyACCommand());
        this.getCommand("flappyacflagclick").setExecutor(new FlagClickCommand());

        //Register Tab completion
        //this.getCommand("flappyanticheat").setTabCompleter(new FlappyAnticheatTabCompletion());

        loadModules();
    }

    @Override
    public void terminate() {
        System.out.println("e");
    }

    public void loadModules(){
        CheckManager checkManager = new CheckManager(this);
        checkManager.loadChecks();
        dataManager = new PlayerDataManager();
        fileData = new FileData();
        violationHandler = new ViolationHandler();
    }
}