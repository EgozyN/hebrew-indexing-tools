package com.code972.indexing.NewsArticles;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by Egozy on 28/11/2014.
 */
public class NewsIndexer {
    public static void indexDirectory(String dirPath, String serverAddress, int serverPort, String clusterName) throws IOException {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build();
        Client client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(serverAddress, serverPort));
        final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate("news");
        final XContentBuilder mappingBuilder = jsonBuilder().startObject().startObject("properties")
                .startObject("text").field("type", "string").field("analyzer", "hebrew").endObject()
                .startObject("title").field("type", "string").field("analyzer", "hebrew").endObject()
                .startObject("author").field("type", "string").field("index", "not_analyzed").endObject()
//                .startObject("category").field("type", "string").field("index", "not_analyzed").endObject()
                .startObject("sites").field("type","string").field("index","not_analyzed").endObject()
                .endObject().endObject();
        System.out.println(mappingBuilder.string());
        createIndexRequestBuilder.addMapping("document", mappingBuilder);
        createIndexRequestBuilder.execute().actionGet();
        NewsHandler handler = new NewsCallBackIndexer(client, "news");
        NewsDirectoryParser parser = new NewsDirectoryParser(dirPath, handler);
        parser.parse();
    }

    /**
     * *
     * @param args newsFolder, elasticsearchServerAddress, elasticSearchTransportIp, clusterName
     */
    public static void main(String[] args) throws IOException {
        NewsIndexer.indexDirectory(args[0],args[1],Integer.parseInt(args[2]),args[3]);
    }
}
