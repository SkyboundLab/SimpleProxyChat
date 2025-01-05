package com.beanbeanjuice.simpleproxychat.yep;

import cc.unilock.yeplib.api.event.YepAdvancementEvent;
import cc.unilock.yeplib.api.event.YepDeathEvent;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;

import com.beanbeanjuice.simpleproxychat.discord.Bot;
import com.beanbeanjuice.simpleproxychat.discord.DiscordChatHandler;

import com.beanbeanjuice.simpleproxychat.utility.epoch.EpochHelper;

import com.beanbeanjuice.simpleproxychat.utility.ISimpleProxyChat;

import com.beanbeanjuice.simpleproxychat.utility.config.Config;
import com.beanbeanjuice.simpleproxychat.utility.config.ConfigKey;
import com.beanbeanjuice.simpleproxychat.utility.config.Permission;

import java.util.logging.Logger;

public class YepListener {

  private final ISimpleProxyChat plugin;
  private final Config config;
  private final Bot discordBot;
  private final EpochHelper epochHelper;

  public YepListener(ISimpleProxyChat plugin, EpochHelper epochHelper) {
    this.plugin = plugin;
    this.config = plugin.getSPCConfig();
    this.discordBot = plugin.getDiscordBot();
    this.epochHelper = epochHelper;

    plugin.log("YepListener created");
  }

  public void register(ProxyServer server) {
    server.getEventManager().register(plugin, this);
  }

  @Subscribe
  public void onYepAdvancement(YepAdvancementEvent event) {
    this.plugin.log("onYepAdvancement");
    // if (config.serverDisabled(event.getSource().getServer().getServerInfo().getName())) return;

    var uuid = event.getPlayer().getUniqueId().toString();
    var server = event.getSource().getServer().getServerInfo().getName();

    // if (config.get(ConfigKey.MINECRAFT_DISCORD_ENABLED).asBoolean()) {
    //     if (config.get(ConfigKey.MINECRAFT_DISCORD_EMBED_USE).asBoolean()) {

    //         Color color = config.get(ConfigKey.MINECRAFT_DISCORD_EMBED_COLOR).asColor();

    //         EmbedBuilder embedBuilder = new EmbedBuilder()
    //                 .setAuthor(discordEmbedTitle, null, getPlayerHeadURL(chatMessageData.getPlayerUUID()))
    //                 .setDescription(discordEmbedMessage)
    //                 .setColor(color);

    //         if (config.get(ConfigKey.MINECRAFT_DISCORD_EMBED_USE_TIMESTAMP).asBoolean())
    //             embedBuilder.setTimestamp(epochHelper.getEpochInstant());

    //         discordBot.sendMessageEmbed(embedBuilder.build());
    //     } else {
    //         discordBot.sendMessage(discordMessage);
    //     }
    // }

    discordBot.sendMessage(
      String.format("**%s has made the advancement __%s__**\n_%s_",
        event.getUsername(),
        event.getTitle(),
        event.getDescription()
      )
    );
  }

  @Subscribe
  public void onYepDeath(YepDeathEvent event) {
    this.plugin.log("onYepDeath");
    // if (config.serverDisabled(event.getSource().getServer().getServerInfo().getName())) return;

    var uuid = event.getPlayer().getUniqueId().toString();
    var server = event.getSource().getServer().getServerInfo().getName();

    discordBot.sendMessage(
      String.format("**%s %s**",
        event.getUsername(),
        event.getMessage()
      )
    );
  }
}