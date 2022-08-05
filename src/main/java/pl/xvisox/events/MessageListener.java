package pl.xvisox.events;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import pl.xvisox.Config;
import pl.xvisox.tools.AllOffers;
import pl.xvisox.tools.Currency;

import static pl.xvisox.tools.PriceTool.*;

public class MessageListener extends ListenerAdapter {
    private static final String LOWEST_COMMAND = ".low";
    private static final String ALL_LOWEST_COMMAND = ".all";
    private final Webhook webhook;

    public MessageListener(Webhook webhook) {
        this.webhook = webhook;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isWebhookMessage()) return;

        String[] messageSent = event.getMessage().getContentRaw().split(" ");
        int length = messageSent.length;
        if (length < 1 || messageSent[0].charAt(0) != '.') return;

        String command = messageSent[0];
        WebhookMessageBuilder messBuilder = new WebhookMessageBuilder()
                .setUsername(Config.NAME)
                .setAvatarUrl(Config.AVATAR);
        if (lowestAskCommand(length, command)) {
            if (!StringUtils.isNumeric(messageSent[1])) return;

            int price = Integer.parseInt(messageSent[1]);
            String newPrice = df.format(getExchangeRateGBPtoPLN() * calculatePirce(price, Currency.GBP));
            sendEmbed(messBuilder, EmbedFactory.lowestAskEmbed(newPrice));
        } else if (allLowestAskCommand(length, command)) {
            String url = messageSent[1];
            String size = null;
            if (url.indexOf('?') != -1) {
                size = url.substring(url.indexOf('?') + 6).replaceAll("W", "");
                url = url.substring(0, url.indexOf('?'));
            }

            AllOffers allOffers = new AllOffers(url);
            sendEmbed(messBuilder, EmbedFactory.allLowestAskOffersEmbed(allOffers, size));
        } else {
            sendEmbed(messBuilder, EmbedFactory.wrongCommandEmbed());
        }
    }

    private boolean allLowestAskCommand(int length, String command) {
        return command.equalsIgnoreCase(ALL_LOWEST_COMMAND) && length == 2;
    }

    private boolean lowestAskCommand(int length, String command) {
        return command.equalsIgnoreCase(LOWEST_COMMAND) && length == 2;
    }

    private void sendEmbed(WebhookMessageBuilder builder, WebhookEmbed embed) {
        builder.addEmbeds(embed);
        webhook.getClient().send(builder.build());
    }
}
