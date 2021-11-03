package fr.mrcubee.packet.bukkit;

import io.netty.channel.Channel;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.WeakHashMap;

public class GenericListenerManager {

    private final String name;
    private final Map<Player, GenericListener> listeners;

    protected GenericListenerManager(String name) {
        this.name = name;
        this.listeners = new WeakHashMap<Player, GenericListener>();
    }

    public boolean containPlayer(Player player) {
        return player != null && this.listeners.containsKey(player);
    }

    private void removeHandler(Channel channel, Player player) {
        if (channel == null || player == null)
            return;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(this.name + player.getName());
        });
    }

    public boolean addPlayer(Player player) {
        Channel channel;
        GenericListener genericListener;

        if (player == null || this.listeners.containsKey(player))
            return false;
        channel = PlayerChannel.getPlayerChannel(player);
        if (channel == null)
            return false;
        genericListener = new GenericListener(this, player);
        this.listeners.put(player, genericListener);
        removeHandler(channel, player);
        channel.pipeline().addBefore("packet_handler", this.name + player.getName(), genericListener);
        return true;
    }

    public boolean removePlayer(Player player) {
        Channel channel;
        GenericListener genericListener;

        if (player == null || !this.listeners.containsKey(player))
            return false;
        channel = PlayerChannel.getPlayerChannel(player);
        this.listeners.remove(player);
        removeHandler(channel, player);
        return true;
    }

    public static GenericListenerManager create(String name) {
        return new GenericListenerManager(name);
    }
}
