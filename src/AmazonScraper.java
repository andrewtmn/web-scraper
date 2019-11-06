import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.Properties;

/**
 * Basic application that gets the product buy price from a product page
 * and sends an e-mail if the price is below a threshold
 */
public class AmazonScraper {

    /**
     *
     * @param URL the url of the web page of the product on Amazon
     * @throws IOException when an error occurs trying to connect to the resource
     */
    public static void scrapeBuyPrice(String URL) throws IOException {
        Document doc = Jsoup.connect(URL).get();

        String title;
        title = doc.title();
        System.out.println(title);

        Elements price = doc.select("#price_inside_buybox");
        System.out.println("The current price is " + price.html());

        if (checkIfBuyable(price.html())) {
            sendNotification();
        }
    }

    // Checks if the price of the product is under a chosen threshold
    private static boolean checkIfBuyable(String price) {
        // remove the dollar sign so price can be parsed
        price = price.substring(1);
        double parsedPrice = Double.parseDouble(price);

        return (parsedPrice < 10000);
    }

    // sends the e-mail to the recipient
    private static void sendNotification() {
        String subject = "Amazon product price drop";
        String textContent = "You can now afford the item. The link's here ";
        String url = "https://www.amazon.com.au/Lenovo-"
                + "Chromebook-MediaTek-Processor-81JW0000US/dp/B07GLV1VC7?ref_"
                + "=Oct_RAsinC_Ajax_4913311051_1&pf_rd_r=H4YWAEH6RM4J8CTWCFRN"
                + "&pf_rd_p=c3b9c102-0e2a-5d82-b3be-581a05a49424&pf_rd_s=merc"
                + "handised-search-10&pf_rd_t=101&pf_rd_i=4913311051&pf_rd_"
                + "m=ANEGB3WVEVKZB";
        String sender = "andrewnguyenn229@gmail.com";
        String recipient = "andrewnguyenn229@gmail.com";
        String password = "";

        EmailSender.sendEmail(subject, textContent + url,
                sender, recipient, password);
    }

    public static void main(String[] args) {
        try {
            scrapeBuyPrice("https://www.amazon.com.au/Lenovo-"
                    + "Chromebook-MediaTek-Processor-81JW0000US/dp/B07GLV1VC7?ref_"
                    + "=Oct_RAsinC_Ajax_4913311051_1&pf_rd_r=H4YWAEH6RM4J8CTWCFRN"
                    + "&pf_rd_p=c3b9c102-0e2a-5d82-b3be-581a05a49424&pf_rd_s=merc"
                    + "handised-search-10&pf_rd_t=101&pf_rd_i=4913311051&pf_rd_"
                    + "m=ANEGB3WVEVKZB");
        } catch (IOException ioe) {
            // couldn't scrape
        }
    }
}

/**
 * Sends e-mail to a chosen recipient, from a given sender. Uses the Gmail
 * server.
 */
class EmailSender {

    /**
     * Sends an e-mail with the given subject and text content. E-mail is sent
     * from the given sender to the given recipient. The Gmail server is used
     * so password authentication is required.
     * @param subject subject of the email
     * @param textContent text content of the email
     * @param sender the sender of the email
     * @param recipient the recipient of the email
     * @param password the authentication password for the sender's email
     */
    public static void sendEmail(String subject, String textContent,
                                 String sender, String recipient,
                                 String password) {
        Properties properties = createProperties();

        // create a session
        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });

        try {

            Transport transport = session.getTransport();
            InternetAddress addressFrom = new InternetAddress(sender);

            MimeMessage message = new MimeMessage(session);

            // set the e-mail sender and recipient
            message.setSender(addressFrom);
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(recipient));

            // set the e-mail subject and text content
            message.setSubject(subject);
            message.setContent(textContent, "text/plain");

            transport.connect();
            Transport.send(message);
            transport.close();

            System.out.println("e-mail sent successfully...");

        } catch (MessagingException me) {
            System.out.println("Failed to send the e-mail");
            me.printStackTrace();
        }

    }

    // Sets the properties of the session for the e-mail send
    private static Properties createProperties() {
        Properties properties = new Properties();

        // SMTP stands for Simple Mail Transfer Protocol - it's a communication
        // protocol for electronic mail transmission.

        // set the transport protocol to be smtp
        properties.setProperty("mail.transport.protocol", "smtp");

        // from host to gmail server - tells JavaMail which server it has to
        // reach for the DNS name (DNS = Domain Name System)
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");

        properties.put("mail.smtp.socketFactory.port", "587");

        // Secure Sockets Layer (SSL) is a standard security technology for
        // establishing an encrypted link between a server and a client
        properties.put("mail.smtp.Factor.class", "javax.net.ssl.SSLSocketFactory");

        // STARTTLS is an email protocol command that tells an email server that
        // an email client, including an email client running in a web browser,
        // wants to turn an existing insecure connection into a secure one
        properties.put("mail.smtp.starttls.enable", "true");

        // smtp authentication is true so that client can log in using the
        // authentication mechanism supported by gmail
        properties.put("mail.smtp.auth", "true");

        // set smtp port - by default it would be port 25
        // 587 is for default unencrypted smtp
        properties.put("mail.smtp.port", "587");

        return properties;
    }


}

