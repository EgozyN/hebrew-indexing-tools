package NewsArticles;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Egozy on 06/11/2014.
 */
public class NewsParseJson {
    public static NewsPage getNewsPageFromJson(InputStream is) throws IOException {
        NewsPage page = new NewsPage();
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        reader.beginObject();
        assertNextNameEquals(reader, "post_number");
        page.setPostNum(reader.nextInt());
        assertNextNameEquals(reader, "crawl_date");
        try {
            page.setCrawlDate(dateFormat.parse(reader.nextString()));
        } catch (ParseException e) {
            //TODO
            page.setCrawlDate(null);
        }
        assertNextNameEquals(reader, "topic_url");
        page.setTopic_url(reader.nextString());
        assertNextNameEquals(reader, "text");
        page.setText(reader.nextString());
        assertNextNameEquals(reader, "site");
        if (reader.peek() == JsonToken.BEGIN_ARRAY) {
            reader.beginArray();
            List<String> sites = new LinkedList<String>();
            while (reader.hasNext()) {
                sites.add(reader.nextString());
            }
            String[] sitesArray = {};
            sitesArray = sites.toArray(sitesArray);
            page.setSite(sitesArray);
            reader.endArray();
        } else {
            page.setSite(new String[]{reader.nextString()});
        }
        assertNextNameEquals(reader, "datetime");
        try {
            page.setDatetime(dateFormat.parse(reader.nextString()));
        } catch (ParseException e) {
            //TODO
            page.setCrawlDate(null);
        }
        assertNextNameEquals(reader, "likes");
        //TODO
        reader.skipValue();
        assertNextNameEquals(reader, "reported_replies_count");
        page.setReportedRepliesCount(reader.nextInt());
        assertNextNameEquals(reader, "title");
        page.setTitle(reader.nextString());
        assertNextNameEquals(reader, "url");
        page.setUrl(reader.nextString());
        assertNextNameEquals(reader, "author");
        page.setAuthor(reader.nextString());
        assertNextNameEquals(reader, "shares");
        reader.skipValue(); //TODO
        assertNextNameEquals(reader, "category_url");
        page.setCategory_url(nextStringOrNull(reader));
        assertNextNameEquals(reader, "type");
        page.setType(reader.nextString());
        assertNextNameEquals(reader, "category_title");
        page.setCategory_title(nextStringOrNull(reader));
        reader.endObject();
        reader.close();
        return page;
    }

    private static void assertNextNameEquals(JsonReader reader, String text) throws IOException {
        if (!reader.nextName().equals(text)){
            throw new IllegalArgumentException();
        }
    }
    private static String nextStringOrNull(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL){
            reader.skipValue();
            return "null";
        }else{
            return reader.nextString();
        }
    }
}
