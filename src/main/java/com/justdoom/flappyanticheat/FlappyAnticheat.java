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
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import javax.annotation.Nullable;
import java.nio.file.Paths;

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

        /**final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(Paths.get("myproject.conf")) // Set where we will load and save to
                .build();

        final CommentedConfigurationNode root;
        try {
            root = loader.load();
        } catch (final ConfigurateException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
            return;
        }

        final ConfigurationNode countNode = root.node("messages", "count");
        final ConfigurationNode moodNode = root.node("messages", "mood");

        final @Nullable String name = root.node("name").getString();
        final int count = countNode.getInt(Integer.MIN_VALUE);
        final @Nullable Mood mood = moodNode.get(Mood.class);

        if (name == null || count == Integer.MIN_VALUE || mood == null) {
            System.err.println("Invalid configuration");
            System.exit(2);
            return;
        }

        System.out.println("Hello, " + name + "!");
        System.out.println("You have " + count + " " + mood + " messages!");
        System.out.println("Thanks for viewing your messages");

        // Update values
        countNode.raw(0); // native type
        moodNode.set(Mood.class, Mood.NEUTRAL); // serialized type

        root.node("accesses").act(n -> {
            n.commentIfAbsent("The times messages have been accessed, in milliseconds since the epoch");
            n.appendListNode().set(System.currentTimeMillis());
        });

        // And save the node back to the file
        try {
            loader.save(root);
        } catch (final ConfigurateException e) {
            System.err.println("Unable to save your messages configuration! Sorry! " + e.getMessage());
            System.exit(1);
        }**/

    }

    enum Mood {

        HAPPY, SAD, CONFUSED, NEUTRAL;

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