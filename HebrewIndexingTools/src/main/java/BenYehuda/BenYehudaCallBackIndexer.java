package BenYehuda;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 * Created by egozy on 11/20/14.
 */
public class BenYehudaCallBackIndexer implements BenYehudaHandler {

    private final Client client;
    private final String indexName;
//    private BulkRequestBuilder currentRequest;
//    private static int bulkSize = 100;
//    private static final AtomicInteger onGoingBulks = new AtomicInteger();
//    private int allowedConcurrentBulks = 3;

    public BenYehudaCallBackIndexer(Client client, String indexName) {
        this.client = client;
        this.indexName = indexName;
    }

    @Override
    public void process(BenYehudaPage page) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
            System.out.println("indexing: " + page.getTitle());
            builder.field("title", page.getTitle());
            builder.field("author", page.getAuthor());
            builder.field("content", page.getContent());
            builder.endObject();
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName,"benyehudapage");
            indexRequestBuilder.setSource(builder);
            IndexResponse response = indexRequestBuilder .execute().actionGet();
        } catch (Exception e) {
            System.out.println("failed to construct index request" + e);
        }
    }

}
