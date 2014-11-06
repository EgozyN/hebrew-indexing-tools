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
        assert (reader.nextName().equals("post_number"));
        page.setPostNum(reader.nextInt());
        assert (reader.nextName().equals("crawl_date"));
        try {
            page.setCrawlDate(dateFormat.parse(reader.nextString()));
        } catch (ParseException e) {
            //TODO
            page.setCrawlDate(null);
        }
        assert (reader.nextName().equals("topic_url"));
        page.setTopic_url(reader.nextString());
        assert (reader.nextName().equals("text"));
        page.setText(reader.nextString());
        assert (reader.nextName().equals("site"));
        if (reader.peek() == JsonToken.BEGIN_ARRAY) {
            reader.beginArray();
            List<String> sites = new LinkedList<String>();
            while (reader.hasNext()) {
                sites.add(reader.nextString());
            }
            reader.endArray();
        } else {
            page.setSite(new String[]{reader.nextString()});

        }
        assert (reader.nextName().equals("datetime"));
        try {
            page.setDatetime(dateFormat.parse(reader.nextString()));
        } catch (ParseException e) {
            //TODO
            page.setCrawlDate(null);
        }
        assert (reader.nextName().equals("likes"));
        //TODO
        reader.skipValue();
        assert (reader.nextName().equals("reported_replies_count"));
        page.setReportedRepliesCount(reader.nextInt());
        assert (reader.nextName().equals("title"));
        page.setTitle(reader.nextString());
        assert (reader.nextName().equals("url"));
        page.setUrl(reader.nextString());
        assert (reader.nextName().equals("author"));
        page.setAuthor(reader.nextString());
        assert (reader.nextName().equals("shares"));
        reader.skipValue(); //TODO
        assert (reader.nextName().equals("category_url"));
        page.setCategory_url(nextStringOrNull(reader));
        assert (reader.nextName().equals("type"));
        page.setType(reader.nextString());
        assert (reader.nextName().equals("category_title"));
        page.setCategory_title(nextStringOrNull(reader));
        reader.endObject();
        reader.close();
        return page;
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
