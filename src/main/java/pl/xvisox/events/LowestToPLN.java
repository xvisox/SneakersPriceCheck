package pl.xvisox.events;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.xvisox.web.WebScraper;

import static pl.xvisox.tools.PriceTool.calculatePirce;
import static pl.xvisox.tools.PriceTool.df;

public class LowestToPLN extends ListenerAdapter {
    private static final String LOWEST_COMMAND = ".low";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] messageSent = event.getMessage().getContentRaw().split(" ");
        String command = messageSent[0];
        if (command.equalsIgnoreCase(LOWEST_COMMAND) && !event.getMember().getUser().isBot()) {
            int price = Integer.parseInt(messageSent[1]);
            String newPrice = df.format(getExchangeRate() * calculatePirce(price));
            event.getChannel().sendMessage(newPrice).queue();
        }
    }

    private double getExchangeRate() {
        int MAGIC_INT = 73000;
        int ANOTHER_MAGIC_INT = 30; // len(data-pair-exchange-value) + few chars
        String url = "https://www.walutomat.pl/kursy-walut/gbp-pln/";
        HtmlPage page = new WebScraper(url).getPage();

        int idx = page.asXml().indexOf("data-pair-exchange-value", MAGIC_INT) + ANOTHER_MAGIC_INT;
        String averageExchangeRate = page.asXml().substring(idx, idx + 30).replaceAll("\\s", "");
        return Double.parseDouble(averageExchangeRate.replace(',', '.'));
    }
}
