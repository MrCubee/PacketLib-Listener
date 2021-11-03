package fr.mrcubee.packet.bukkit.event;

import fr.mrcubee.packet.bukkit.GenericOutPacket;
import fr.mrcubee.packet.bukkit.Packets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerPacketSendEvent extends PacketListenerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player receiver;
    private final GenericOutPacket genericPacket;

    protected PlayerPacketSendEvent(Player receiver, GenericOutPacket genericPacket, Object rawPacket) {
        super(genericPacket.getPacket(), rawPacket);
        this.receiver = receiver;
        this.genericPacket = genericPacket;
    }

    protected PlayerPacketSendEvent(Player receiver, Packets packetType, Object rawPacket) {
        super(packetType, rawPacket);
        this.receiver = receiver;
        this.genericPacket = null;
    }

    public Player getReceiver() {
        return this.receiver;
    }

    public GenericOutPacket getGenericPacket() {
        return this.genericPacket;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerPacketSendEvent.HANDLERS;
    }

    @Override
    public String getEventName() {
        return "PlayerPacketReceiveEvent";
    }

    public static HandlerList getHandlerList() {
        return PlayerPacketSendEvent.HANDLERS;
    }

    public static PlayerPacketSendEvent invokeEvent(Player receiver, GenericOutPacket genericPacket, Object rawPacket) {
        PlayerPacketSendEvent event;

        if (receiver == null || genericPacket == null || rawPacket == null)
            return null;
        event = new PlayerPacketSendEvent(receiver, genericPacket, rawPacket);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerPacketSendEvent invokeEvent(Player receiver, Packets packetType, Object rawPacket) {
        PlayerPacketSendEvent event;

        if (receiver == null || packetType == null || rawPacket == null)
            return null;
        event = new PlayerPacketSendEvent(receiver, packetType, rawPacket);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}
