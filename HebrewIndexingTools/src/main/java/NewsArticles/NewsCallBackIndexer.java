package NewsArticles;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 * Created by egozy on 11/20/14.
 */
public class NewsCallBackIndexer implements NewsHandler {

    private final Client client;
    private final String indexName;
//    private BulkRequestBuilder currentRequest;
//    private static int bulkSize = 100;
//    private static final AtomicInteger onGoingBulks = new AtomicInteger();
//    private int allowedConcurrentBulks = 3;

    public NewsCallBackIndexer(Client client, String indexName) {
        this.client = client;
        this.indexName = indexName;
    }

    @Override
    public void process(NewsPage page) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
            builder.field("title", page.getTitle());
            builder.field("author", page.getAuthor());
            builder.field("date", page.getDatetime());
            builder.field("type", page.getType());
            builder.field("text", page.getText());
            builder.endObject();
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName,"newspage");
            indexRequestBuilder.setSource(builder);
            IndexResponse response = indexRequestBuilder .execute().actionGet();
        } catch (Exception e) {
            System.out.println("failed to construct index request" + e);
        }
    }

}
