package com.code972.indexing.NewsArticles;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Egozy on 06/11/2014.
 */
public class NewsZipParser {
    private ZipFile zip;
    private NewsHandler handler;

    public NewsZipParser(String zipName, NewsHandler handler) throws IOException {
        this.zip = new ZipFile(zipName);
        this.handler = handler;
    }

    public NewsZipParser(File zip, NewsHandler handler) throws IOException {
        this.zip = new ZipFile(zip);
        this.handler = handler;
    }

    public void parse() throws IOException {
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory()) {
                NewsPage page = NewsParseJson.getNewsPageFromJson(zip.getInputStream(entry));
                handler.process(page);
            }
        }
        handler.finishRemaining();
    }

    public int getNumOfEntries() {
        return zip.size();
    }
}
