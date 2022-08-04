package pl.xvisox.events;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.xvisox.Config;

import static pl.xvisox.tools.PriceTool.*;

public class MessageListener extends ListenerAdapter {
    private static final String LOWEST_COMMAND = ".low";
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
            int price = Integer.parseInt(messageSent[1]);
            String newPrice = df.format(getExchangeRate() * calculatePirce(price));
            sendEmbed(messBuilder, EmbedFactory.lowestAskEmbed(newPrice));
        } else {
            sendEmbed(messBuilder, EmbedFactory.wrongCommandEmbed());
        }
    }

    private boolean lowestAskCommand(int size, String command) {
        return command.equalsIgnoreCase(LOWEST_COMMAND) && size == 2;
    }

    private void sendEmbed(WebhookMessageBuilder builder, WebhookEmbed embed) {
        builder.addEmbeds(embed);
        webhook.getClient().send(builder.build());
    }
}
