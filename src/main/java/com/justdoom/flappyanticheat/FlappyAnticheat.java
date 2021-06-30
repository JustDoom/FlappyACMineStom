package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.checks.CheckManager;
import com.justdoom.flappyanticheat.commands.FlagClickCommand;
import com.justdoom.flappyanticheat.commands.FlappyACCommand;
import com.justdoom.flappyanticheat.data.FileData;
import com.justdoom.flappyanticheat.data.PlayerDataManager;
import com.justdoom.flappyanticheat.listener.PlayerConnectionListener;
import com.justdoom.flappyanticheat.utils.FileUtil;
import com.justdoom.flappyanticheat.violations.ViolationHandler;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import net.minestom.server.extensions.Extension;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

public class FlappyAnticheat extends Extension {

    private static FlappyAnticheat instance;

    public static FlappyAnticheat getInstance() {
        return instance;
    }

    public ViolationHandler violationHandler;
    public PlayerDataManager dataManager;
    public FileData fileData;

    public CommentedConfigurationNode root;

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

        loadModules();

        try {
            if(!FileUtil.doesFileExist("./FlappyAnticheat"))
                FileUtil.createDirectory("./FlappyAnticheat");

            if(!FileUtil.doesFileExist("./FlappyAnticheat/config.yml"))
                FileUtil.addConfig("./FlappyAnticheat/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of("./FlappyAnticheat/config.yml")) // Set where we will load and save to
                .build();

        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
            return;
        }
    }

    @Override
    public void terminate() { }

    public void loadModules(){
        new FileUtil();
        CheckManager checkManager = new CheckManager(this);
        checkManager.loadChecks();
        dataManager = new PlayerDataManager();
        fileData = new FileData();
        violationHandler = new ViolationHandler();
    }
}