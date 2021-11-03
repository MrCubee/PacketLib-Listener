package fr.mrcubee.packet.bukkit.event;

import fr.mrcubee.packet.bukkit.Packets;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class PacketListenerEvent extends Event implements Cancellable {

    private final Packets packetType;
    private final Object rawPacket;
    private boolean cancelled;

    protected PacketListenerEvent(Packets packetType, Object rawPacket) {
        this.packetType = packetType;
        this.rawPacket = rawPacket;
        this.cancelled = false;
    }

    public Packets getPacketType() {
        return this.packetType;
    }

    public Object getRawPacket() {
        return this.rawPacket;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public abstract HandlerList getHandlers();

    @Override
    public abstract String getEventName();
}
