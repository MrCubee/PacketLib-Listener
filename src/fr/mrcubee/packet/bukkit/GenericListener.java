package fr.mrcubee.packet.bukkit;

import fr.mrcubee.packet.bukkit.event.PlayerPacketReceiveEvent;
import fr.mrcubee.packet.bukkit.event.PlayerPacketSendEvent;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

/**
 * @author MrCubee
 * @since 1.0
 */
public class GenericListener extends ChannelDuplexHandler {

    private final GenericListenerManager manager;
    private final Player player;

    protected GenericListener(GenericListenerManager manager, Player player) {
        this.manager = manager;
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object packetObj) throws Exception {
        Packets packetType = Packets.getFromPacketClass(packetObj.getClass());
        GenericInPacket genericInPacket;
        PlayerPacketReceiveEvent event;

        if (packetType == null || packetType.getDirection() != PacketDirection.IN) {
            super.channelRead(context, packetObj);
            return;
        }
        genericInPacket = (GenericInPacket) packetType.createPacket(packetObj);
        if (genericInPacket == null)
            event = PlayerPacketReceiveEvent.invokeEvent(getPlayer(), packetType, packetObj);
        else
            event = PlayerPacketReceiveEvent.invokeEvent(getPlayer(), genericInPacket, packetObj);
        if (event != null && event.isCancelled())
            return;
        super.channelRead(context, packetObj);
    }

    @Override
    public void write(ChannelHandlerContext context, Object packetObj, ChannelPromise promise) throws Exception {
        Packets packetType = Packets.getFromPacketClass(packetObj.getClass());
        GenericOutPacket genericOutPacket;
        PlayerPacketSendEvent event;

        if (packetType == null || packetType.getDirection() != PacketDirection.OUT) {
            super.write(context, packetObj, promise);
            return;
        }
        genericOutPacket = (GenericOutPacket) packetType.createPacket(packetObj);
        if (genericOutPacket == null)
            event = PlayerPacketSendEvent.invokeEvent(player, packetType, packetObj);
        else
            event = PlayerPacketSendEvent.invokeEvent(player, genericOutPacket, packetObj);
        if (event != null && event.isCancelled())
            return;
        super.write(context, packetObj, promise);
    }

    public GenericListenerManager getManager() {
        return this.manager;
    }

    public Player getPlayer() {
        return this.player;
    }
}
