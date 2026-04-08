package com.beanbeanjuice.simpleproxychat.yep;

import com.beanbeanjuice.simpleproxychat.shared.chat.ChatHandler;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

public class YepLibListener {

    public static final MinecraftChannelIdentifier YEP_GENERIC = MinecraftChannelIdentifier.create("yep", "generic");

    private final ChatHandler chatHandler;

    private static final String TYPE_SEPARATOR = "\u241E";  // Separates type from parameters
    private static final String PARAM_SEPARATOR = "\u241F"; // Separates individual parameters

    public YepLibListener(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(YEP_GENERIC)) return;
        if (!event.getResult().isAllowed()) return;
        if (!(event.getSource() instanceof ServerConnection)) return;

        ServerConnection source = (ServerConnection) event.getSource();
        byte[] data = event.getData();
        String message = new String(data, java.nio.charset.StandardCharsets.UTF_8);

        // First split on TYPE_SEPARATOR (U+241E) to separate type from parameters
        String[] typeSplit = message.split(TYPE_SEPARATOR, 2);
        if (typeSplit.length < 2) return;
        
        String type = typeSplit[0];
        String paramsString = typeSplit[1];
        
        // Now split parameters on PARAM_SEPARATOR (U+241F)
        String[] params = paramsString.split(PARAM_SEPARATOR);

        if (type.equals("yep:death")) {
            handleDeathMessage(params, source);
        } else if (type.equals("yep:advancement")) {
            handleAdvancementMessage(params, source);
        }
    }

    private void handleDeathMessage(String[] params, ServerConnection source) {
        // Format: playerName␟displayName␟deathMessage
        if (params.length < 3) return;

        String playerName = params[0];
        String displayName = params[1];
        String deathMessage = params[2];

        chatHandler.runProxyDeathMessage(playerName, deathMessage, source.getServerInfo().getName());
    }

    private void handleAdvancementMessage(String[] params, ServerConnection source) {
        // Format: playerName␟displayName␟frameType␟title␟description
        if (params.length < 5) return;

        String playerName = params[0];
        String displayName = params[1];
        String frameType = params[2];
        String title = params[3];
        String description = params[4];

        chatHandler.runProxyAdvancementMessage(playerName, frameType, title, description, source.getServerInfo().getName());
    }
}