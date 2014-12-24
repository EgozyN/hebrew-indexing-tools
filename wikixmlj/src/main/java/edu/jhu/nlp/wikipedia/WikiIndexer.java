package edu.jhu.nlp.wikipedia;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by Egozy on 28/11/2014.
 */
public class WikiIndexer {
    public static void indexDump(String dumpPath, String serverAddress, int serverPort) throws Exception {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "egozy").build();
        TransportClient transportClient = new TransportClient(settings);
        transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress(serverAddress, serverPort));
        Client client = transportClient;

        final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate("wiki");
        final XContentBuilder mappingBuilder = jsonBuilder().startObject().startObject("properties")
                .startObject("title").field("type", "string").field("analyzer", "hebrew").endObject()
                .startObject("text").field("type", "string").field("analyzer", "hebrew").endObject()
                .startObject("category").field("type", "string").field("index","not_analyzed").endObject()
//                .startObject("category").field("type", "string").field("analyzer", "hebrew").endObject()
//                .startObject("link").field("type", "string").field("analyzer", "hebrew").endObject()
                .endObject().endObject();
        System.out.println(mappingBuilder.string());
        createIndexRequestBuilder.addMapping("document", mappingBuilder);
        createIndexRequestBuilder.execute().actionGet();

        PageCallbackHandler handler = new WikiCallBackIndexer(client,"wiki");
        WikiXMLParser wxsp = null;
        try {
            File file = new File(dumpPath);
            wxsp = WikiXMLParserFactory.getSAXParser(file.toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        try {
            wxsp.setPageCallback(handler);
            wxsp.parse();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
