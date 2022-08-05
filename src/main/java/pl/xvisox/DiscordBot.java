package pl.xvisox;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import pl.xvisox.events.MessageListener;
import pl.xvisox.events.Webhook;
import pl.xvisox.web.WebScraper;

import javax.security.auth.login.LoginException;

public class DiscordBot {

    public static void main(String[] args) throws LoginException {
        Webhook webhook = new Webhook();
        JDA bot = JDABuilder.createLight(Config.TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new MessageListener(webhook))
                .build();
        WebScraper webScraper = new WebScraper("https://stockx.com/air-jordan-1-retro-low-og-unc");
        System.out.println(webScraper.getPage().asXml());
    }
}
