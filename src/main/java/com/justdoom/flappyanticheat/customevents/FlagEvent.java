package com.justdoom.flappyanticheat.customevents;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;

public class FlagEvent implements Event, CancellableEvent {

    private final Player player;
    private boolean isCancelled, isPunishable;
    private Check check;
    private int vl;

    public FlagEvent(Player player, Check check) {
        this.player = player;
        this.isCancelled = false;
        this.check = check;
        this.isPunishable = FlappyAnticheat.getInstance().getConfig().getBoolean("checks." + check.check + "." + check.checkType + ".punishable");
        this.vl = FlappyAnticheat.getInstance().violationHandler.getViolations(check, player);
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Player getFlaggedPlayer() {
        return this.player;
    }

    public Check getCheck(){
        return this.check;
    }

    public boolean isPunishable(){
        return this.isPunishable;
    }


    public int getViolations(){
        return this.vl;
    }
}
