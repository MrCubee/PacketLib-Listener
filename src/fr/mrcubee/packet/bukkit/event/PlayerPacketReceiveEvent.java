package fr.mrcubee.packet.bukkit.event;

import fr.mrcubee.packet.bukkit.GenericInPacket;
import fr.mrcubee.packet.bukkit.GenericOutPacket;
import fr.mrcubee.packet.bukkit.Packets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerPacketReceiveEvent extends PacketListenerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player sender;
    private final GenericInPacket genericPacket;

    protected PlayerPacketReceiveEvent(Player sender, GenericInPacket genericPacket, Object rawPacket) {
        super(genericPacket.getPacket(), rawPacket);
        this.sender = sender;
        this.genericPacket = genericPacket;
    }

    protected PlayerPacketReceiveEvent(Player sender, Packets packetType, Object rawPacket) {
        super(packetType, rawPacket);
        this.sender = sender;
        this.genericPacket = null;
    }

    public Player getSender() {
        return this.sender;
    }

    public GenericInPacket getGenericPacket() {
        return this.genericPacket;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerPacketReceiveEvent.HANDLERS;
    }

    @Override
    public String getEventName() {
        return "PlayerPacketReceiveEvent";
    }

    public static HandlerList getHandlerList() {
        return PlayerPacketReceiveEvent.HANDLERS;
    }

    public static PlayerPacketReceiveEvent invokeEvent(Player sender, GenericInPacket genericPacket, Object rawPacket) {
        PlayerPacketReceiveEvent event;

        if (sender == null || genericPacket == null || rawPacket == null)
            return null;
        event = new PlayerPacketReceiveEvent(sender, genericPacket, rawPacket);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerPacketReceiveEvent invokeEvent(Player sender, Packets packetType, Object rawPacket) {
        PlayerPacketReceiveEvent event;

        if (sender == null || packetType == null || rawPacket == null)
            return null;
        event = new PlayerPacketReceiveEvent(sender, packetType, rawPacket);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}
