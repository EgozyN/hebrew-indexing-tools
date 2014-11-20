package BenYehuda;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;

/**
 * Created by Egozy on 29/10/2014.
 */
public class BenYehudaTester {

    //    @Test
    public void test() throws Exception {
        BenYehudaParser parser = new BenYehudaParser("./ProjectBenYehuda_Dump.zip");
        parser.setPageCallBack(new BenYehudaHandler() {
            @Override
            public void process(BenYehudaPage page) {
//                System.out.println(page.getTitle());
            }
        });
        parser.parse();
    }

//    @Test
    public void testIndex() throws Exception {
        //Create Client
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "egozy").build();
        TransportClient transportClient = new TransportClient(settings);
        transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
        Client client = (Client) transportClient;
        BenYehudaParser parser = new BenYehudaParser("./ProjectBenYehuda_Dump.zip");
        parser.setPageCallBack(new BenYehudaCallBackIndexer(client, "benyehuda"));
        parser.parse();
    }

}
