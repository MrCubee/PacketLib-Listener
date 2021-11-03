package fr.mrcubee.packet.bukkit;

import io.netty.channel.Channel;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PlayerChannel {


    private static Object invokeGetterMethod(Object object, String methodName) {
        Method method = null;
        Object result = null;

        if (object == null || methodName == null)
            return null;
        try {
            method = object.getClass().getMethod(methodName);
        } catch (NoSuchMethodException ignored) {}
        if (method == null)
            return null;
        try {
            result = method.invoke(object);
        } catch (Exception ignored) {}
        return result;
    }

    private static Object getFieldValue(Object object, String fieldName) {
        Field field = null;
        Object result = null;

        if (object == null || fieldName == null)
            return null;
        try {
            field = object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignored) {}
        if (field == null)
            return null;
        try {
            result = field.get(object);
        } catch (Exception ignored) {}
        return result;
    }

    public static Channel getPlayerChannel(Player player) {
        Object temp;

        if (player == null)
            return null;
        temp = invokeGetterMethod(player, "getHandle");
        temp = getFieldValue(temp, "playerConnection");
        temp = getFieldValue(temp, "networkManager");
        temp = getFieldValue(temp, "channel");
        return (Channel) temp;
    }

}
