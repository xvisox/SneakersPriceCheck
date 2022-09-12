package pl.xvisox.tools;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import pl.xvisox.web.WebScraper;

import java.util.ArrayList;

public class StockXOffers {
    private final String url;
    private String retail;
    private String title;

    public StockXOffers(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getRetail() {
        return retail;
    }

    public ArrayList<Offer> getLowestAsksOffers() {
        HtmlPage page = new WebScraper(url).getPage();
        this.title = page.getTitleText();
        this.retail = scrapeRetail(page);
        String pageString = getOffersString(page);
        String pricePattern = "\"price\"";

        ArrayList<Offer> offers = new ArrayList<>();
        int i = pageString.indexOf(pricePattern);
        int nextOffer = 35;
        String price, size;
        while (i != -1) {
            price = pageString.substring(i, i + 15).replaceAll("[^\\d\\. ]", "");
            size = pageString.substring(i + 15, i + nextOffer).replaceAll("[^\\d\\. ]", "");
            offers.add(new Offer(price, size));
            i = pageString.indexOf(pricePattern, i + nextOffer);
        }
        return offers;

    }

    private String getOffersString(HtmlPage page) {
        String pageString = page.asXml();
        int SEARCH_LOWEST_FROM = pageString.indexOf("highPrice", 50000) + 10; // len(highPrice) + few chars
        return pageString.substring(SEARCH_LOWEST_FROM, SEARCH_LOWEST_FROM + 20000);
    }

    private String scrapeRetail(HtmlPage page) {
        String pageString = page.asXml();
        int START_FROM = pageString.indexOf("Retail Price", 150000);
        START_FROM = pageString.indexOf("$", START_FROM);
        return pageString.substring(START_FROM, START_FROM + 10).replaceAll("[^0-9]", "");
    }
}
