package edu.jhu.nlp.wikipedia;

import org.junit.Test;

import java.io.File;

/**
 * Created by Egozy on 28/11/2014.
 */
public class WikiIndexTest {
    @Test
    public void testIndexDump() throws Exception {
//        File file = new File("./hewiki-20141005-pages-articles.xml.bz2");
//        System.out.println(file.exists());
        WikiIndexer.indexDump("./hewiki-20141005-pages-articles.xml.bz2", "10.0.0.6",9300);
//        WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser());

    }

}
