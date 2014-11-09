package BenYehuda;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Egozy on 29/10/2014.
 */
public class BenYehudaParser {
    private ZipFile zip = null;
    private BenYehudaHandler handler = null;

    public BenYehudaParser(String zipName)
    {
        try {
            this.zip = new ZipFile(zipName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPageCallBack(BenYehudaHandler handler)
    {
        this.handler = handler;
    }

    /**
     * The main parse method.
     * @throws Exception
     */
    public void parse() throws Exception {
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory()) {
                BenYehudaPage page = getPublish(zip.getInputStream(entry));
                handler.process(page);
            }
        }
    }

    public static BenYehudaPage getPublish(InputStream stream) throws IOException {
        Document doc =  Jsoup.parse(stream,null,"");
        String[] title = doc.title().split("(/)|מאת");
        String topic = title[0].trim();
        String author = (title.length>1)?title[1].trim():null;
        String extra = (title.length>2)?title[2].trim():null;
        return new BenYehudaPage(topic,author,doc.text(),extra);
    }
}
