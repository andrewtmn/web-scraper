import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleWebScraper {

    public static void pageFetcher() throws IOException {

        /* Here, I've created a connection to the resource (i.e. the web
        page). Then the get() method is called to retrieve the page content.*/
        Document doc = Jsoup.connect("https://www.amazon.com/NPKC-"
                + "Customized-Suitable-Switches-Mechanical/dp/B06XWWRRXT/"
                + "ref=sr_1_8?keywords=keyboard+caps&qid=1572181759&sr=8-8")
                .get();

        // Here's a manual way to get the document out of the resource.
        URL url = new URL("http://google.com/");
        // connect to the URL
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        // Now we're gonna read the file line by line and append it to our
        // StringBuilder
        String line = null;
        StringBuilder tmp = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                urlConn.getInputStream()));
        while ((line = in.readLine()) != null) {
            tmp.append(line);
        }
        in.close();

        // now let Jsoup parse the String that we've read
        Document doc2 = Jsoup.parse(tmp.toString());
    }

    public static void main(String[] args) {
        try {
            pageFetcher();
        } catch (IOException ioe) {
            System.out.println("something went wrong");
        }
    }
}
