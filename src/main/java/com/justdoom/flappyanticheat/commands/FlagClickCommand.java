package com.justdoom.flappyanticheat.commands;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.condition.Conditions;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.serialize.SerializationException;

import static net.minestom.server.command.builder.arguments.ArgumentType.Word;

public class FlagClickCommand extends Command {

    public FlagClickCommand(){
        super("flappyacflagclick");

        setCondition(Conditions::playerOnly);

        addSyntax(this::execute);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context){
        try {
            for (String commands : FlappyAnticheat.getInstance().root.node("messages", "commands").getList(String.class)) {
                MinecraftServer.getCommandManager().execute(MinecraftServer.getCommandManager().getConsoleSender(),
                        commands.replace("{player", sender.asPlayer().getUsername()));
            }
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }
}
