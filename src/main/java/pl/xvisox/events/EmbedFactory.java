package pl.xvisox.events;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;

public class EmbedFactory {

    public static WebhookEmbed wrongCommandEmbed() {
        return new WebhookEmbedBuilder()
                .setColor(0xFF0000)
                .setTitle(new WebhookEmbed.EmbedTitle("**WRONG COMMAND!**", null))
                .setDescription(".help for more info")
                .build();
    }

    public static WebhookEmbed lowestAskEmbed(String newPrice) {
        return new WebhookEmbedBuilder()
                .setColor(0xFF00EE)
                .setTitle(new WebhookEmbed.EmbedTitle("**Lowest ask to PLN**", null))
                .setDescription(newPrice)
                .build();
    }

}
