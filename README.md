# Java-Projects

##### Web Scraper

Get started on web scraping by downloading the jsoup library from https://jsoup.org/download.

Send emails using the JavaMail library from https://javaee.github.io/javamail/. If you're 
using Java 11 onwards, you will also need to download and import the javax.activation module 
since it has been removed. If you're using Java 9 or 10 then the module has been deprecated 
and you'll need to add the module at runtime.
 e.g. "--add-modules java.activation"

In intellij, you can simply copy the jsoup jar files into a folder in the project, right click
on the folder and click "Add as library". You can choose whether the library is at project, 
module or global level. Do the same for JavaMail and javax.activation.

Have a look at SimpleWebScraper.java for some of the basics in using jsoup. Reference link is
 https://www.htmlgoodies.com/html5/other/web-page-scraping-with-jsoup.html
 
 

