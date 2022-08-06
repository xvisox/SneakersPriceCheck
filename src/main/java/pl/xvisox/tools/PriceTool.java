package pl.xvisox.tools;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import pl.xvisox.web.WebScraper;

import java.text.DecimalFormat;

public abstract class PriceTool {
    public static final DecimalFormat df = new DecimalFormat("0.00");
    private static final double SHIPPING_GBP = 4.2;
    private static final double SHIPPING_USD = 5.2;
    private static double exchangeRateGBPtoPLN = -1;
    private static double exchangeRateUSDtoPLN = -1;

    public static int calculatePrice(int currentPrice, Currency currency) {
        double SHIPPING = currency == Currency.GBP ? SHIPPING_GBP : SHIPPING_USD;
        return (int) ((currentPrice / 1.05) * 0.88 - SHIPPING);
    }

    public static double getExchangeRateGBPtoPLN() {
        if (exchangeRateGBPtoPLN > 0) {
            return exchangeRateGBPtoPLN;
        }
        String averageExchangeRate = scrapeExchangeRateFromPage("https://www.walutomat.pl/kursy-walut/gbp-pln/");
        return (exchangeRateGBPtoPLN = Double.parseDouble(averageExchangeRate.replace(',', '.')));
    }

    public static double getExchangeRateUSDtoPLN() {
        if (exchangeRateUSDtoPLN > 0) {
            return exchangeRateUSDtoPLN;
        }
        String averageExchangeRate = scrapeExchangeRateFromPage("https://www.walutomat.pl/kursy-walut/usd-pln/");
        return (exchangeRateUSDtoPLN = Double.parseDouble(averageExchangeRate.replace(',', '.')));
    }

    private static String scrapeExchangeRateFromPage(String url) {
        HtmlPage page = new WebScraper(url).getPage();
        int ANOTHER_MAGIC_INT = 30; // len(data-pair-exchange-value) + few chars
        int MAGIC_INT = 73000;
        int idx = page.asXml().indexOf("data-pair-exchange-value", MAGIC_INT) + ANOTHER_MAGIC_INT;
        return page.asXml().substring(idx, idx + 30).replaceAll("\\s", "");
    }
}
