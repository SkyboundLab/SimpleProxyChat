package com.beanbeanjuice.simpleproxychat.utility;

import com.beanbeanjuice.simpleproxychat.utility.config.Config;
import com.beanbeanjuice.simpleproxychat.utility.config.ConfigDataKey;
import litebans.api.Database;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import nl.chimpgamer.networkmanager.api.NetworkManagerProvider;

import java.util.UUID;

public class Helper {

    /**
     * @see <a href="https://docs.advntr.dev/minimessage/format.html">Mini-Message Decorations</a>
     */
    public static String translateLegacyCodes(String string) {
        return string
                .replace("&0", convertToTag(NamedTextColor.BLACK.asHexString()))
                .replace("&1", convertToTag(NamedTextColor.DARK_BLUE.asHexString()))
                .replace("&2", convertToTag(NamedTextColor.DARK_GREEN.asHexString()))
                .replace("&3", convertToTag(NamedTextColor.DARK_AQUA.asHexString()))
                .replace("&4", convertToTag(NamedTextColor.DARK_RED.asHexString()))
                .replace("&5", convertToTag(NamedTextColor.DARK_PURPLE.asHexString()))
                .replace("&6", convertToTag(NamedTextColor.GOLD.asHexString()))
                .replace("&7", convertToTag(NamedTextColor.GRAY.asHexString()))
                .replace("&8", convertToTag(NamedTextColor.DARK_GRAY.asHexString()))
                .replace("&9", convertToTag(NamedTextColor.BLUE.asHexString()))
                .replace("&a", convertToTag(NamedTextColor.GREEN.asHexString()))
                .replace("&b", convertToTag(NamedTextColor.AQUA.asHexString()))
                .replace("&c", convertToTag(NamedTextColor.RED.asHexString()))
                .replace("&d", convertToTag(NamedTextColor.LIGHT_PURPLE.asHexString()))
                .replace("&e", convertToTag(NamedTextColor.YELLOW.asHexString()))
                .replace("&f", convertToTag(NamedTextColor.WHITE.asHexString()))
                .replace("&k", convertToTag("obfuscated"))
                .replace("&l", convertToTag("bold"))
                .replace("&m", convertToTag("strikethrough"))
                .replace("&n", convertToTag("underlined"))
                .replace("&o", convertToTag("italic"))
                .replace("&r", convertToTag("reset"))
                .replace("\\n", convertToTag("newline"))

                .replaceAll("&#([A-Fa-f0-9]{6})", "<#$1>");  // "&#FFC0CBHello! -> <#FFC0CB>Hello!
    }

    public static String convertAlias(Config config, String serverName) {
        String alias = config.getAsStringMap(ConfigDataKey.ALIASES).get(serverName);
        return (alias == null) ? serverName : alias;
    }

    private static String convertToTag(String string) {
        return "<" + string + ">";
    }

    public static String sanitize(String message) {
        return stripColor(MiniMessage.miniMessage().deserialize(message));
    }

    public static String stripColor(Component input) {
        return PlainTextComponentSerializer.plainText().serialize(input);
    }

    public static Component stringToComponent(String string) {
        return MiniMessage.miniMessage().deserialize(string);
    }

    public static boolean serverHasChatLocked(Config config, String serverName) {
        if (config.getAsBoolean(ConfigDataKey.NETWORKMANAGER_ENABLED) && NetworkManagerProvider.Companion.get().getChatManager().isChatLocked(serverName))
            return true;

        // TODO: Other methods of checking if chat is locked.
        return false;
    }

    public static boolean playerCanChat(Config config, UUID playerUUID) {
        if (config.getAsBoolean(ConfigDataKey.LITEBANS_ENABLED) && Database.get().isPlayerMuted(playerUUID, null))
            return false;

        // TODO: Other methods of checking if player can talk.
        return true;
    }

}
