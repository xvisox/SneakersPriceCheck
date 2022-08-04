package pl.xvisox.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class WebScraper {
    private final WebClient webClient = new WebClient();
    private HtmlPage page;

    public WebScraper(String url) {
        try {
            page = getWebPage(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HtmlPage getWebPage(String url) throws IOException {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        return webClient.getPage(url);
    }

    public HtmlPage getPage() {
        return page;
    }
}
