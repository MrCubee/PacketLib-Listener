package fr.mrcubee.packet.bukkit.event;

import fr.mrcubee.packet.bukkit.GenericInPacket;
import fr.mrcubee.packet.bukkit.PacketListener;
import fr.mrcubee.packet.bukkit.Packets;
import org.bukkit.entity.Player;

public class PacketReceiveEvent extends PacketListenerEvent {

    private final Player sender;
    private final GenericInPacket genericPacket;

    protected PacketReceiveEvent(Player sender, GenericInPacket genericPacket, Object rawPacket) {
        super(genericPacket.getPacket(), rawPacket);
        this.sender = sender;
        this.genericPacket = genericPacket;
    }

    protected PacketReceiveEvent(Player sender, Packets packetType, Object rawPacket) {
        super(packetType, rawPacket);
        this.sender = sender;
        this.genericPacket = null;
    }

    /**
     * @since 1.0
     * @return Returns the player who sent the packet.
     */
    public Player getSender() {
        return this.sender;
    }

    /**
     * @since 1.0
     * @return Returns the package in generic form if it exists otherwise returns null.
     */
    public GenericInPacket getGenericPacket() {
        return this.genericPacket;
    }

    /** Creates an instance of the event and sends it to all registered packet handlers.
     * @since 1.0
     * @param sender The player who sends the packet.
     * @param genericPacket The packet in generic form.
     * @param rawPacket The raw packet.
     * @return Returns the event.
     */
    public static PacketReceiveEvent invokeEvent(Player sender, GenericInPacket genericPacket, Object rawPacket) {
        PacketReceiveEvent event;

        if (sender == null || genericPacket == null || rawPacket == null)
            return null;
        event = new PacketReceiveEvent(sender, genericPacket, rawPacket);
        PacketListener.callEvent(event);
        return event;
    }

    /** Creates an instance of the event and sends it to all registered packet handlers.
     * @since 1.0
     * @param sender The player who sends the packet
     * @param packetType The packet type.
     * @param rawPacket The raw packet.
     * @return Returns the event.
     */
    public static PacketReceiveEvent invokeEvent(Player sender, Packets packetType, Object rawPacket) {
        PacketReceiveEvent event;

        if (sender == null || packetType == null || rawPacket == null)
            return null;
        event = new PacketReceiveEvent(sender, packetType, rawPacket);
        PacketListener.callEvent(event);
        return event;
    }
}
