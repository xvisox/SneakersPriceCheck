package pl.xvisox.events;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import pl.xvisox.Config;

public class Webhook {
    private final WebhookClient client;

    public Webhook() {
        WebhookClientBuilder builder = new WebhookClientBuilder(Config.URL); // or id, token
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Webhook-Thread");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        this.client = builder.build();
    }

    public WebhookClient getClient() {
        return client;
    }
}
