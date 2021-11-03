package fr.mrcubee.packet.bukkit;

import fr.mrcubee.packet.bukkit.event.PacketReceiveEvent;
import fr.mrcubee.packet.bukkit.event.PacketSendEvent;

/**
 * @author MrCubee
 * @since 1.0
 * @version 1.0
 */
public interface PacketHandler {

    /** This method is called each time a packet is sent.
     * @since 1.0
     * @param event The event containing all the information of the packet.
     */
    public void onSend(PacketSendEvent event);

    /** This method is called each time a packet is received.
     * @since 1.0
     * @param event The event containing all the information of the packet.
     */
    public void onReceive(PacketReceiveEvent event);

}
