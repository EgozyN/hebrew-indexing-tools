package NewsArticles;

import BenYehuda.BenYehudaCallBackIndexer;
import BenYehuda.BenYehudaParser;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
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

    @Test
    public void testIndexer() throws IOException {
        //Create Client
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "egozy").build();
        TransportClient transportClient = new TransportClient(settings);
        transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
        Client client = (Client) transportClient;
        NewsZipParser parser = new NewsZipParser(getClass().getResource("/063633016.zip").getPath(), new NewsCallBackIndexer(client, "news"));
        parser.parse();
    }
}
