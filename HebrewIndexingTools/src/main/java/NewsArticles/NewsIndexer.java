package NewsArticles;

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

    public static void indexDirectory(String dirPath, String serverAddress, int serverPort) throws IOException {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "egozy").build();
        TransportClient transportClient = new TransportClient(settings);
        transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress(serverAddress, serverPort));
        Client client = transportClient;
        final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate("news");
        final XContentBuilder mappingBuilder = jsonBuilder().startObject().startObject("properties")
                .startObject("text").field("type", "string").field("analyzer", "hebrew").endObject()
                .startObject("title").field("type", "string").field("analyzer", "hebrew").endObject()
                .endObject().endObject();
        System.out.println(mappingBuilder.string());
        createIndexRequestBuilder.addMapping("document", mappingBuilder);
        createIndexRequestBuilder.execute().actionGet();
        NewsHandler handler = new NewsCallBackIndexer(client, "news");
        NewsDirectoryParser parser = new NewsDirectoryParser(dirPath, handler);
        parser.parse();
    }
}
