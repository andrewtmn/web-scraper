import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class AmazonScraper {
    public static void scrapeBuyPrice() throws IOException {
        Document doc = Jsoup.connect("https://www.amazon.com.au/Lenovo-"
                + "Chromebook-MediaTek-Processor-81JW0000US/dp/B07GLV1VC7?ref_"
                + "=Oct_RAsinC_Ajax_4913311051_1&pf_rd_r=H4YWAEH6RM4J8CTWCFRN"
                + "&pf_rd_p=c3b9c102-0e2a-5d82-b3be-581a05a49424&pf_rd_s=merc"
                + "handised-search-10&pf_rd_t=101&pf_rd_i=4913311051&pf_rd_"
                + "m=ANEGB3WVEVKZB").get();

        String title;
        title = doc.title();
        System.out.println(title);

        Elements price = doc.select("#price_inside_buybox");
        System.out.println("The current price is " + price.html());
    }

    public static void main(String[] args) {
        try {
            scrapeBuyPrice();
        } catch (IOException ioe) {
            // hahahahah
        }
    }
}
