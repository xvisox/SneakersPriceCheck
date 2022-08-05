package pl.xvisox.events;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import pl.xvisox.tools.AllOffers;
import pl.xvisox.tools.Currency;
import pl.xvisox.tools.Offer;

import java.util.ArrayList;

import static pl.xvisox.tools.PriceTool.*;

public abstract class EmbedFactory {

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

    public static WebhookEmbed allLowestAskOffersEmbed(AllOffers allOffers, String size) {
        ArrayList<Offer> offers = allOffers.getLowestAsksOffers();
        WebhookEmbedBuilder builder = new WebhookEmbedBuilder();
        builder.setColor(0x00FFFF);
        builder.setTitle(new WebhookEmbed.EmbedTitle(allOffers.getName(), allOffers.getUrl()));

        String formattedPrice, formattedSize;
        for (var offer : offers) {
            double price = calculatePirce(Integer.parseInt(offer.getPrice()), Currency.USD) * getExchangeRateUSDtoPLN();
            formattedPrice = formatPrice(size, offer, df.format(price));
            formattedSize = offer.getSize();
            builder.addField(new WebhookEmbed.EmbedField(true, formattedSize, formattedPrice));
        }
        return builder.build();
    }

    private static String formatPrice(String size, Offer offer, String price) {
        return size != null && size.equals(offer.getSize()) ?
                "```**" + price + "**```" :
                "```" + price + "```";
    }
}
