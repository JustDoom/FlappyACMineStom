package com.justdoom.flappyanticheat.customevents;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;

public class ViolationResetEvent implements Event, CancellableEvent {
    private boolean isCancelled;

    public ViolationResetEvent() {
        super();
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}
