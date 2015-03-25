package com.code972.indexing.NewsArticles;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by Egozy on 06/11/2014.
 */
public class NewsZipTester {
    @Test
    public void simpleTestSingleZip() throws IOException {
        NewsZipParser parser = new NewsZipParser(getClass().getResource("/063633016.zip").getPath(),
                new NewsHandler() {
                    @Override
                    public void process(NewsPage page) {
//                        System.out.println(page.getTitle());
                    }
                });
        parser.parse();
        assert (parser.getNumOfEntries() == 875);
    }

    //    @Test
//    public void testZipIndexer() throws IOException {
//        NewsIndexer.indexZip(getClass().getResource("/063633016.zip").getPath());
//    }
}
