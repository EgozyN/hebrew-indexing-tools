package NewsArticles;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by Egozy on 06/11/2014.
 */
public class NewsDirectoryTester {
    @Test
    public void simpleTestSingleDir() throws IOException {
        NewsDirectoryParser parser = new NewsDirectoryParser("./News/",
                new NewsHandler() {
                    @Override
                    public void process(NewsPage page) {
//                        System.out.println(page.getID());
                    }
                });
        parser.parse();
        System.out.println(parser.getTotalFiles());
    }
}
