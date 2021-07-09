package com.justdoom.flappyanticheat.commands;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.utils.ColorUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

import static net.minestom.server.command.builder.arguments.ArgumentType.Word;

public class FlappyACCommand extends Command {

    public FlappyACCommand(){
        super("flappyanticheat", "flappyac", "fac");

        setCondition(Conditions::playerOnly);

        ArgumentWord operation = Word("operation").from("reload", "resetviolations", "alerts", "profile");

        //todo do proper tab completion depending on previous args

        addSyntax(this::execute, operation);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context){
        final String operation = context.get("operation");

        sender.sendMessage(operation);

        switch (operation){
            case "reload":
                final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                        .path(Path.of("./FlappyAnticheat/config.yml")) // Set where we will load and save to
                        .build();

                try {
                    FlappyAnticheat.getInstance().root = loader.load();
                } catch (IOException e) {
                    System.err.println("An error occurred while loading this configuration: " + e.getMessage());
                    if (e.getCause() != null) {
                        e.getCause().printStackTrace();
                    }
                    System.exit(1);
                    return;
                }
                sender.sendMessage(ColorUtil.translate(FlappyAnticheat.getInstance().root.node("prefix").getString() + FlappyAnticheat.getInstance().root.node("messages", "reload").getString()));

            case "resetviolations":
                //todo re-add violation reset for specific players
                /**if(args.length >= 2){
                    if(Bukkit.getPlayerExact(args[1]) != null) {
                        FlappyAnticheat.getInstance().violationHandler.clearViolations(Bukkit.getPlayerExact(args[1]));
                        for(Player p: MinecraftServer.getConnectionManager().getOnlinePlayers()){
                            if(p.hasPermission("flappyanticheat.alerts")){
                                p.sendMessage(ColorUtil.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-reset.player").replace("{player}", args[1])));
                            }
                        }
                        if(FlappyAnticheat.getInstance().getConfig().getBoolean("messages.flag-to-console")) {
                            Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-reset.player").replace("{player}", args[1])));
                        }
                    } else {
                        sender.sendMessage(ColorUtil.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-remove-invalid-player")));
                    }
                } else {**/
                    FlappyAnticheat.getInstance().violationHandler.clearAllViolations();
                    for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                        if (p.hasPermission("flappyanticheat.alerts")) {
                            p.sendMessage(ColorUtil.translate(FlappyAnticheat.getInstance().root.node("prefix").getString() + FlappyAnticheat.getInstance().root.node("messages", "violation-reset", "all").getString()));
                        }
                    }
                    if(FlappyAnticheat.getInstance().root.node("messages", "flag-to-console").getBoolean()) {
                        //todo MinecraftServer.getCommandManager().execute(MinecraftServer.getCommandManager().getConsoleSender(), ColorUtil.translate(FlappyAnticheat.getInstance().root.node("prefix").getString() + FlappyAnticheat.getInstance().root.node("messages", "violation-reset", "all").getString()));
                    }
                //}
            case "alerts":
                if(!FlappyAnticheat.getInstance().dataManager.alertsDisabled.contains(sender.asPlayer())){
                    FlappyAnticheat.getInstance().dataManager.disabledAlertsAdd(sender.asPlayer());
                    sender.sendMessage(ColorUtil.translate(FlappyAnticheat.getInstance().root.node("prefix").getString() + FlappyAnticheat.getInstance().root.node("messages", "alert-toggle", "disable").getString()));
                } else {
                    FlappyAnticheat.getInstance().dataManager.disabledAlertsRemove(sender.asPlayer());
                    sender.sendMessage(ColorUtil.translate(FlappyAnticheat.getInstance().root.node("prefix").getString() + FlappyAnticheat.getInstance().root.node("messages", "alert-toggle", "enable")));
                }

            case "profile":

        }
    }
}
