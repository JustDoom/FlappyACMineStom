package com.justdoom.flappyanticheat.customevents;

import com.justdoom.flappyanticheat.checks.Check;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;

public class PunishEvent implements Event, CancellableEvent {

    private final Player player;
    private boolean isCancelled;
    private Check check;

    public PunishEvent(Player player, Check check) {
        super();
        this.player = player;
        this.isCancelled = false;
        this.check = check;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Player getPunishedPlayer() {
        return this.player;
    }

    public Check getCheck(){
        return this.check;
    }
}
