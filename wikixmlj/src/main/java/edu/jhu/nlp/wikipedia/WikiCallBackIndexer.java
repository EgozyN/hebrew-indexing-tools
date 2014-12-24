package edu.jhu.nlp.wikipedia;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.base.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Egozy on 28/11/2014.
 */
public class WikiCallBackIndexer implements PageCallbackHandler {
    private static org.apache.log4j.Logger logger   = org.apache.log4j.Logger.getLogger(WikiCallBackIndexer.class);
    private final Client client;
    private final String indexName;
    private BulkRequestBuilder currentRequest;
    private static int bulkSize = 100;
    private static final AtomicInteger onGoingBulks = new AtomicInteger();
    private int allowedConcurrentBulks = 3;

    public WikiCallBackIndexer(Client client, String indexName) {
        this.client = client;
        this.indexName = indexName;
        this.currentRequest = client.prepareBulk();
    }

    @Override
    public void process(WikiPage page) {
        String title = stripTitle(page.getTitle());
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("page #%s: %s", page.getID(), page.getTitle()));
        }

        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
            builder.field("title", title);
            builder.field("text", page.getText());

            if (page.isRedirect()) {
                if (!Strings.isNullOrEmpty(page.getRedirectPage()))
                    builder.field("redirect", page.getRedirectPage());
                else
                    builder.field("redirect", "#");
            }
            if (page.isSpecialPage())
                builder.field("special", true);
            if (page.isStub())
                builder.field("stub", true);
            if (page.isDisambiguationPage())
                builder.field("disambiguation", true);

            builder.startArray("category");
            for (String s : page.getCategories()) {
                builder.value(s);
            }
            builder.endArray();

            builder.startArray("link");
            for (String s : page.getLinks()) {
                builder.value(s);
            }
            builder.endArray();

            builder.endObject();
            // For now, we index (and not create) since we need to keep track of what we indexed...
            currentRequest.add(client.prepareIndex(indexName, "document", page.getID()).setCreate(false).setSource(builder));
            processBulkIfNeeded(page.getID());
        } catch (Exception e) {
            logger.warn("failed to construct index request", e);
        }
    }

    int inserted = 0;
    private void processBulkIfNeeded(String id) {
        if (currentRequest.numberOfActions() >= bulkSize) {
            // execute the bulk operation
            int currentOnGoingBulks = onGoingBulks.incrementAndGet();
            if (currentOnGoingBulks > allowedConcurrentBulks) {
                // TODO, just wait here!, we can slow down the wikipedia parsing
                logger.warn(String.format("dropping bulk, [%d] crossed threshold [%d]", onGoingBulks.get(), allowedConcurrentBulks));

                try {
                    Thread.sleep(6000);
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
                            logger.warn("Bulk response has failures");
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        logger.warn("failed to execute bulk");
                    }
                });
            } catch (Exception e) {
                logger.warn("failed to process bulk", e);
                return;
            }

            inserted += currentRequest.numberOfActions();
            System.out.println("Inserted " + inserted + " last id " + id);

            currentRequest = client.prepareBulk();
        }
    }

    private StringBuilder sb = new StringBuilder();

    private String stripTitle(String title) {
        sb.setLength(0);
        sb.append(title);
        while (sb.length() > 0 && (sb.charAt(sb.length() - 1) == '\n' || (sb.charAt(sb.length() - 1) == ' '))) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
