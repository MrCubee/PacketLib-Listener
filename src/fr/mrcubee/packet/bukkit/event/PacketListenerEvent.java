package fr.mrcubee.packet.bukkit.event;

import fr.mrcubee.packet.bukkit.Packets;
import org.bukkit.event.Cancellable;

public abstract class PacketListenerEvent implements Cancellable {

    private final Packets packetType;
    private final Object rawPacket;
    private boolean cancelled;

    protected PacketListenerEvent(Packets packetType, Object rawPacket) {
        this.packetType = packetType;
        this.rawPacket = rawPacket;
        this.cancelled = false;
    }

    /**
     * @since 1.0
     * @return Returns the type of the packet.
     */
    public Packets getPacketType() {
        return this.packetType;
    }

    /**
     * @since 1.0
     * @return Return the raw packet.
     */
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
}
