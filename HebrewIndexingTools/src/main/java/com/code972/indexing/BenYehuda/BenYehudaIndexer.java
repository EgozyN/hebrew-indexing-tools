package com.code972.indexing.BenYehuda;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import java.net.InetAddress;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by Egozy on 28/11/2014.
 */
public class BenYehudaIndexer {
    public static void indexZip(String dumpFile, String serverAddress, int serverPort, String clusterName) throws Exception {
        Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
        Client client = TransportClient.builder().settings(settings).build().
                addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serverAddress), serverPort));
        final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate("benyehuda");
        final XContentBuilder mappingBuilder = jsonBuilder().startObject().startObject("properties")
                .startObject("text").field("type", "string").field("analyzer", "hebrew").endObject()
                .startObject("title").field("type", "string").field("analyzer", "hebrew").endObject()
                .startObject("author").field("type", "string").field("index","not_analyzed").endObject()
                .endObject().endObject();
        System.out.println(mappingBuilder.string());
        createIndexRequestBuilder.addMapping("document", mappingBuilder);
        createIndexRequestBuilder.execute().actionGet();
        BenYehudaParser parser = new BenYehudaParser(dumpFile);
        BenYehudaHandler handler = new BenYehudaCallBackIndexer(client, "benyehuda");
        parser.setPageCallBack(handler);
        parser.parse();
    }
    /**
     * *
     * @param args benYehudaZip, elasticsearchServerAddress, elasticSearchTransportIp, clusterName
     */
    public static void main(String[] args) throws Exception {
        indexZip(args[0],args[1],Integer.parseInt(args[2]),args[3]);
   }
}
