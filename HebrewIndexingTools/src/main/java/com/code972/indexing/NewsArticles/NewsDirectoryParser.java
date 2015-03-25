package com.code972.indexing.NewsArticles;

import java.io.File;
import java.io.IOException;

/**
 * Created by Egozy on 06/11/2014.
 */
public class NewsDirectoryParser {
    private File dir;
    private NewsHandler handler;
    private int totalFiles;

    public int getTotalFiles() {
        return totalFiles;

    }

    public NewsDirectoryParser(String dirName, NewsHandler handler) {
        dir = new File(dirName);
        if (!dir.isDirectory()) {
            //TODO: exception?
        }
        this.handler = handler;
        totalFiles = 0;
    }

    public void parse() throws IOException {
        for (File file : dir.listFiles()) {
            if (file.getName().toLowerCase().endsWith(".zip")) {
                NewsZipParser parser = new NewsZipParser(file, handler);
                parser.parse();
                totalFiles+=parser.getNumOfEntries();
            }
        }
    }
}
