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
        int size = messageSent.length;
        if (size < 1 || messageSent[0].charAt(0) != '.') return;

        String command = messageSent[0];
        WebhookMessageBuilder messBuilder = new WebhookMessageBuilder()
                .setUsername(Config.NAME)
                .setAvatarUrl(Config.AVATAR);
        if (lowestAskCommand(size, command)) {
            if (!StringUtils.isNumeric(messageSent[1])) return;

            int price = Integer.parseInt(messageSent[1]);
            String newPrice = df.format(getExchangeRateGBPtoPLN() * calculatePirce(price, Currency.GBP));
            sendEmbed(messBuilder, EmbedFactory.lowestAskEmbed(newPrice));
        } else if (allLowestAskCommand(size, command)) {
            String url = messageSent[1];
            AllOffers allOffers = new AllOffers(url);
            sendEmbed(messBuilder, EmbedFactory.allLowestAskOffersEmbed(allOffers));
        } else {
            sendEmbed(messBuilder, EmbedFactory.wrongCommandEmbed());
        }
    }

    private boolean allLowestAskCommand(int size, String command) {
        return command.equalsIgnoreCase(ALL_LOWEST_COMMAND) && size == 2;
    }

    private boolean lowestAskCommand(int size, String command) {
        return command.equalsIgnoreCase(LOWEST_COMMAND) && size == 2;
    }

    private void sendEmbed(WebhookMessageBuilder builder, WebhookEmbed embed) {
        builder.addEmbeds(embed);
        webhook.getClient().send(builder.build());
    }
}
