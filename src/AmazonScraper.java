import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.Properties;

public class AmazonScraper {
    public static void scrapeBuyPrice(String URL) throws IOException {
        Document doc = Jsoup.connect(URL).get();

        String title;
        title = doc.title();
        System.out.println(title);

        Elements price = doc.select("#price_inside_buybox");
        System.out.println("The current price is " + price.html());

        checkIfBuyable(price.html());
    }

    private static void checkIfBuyable(String price) {
        // remove the dollar sign so price can be parsed
        price = price.substring(1);
        double parsedPrice = Double.parseDouble(price);

        if (parsedPrice < 10000) {
           EmailSender.sendEmail();
        }
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

class EmailSender {

    public static void sendEmail() {

        String from = "andrewnguyenn229@gmail.com";
        String to = "andrewnguyenn229@gmail.com";
        String pass = "";

        Properties properties = createProperties();

        // create a session
        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        try {

            Transport transport = session.getTransport();
            InternetAddress addressFrom = new InternetAddress(from);

            MimeMessage message = new MimeMessage(session);

            // set the e-mail sender and recipient
            message.setSender(addressFrom);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // set the e-mail subject and text content
            message.setSubject("The price on Amazon has dropped");
            message.setContent("Check out the item, its price has been reduced", "text/plain");

            transport.connect();
            Transport.send(message);
            transport.close();

            System.out.println("e-mail sent successfully...");

        } catch (MessagingException me) {
            System.out.println("Failed to send the e-mail");
            me.printStackTrace();
        }

    }

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

