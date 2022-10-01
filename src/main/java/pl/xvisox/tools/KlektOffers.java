package pl.xvisox.tools;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import pl.xvisox.web.WebScraper;

import java.util.ArrayList;
import java.util.Collections;

public class KlektOffers {
    private final String url;
    private String retail;
    private String title;

    public KlektOffers(String url) {
        this.url = url;
    }

    public String getRetail() {
        return retail;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Offer> getLowestAskOffers() {
        HtmlPage page = new WebScraper(url).getPage();
        this.retail = "NULL";
        this.title = page.getTitleText();
        int i = page.asXml().indexOf("highPrice");
        String pageString = page.asXml().substring(i, i + 6000);
        String pricePattern = "\"price\"";
        String sizePattern = "\"size\"";

        ArrayList<Offer> offers = new ArrayList<>();
        String price, size;
        i = 0;
        while (true) {
            i = pageString.indexOf(pricePattern, i);
            if (i == -1) break;
            price = pageString.substring(i, i + 15).replaceAll("[^\\d\\. ]", "");
            i = pageString.indexOf(sizePattern, i + 15);
            if (i == -1) break;
            size = pageString.substring(i, i + 15).replaceAll("[^\\d\\. ]", "");
            offers.add(new Offer(price, size));
        }
        offers.sort(Collections.reverseOrder());
        while (offers.get(0).getSize().charAt(0) == '1') {
            offers.add(offers.get(0));
            offers.remove(0);
        }
        return offers;
    }
}
