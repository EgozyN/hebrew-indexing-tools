package com.code972.indexing.BenYehuda;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    public BenYehudaParser(String zipName) {
        try {
            this.zip = new ZipFile(zipName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPageCallBack(BenYehudaHandler handler) {
        this.handler = handler;
    }

    /**
     * The main parse method.
     *
     * @throws Exception
     */
    public void parse() throws Exception {
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory()) {
                BenYehudaPage page = getPublish(zip.getInputStream(entry));
                handler.process(page);
            }
        }
        handler.finishRemaining();
    }

    // known ids to ignore: donate-banner, proof-form-wrap, recommend-form-wrap
    // known classes to ignore: proof-panel, recommend-panel
    // other classes: MsoNormal, msocomtxt, Section1-4, WordSection1-3
    public static BenYehudaPage getPublish(InputStream stream) throws IOException {
        Document doc = Jsoup.parse(stream, null, "");
        String[] title = doc.title().split("(/)|מאת");
        String topic = title[0].trim();
        String author = (title.length > 1) ? title[1].trim() : null;
        String extra = (title.length > 2) ? title[2].trim() : null;
        Elements divs = doc.select("div");
        divs = divs.not("#donate-banner,.proof-panel,#proof-form-wrap,.recommend-panel,#recommend-form-wrap");
        return new BenYehudaPage(topic, author, divs.text(), extra);
    }
}
