import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

/**
 * Created by Egozy on 29/10/2014.
 */
public class BenYehudaParser {
    public static Publish getPublish(String htmlName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(htmlName));
        Document doc =  Jsoup.parse(new File(htmlName), null);
        String[] title = doc.title().split("(/)|מאת");
        String topic = title[0].trim();
        String author = (title.length>1)?title[1].trim():null;
        String extra = (title.length>2)?title[2].trim():null;
        System.out.println(topic);
        System.out.println(author);
        System.out.println(extra);
        return new Publish(topic,author,doc.text(),extra);
    }
}
