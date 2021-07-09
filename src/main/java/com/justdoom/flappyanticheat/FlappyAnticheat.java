package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.checks.CheckManager;
import com.justdoom.flappyanticheat.commands.FlagClickCommand;
import com.justdoom.flappyanticheat.commands.FlappyACCommand;
import com.justdoom.flappyanticheat.data.FileData;
import com.justdoom.flappyanticheat.data.PlayerDataManager;
import com.justdoom.flappyanticheat.utils.FileUtil;
import com.justdoom.flappyanticheat.violations.ViolationHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;
import org.spongepowered.configurate.CommentedConfigurationNode;
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

        try {
            if(!FileUtil.doesFileExist("./extensions/FlappyAnticheat"))
                FileUtil.createDirectory("./extensions/FlappyAnticheat");

            if(!FileUtil.doesFileExist("./extensions/FlappyAnticheat/config.yml"))
                FileUtil.addConfig("./extensions/FlappyAnticheat/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of("./extensions/FlappyAnticheat/config.yml")) // Set where we will load and save to
                .build();

        try {
            root = loader.load();
            getLogger().info("Config has been loaded");
        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
            return;
        }

        loadModules();

        //Register commands
        MinecraftServer.getCommandManager().register(new FlagClickCommand());
        MinecraftServer.getCommandManager().register(new FlappyACCommand());
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