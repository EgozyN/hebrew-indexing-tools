package NewsArticles;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by egozy on 11/20/14.
 */
public class NewsCallBackIndexer extends NewsHandler {

    private final Client client;
    private final String indexName;
    private BulkRequestBuilder currentRequest;
    private static int bulkSize = 100;
    private static final AtomicInteger onGoingBulks = new AtomicInteger();
    private int allowedConcurrentBulks = 3;

    public NewsCallBackIndexer(Client client, String indexName) {
        this.client = client;
        this.indexName = indexName;
        this.currentRequest = client.prepareBulk();
    }

    @Override
    public void process(NewsPage page) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
            builder.field("title", page.getTitle());
            builder.field("author", page.getAuthor());
            builder.field("date", page.getDatetime());
            builder.field("type", page.getType());
            builder.field("url", page.getUrl());
            builder.field("text", page.getText());
            builder.startArray("sites");
            for (String s : page.getSites()) {
                builder.value(s);
            }
            builder.endArray();
//            if (!page.getCategory_title().equals("null"))
//            {
//                builder.field("category",page.getCategory_title());
//            }
            builder.endObject();
            currentRequest.add(client.prepareIndex(indexName, "document").setCreate(false).setSource(builder));
            processBulkIfNeeded();
        } catch (Exception e) {
            System.out.println("failed to construct index request: " + e);
        }
    }
    int inserted = 0;
    private void processBulkIfNeeded() {
        if (currentRequest.numberOfActions() >= bulkSize) {
            // execute the bulk operation
            int currentOnGoingBulks = onGoingBulks.incrementAndGet();
            if (currentOnGoingBulks > allowedConcurrentBulks) {
                // TODO, just wait here!, we can slow down the wikipedia parsing
//                logger.warn(String.format("dropping bulk, [%d] crossed threshold [%d]", onGoingBulks.get(), allowedConcurrentBulks));
                System.out.println(String.format("dropping bulk, [%d] crossed threshold [%d]", onGoingBulks.get(), allowedConcurrentBulks));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            try {
                currentRequest.execute(new ActionListener<BulkResponse>() {
                    @Override
                    public void onResponse(BulkResponse bulkResponse) {
                        onGoingBulks.decrementAndGet();
                        if (bulkResponse.hasFailures()) {
//                            logger.warn("Bulk response has failures");
                            System.out.println("Bulk response has failures");
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
//                        logger.warn("failed to execute bulk");
                        System.out.println("failed to execute bulk");
                    }
                });
            } catch (Exception e) {
//                logger.warn("failed to process bulk", e);
                System.out.println("failed to process bulk" + e);
                return;
            }
            inserted += currentRequest.numberOfActions();
            System.out.println("Inserted " + inserted);
            currentRequest = client.prepareBulk();
        }
    }

    @Override
    public void finishRemaining(){
        if(currentRequest.numberOfActions() > 0){
            inserted += currentRequest.numberOfActions();
            BulkResponse bulkRes = currentRequest.execute().actionGet();
            if(bulkRes.hasFailures()){
                System.out.println("failed to process last bulk");
            }else{
                System.out.println("Inserted " + inserted);
            }
        }
        this.currentRequest = client.prepareBulk();
    }
}
