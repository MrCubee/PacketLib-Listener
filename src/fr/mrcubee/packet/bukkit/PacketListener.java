package fr.mrcubee.packet.bukkit;

import fr.mrcubee.packet.bukkit.event.PacketReceiveEvent;
import fr.mrcubee.packet.bukkit.event.PacketSendEvent;
import fr.mrcubee.weak.WeakHashSet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author MrCubee
 * @since 1.0
 * @version 1.0
 */
public class PacketListener extends ChannelDuplexHandler {

    private static final Map<PacketHandlerPriority, Set<PacketHandler>> HANDLERS = new HashMap<PacketHandlerPriority, Set<PacketHandler>>();
    private static final String HANDLER_NAME = "MrCubee_";

    private final Player player;

    private PacketListener(Player player) {
        this.player = player;
    }

    /**
     * @since 1.0
     * @return Returns the player who owns this listener.
     */
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object packetObj) throws Exception {
        Packets packetType = Packets.getFromPacketClass(packetObj.getClass());
        GenericInPacket genericInPacket;
        PacketReceiveEvent event;

        if (packetType == null || packetType.getDirection() != PacketDirection.IN) {
            super.channelRead(context, packetObj);
            return;
        }
        genericInPacket = (GenericInPacket) packetType.createPacket(packetObj);
        if (genericInPacket == null)
            event = PacketReceiveEvent.invokeEvent(getPlayer(), packetType, packetObj);
        else
            event = PacketReceiveEvent.invokeEvent(getPlayer(), genericInPacket, packetObj);
        if (event != null && event.isCancelled())
            return;
        super.channelRead(context, packetObj);
    }

    @Override
    public void write(ChannelHandlerContext context, Object packetObj, ChannelPromise promise) throws Exception {
        Packets packetType = Packets.getFromPacketClass(packetObj.getClass());
        GenericOutPacket genericOutPacket;
        PacketSendEvent event;

        if (packetType == null || packetType.getDirection() != PacketDirection.OUT) {
            super.write(context, packetObj, promise);
            return;
        }
        genericOutPacket = (GenericOutPacket) packetType.createPacket(packetObj);
        if (genericOutPacket == null)
            event = PacketSendEvent.invokeEvent(player, packetType, packetObj);
        else
            event = PacketSendEvent.invokeEvent(player, genericOutPacket, packetObj);
        if (event != null && event.isCancelled())
            return;
        super.write(context, packetObj, promise);
    }

    /** Unregister the packet handler.
     * @since 1.0
     * @param packetHandler The targeted packet handler.
     */
    public static void unRegisterPacketHandler(PacketHandler packetHandler) {
        Set<PacketHandler> handlers;

        if (packetHandler == null)
            return;
        for (PacketHandlerPriority priority : PacketHandlerPriority.values()) {
            handlers = PacketListener.HANDLERS.get(priority);
            if (handlers != null) {
                handlers.remove(packetHandler);
                if (handlers.isEmpty())
                    PacketListener.HANDLERS.remove(priority);
            }
        }
    }

    /** Register the packet handler.
     * @since 1.0
     * @param priority The modification priority of the packet handler.
     * @param packetHandler The targeted packet handler.
     */
    public static void registerPacketHandler(PacketHandlerPriority priority, PacketHandler packetHandler) {
        Set<PacketHandler> handlers;

        if (priority == null || packetHandler == null)
            return;
        unRegisterPacketHandler(packetHandler);
        handlers = PacketListener.HANDLERS.get(priority);
        if (handlers == null) {
            handlers = new WeakHashSet<PacketHandler>();
            PacketListener.HANDLERS.put(priority, handlers);
        }
        handlers.add(packetHandler);
    }

    /** Register the packet handler with normal priority.
     * @since 1.0
     * @param packetHandler The targeted packet handler.
     */
    public static void registerPacketHandler(PacketHandler packetHandler) {
        registerPacketHandler(PacketHandlerPriority.NORMAL, packetHandler);
    }

    /** Unregister all registered packet handlers.
     * @since 1.0
     */
    public static void clearHandlers() {
        PacketListener.HANDLERS.clear();
    }

    /** Add listener to the player's connection.
     * @since 1.0
     * @param player The targeted player.
     */
    public static void addPlayer(Player player) {
        Channel channel;

        if (player == null)
            return;
        channel = PlayerConnection.getPlayerChannel(player);
        if (channel == null) {
            return;
        }
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(PacketListener.HANDLER_NAME + player.getName());
        });
        channel.eventLoop().submit(() -> {
            channel.pipeline().addBefore("packet_handler", PacketListener.HANDLER_NAME + player.getName(), new PacketListener(player));
        });
    }

    /** Remove listener from the player's connection.
     * @since 1.0
     * @param player The targeted player.
     */
    public static void removePlayer(Player player) {
        Channel channel;

        if (player == null)
            return;
        channel = PlayerConnection.getPlayerChannel(player);
        if (channel == null)
            return;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(PacketListener.HANDLER_NAME + player.getName());
        });
    }

    /** Sends the event to the packet handlers of a priority.
     * @since 1.0
     * @param handlers The packet handlers of a priority.
     * @param event The desired event.
     */
    private static void onSendEvent(Set<PacketHandler> handlers, PacketSendEvent event) {
        if (handlers == null || event == null)
            return;
        for (PacketHandler packetHandler : handlers)
            packetHandler.onSend(event);
    }

    /** Sends the event to the packet handlers of a priority.
     * @since 1.0
     * @param handlers The packet handlers of a priority.
     * @param event The desired event.
     */
    private static void onReceiveEvent(Set<PacketHandler> handlers, PacketReceiveEvent event) {
        if (handlers == null || event == null)
            return;
        for (PacketHandler packetHandler : handlers)
            packetHandler.onReceive(event);
    }

    /** Sends the event to all packet handlers in priority order.
     * @since 1.0
     * @param event The desired event.
     */
    public static void callEvent(PacketSendEvent event) {
        if (event == null)
            return;
        for (PacketHandlerPriority priority : PacketHandlerPriority.values())
            onSendEvent(PacketListener.HANDLERS.get(priority), event);
    }

    /** Sends the event to all packet handlers in priority order.
     * @since 1.0
     * @param event The desired event.
     */
    public static void callEvent(PacketReceiveEvent event) {
        if (event == null)
            return;
        for (PacketHandlerPriority priority : PacketHandlerPriority.values())
            onReceiveEvent(PacketListener.HANDLERS.get(priority), event);
    }
}