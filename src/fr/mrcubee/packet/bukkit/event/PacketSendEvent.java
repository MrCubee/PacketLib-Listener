package fr.mrcubee.packet.bukkit.event;

import fr.mrcubee.packet.bukkit.GenericOutPacket;
import fr.mrcubee.packet.bukkit.PacketListener;
import fr.mrcubee.packet.bukkit.Packets;
import org.bukkit.entity.Player;

public class PacketSendEvent extends PacketListenerEvent {

    private final Player receiver;
    private final GenericOutPacket genericPacket;

    protected PacketSendEvent(Player receiver, GenericOutPacket genericPacket, Object rawPacket) {
        super(genericPacket.getPacket(), rawPacket);
        this.receiver = receiver;
        this.genericPacket = genericPacket;
    }

    protected PacketSendEvent(Player receiver, Packets packetType, Object rawPacket) {
        super(packetType, rawPacket);
        this.receiver = receiver;
        this.genericPacket = null;
    }

    /**
     * @since 1.0
     * @return Returns the player who receives the packet.
     */
    public Player getReceiver() {
        return this.receiver;
    }

    /**
     * @since 1.0
     * @return Returns the package in generic form if it exists otherwise returns null.
     */
    public GenericOutPacket getGenericPacket() {
        return this.genericPacket;
    }

    /** Creates an instance of the event and sends it to all registered packet handlers.
     * @since 1.0
     * @param receiver The player who receives the packet.
     * @param genericPacket The packet in generic form.
     * @param rawPacket The raw packet.
     * @return Returns the event.
     */
    public static PacketSendEvent invokeEvent(Player receiver, GenericOutPacket genericPacket, Object rawPacket) {
        PacketSendEvent event;

        if (receiver == null || genericPacket == null || rawPacket == null)
            return null;
        event = new PacketSendEvent(receiver, genericPacket, rawPacket);
        PacketListener.callEvent(event);
        return event;
    }

    /** Creates an instance of the event and sends it to all registered packet handlers.
     * @since 1.0
     * @param receiver The player who receives the packet.
     * @param packetType The packet type.
     * @param rawPacket The raw packet.
     * @return Returns the event.
     */
    public static PacketSendEvent invokeEvent(Player receiver, Packets packetType, Object rawPacket) {
        PacketSendEvent event;

        if (receiver == null || packetType == null || rawPacket == null)
            return null;
        event = new PacketSendEvent(receiver, packetType, rawPacket);
        PacketListener.callEvent(event);
        return event;
    }
}
