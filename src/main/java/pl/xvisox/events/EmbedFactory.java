package pl.xvisox.events;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import pl.xvisox.tools.KlektOffers;
import pl.xvisox.tools.StockXOffers;
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

    public static WebhookEmbed allStockXOffersEmbed(StockXOffers stockXOffers, String size) {
        ArrayList<Offer> offers = stockXOffers.getLowestAsksOffers();
        WebhookEmbedBuilder builder = new WebhookEmbedBuilder();
        builder.setColor(0x00FFFF);
        builder.setTitle(new WebhookEmbed.EmbedTitle(stockXOffers.getTitle(), stockXOffers.getUrl()));

        String formattedPrice, formattedSize;
        for (var offer : offers) {
            double price = calculatePrice(Integer.parseInt(offer.getPrice()), Currency.USD) * getExchangeRateUSDtoPLN();
            formattedPrice = formatPrice(size, offer, df.format(price));
            formattedSize = offer.getSize();
            builder.addField(new WebhookEmbed.EmbedField(true, formattedSize, formattedPrice));
            if (offer.getSize().equals(size)) {
                String profit = "Potential profit for your size: " + getProfit(offer, stockXOffers.getRetail());
                builder.setFooter(new WebhookEmbed.EmbedFooter(profit, null));
            }
        }
        return builder.build();
    }

    public static WebhookEmbed allKlektOffersEmbed(KlektOffers klektOffers) {
        ArrayList<Offer> offers = klektOffers.getLowestAskOffers();
        WebhookEmbedBuilder builder = new WebhookEmbedBuilder();
        builder.setColor(0x00FFFF);
        builder.setTitle(new WebhookEmbed.EmbedTitle(klektOffers.getTitle(), klektOffers.getUrl()));

        String formattedPrice, formattedSize;
        for (var offer : offers) {
            double price = Integer.parseInt(offer.getPrice()) / 1.17 * getExchangeRateEURtoPLN();
            formattedPrice = formatPrice(null, offer, df.format(price));
            formattedSize = offer.getSize();
            builder.addField(new WebhookEmbed.EmbedField(true, formattedSize, formattedPrice));
        }
        return builder.build();
    }

    public static WebhookEmbed helpEmbed() {
        WebhookEmbedBuilder builder = new WebhookEmbedBuilder();
        builder.setColor(0x00FFFF);
        builder.setTitle(new WebhookEmbed.EmbedTitle("All available commands", null));

        String low, stockx, klekt, help;
        low = "converts lowest ask to PLN";
        stockx = "converts all lowest asks from given stockx url to PLN";
        klekt = "converts all lowest asks from given klekt url to PLN";
        help = "shows this help message";

        builder.addField(new WebhookEmbed.EmbedField(false, MessageListener.LOWEST_COMMAND, low));
        builder.addField(new WebhookEmbed.EmbedField(false, MessageListener.STOCKX_COMMAND, stockx));
        builder.addField(new WebhookEmbed.EmbedField(false, MessageListener.KLEKT_COMMAND, klekt));
        builder.addField(new WebhookEmbed.EmbedField(false, MessageListener.HELP_COMMAND, help));
        return builder.build();
    }

    private static String formatPrice(String size, Offer offer, String price) {
        return size != null && size.equals(offer.getSize()) ?
                "```**" + price + "**```" :
                "```" + price + "```";
    }

    private static String getProfit(Offer chosenOffer, String retail) {
        double profit = calculatePrice(Integer.parseInt(chosenOffer.getPrice()), Currency.USD) - Integer.parseInt(retail);
        return df.format(profit * getExchangeRateUSDtoPLN());
    }
}
