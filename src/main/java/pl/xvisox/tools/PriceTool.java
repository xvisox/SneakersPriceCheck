package pl.xvisox.tools;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import pl.xvisox.web.WebScraper;

import java.text.DecimalFormat;

public abstract class PriceTool {
    private static final double SHIPPING = 4.2;
    public static final DecimalFormat df = new DecimalFormat("0.00");

    public static int calculatePirce(int currentPrice) {
        return (int) ((currentPrice / 1.05) * 0.88 - SHIPPING);
    }

    public static double getExchangeRate() {
        int MAGIC_INT = 73000;
        int ANOTHER_MAGIC_INT = 30; // len(data-pair-exchange-value) + few chars
        String url = "https://www.walutomat.pl/kursy-walut/gbp-pln/";
        HtmlPage page = new WebScraper(url).getPage();

        int idx = page.asXml().indexOf("data-pair-exchange-value", MAGIC_INT) + ANOTHER_MAGIC_INT;
        String averageExchangeRate = page.asXml().substring(idx, idx + 30).replaceAll("\\s", "");
        return Double.parseDouble(averageExchangeRate.replace(',', '.'));
    }
}
