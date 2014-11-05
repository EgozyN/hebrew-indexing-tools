package edu.jhu.nlp.wikipedia;

/**
 * Created by Egozy on 05/11/2014.
 */

import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


public class WikiXMLParserFactoryTest {
    @Test
    public void testGetSAXParserFromRelativeFileName() {
        try {
            assertNotNull(WikiXMLParserFactory.getSAXParser("../articles.xml"));
        } catch (MalformedURLException e) {
            fail("relative filename should be get parser; exception:" + e);
        }
    }

    @Test
    public void testGetSAXParserFromAbsoluteFileName() {
        try {
            assertNotNull(WikiXMLParserFactory.getSAXParser("/articles.xml"));
        } catch (MalformedURLException e) {
            fail("relative filename should be get parser; exception:" + e);
        }
    }
}