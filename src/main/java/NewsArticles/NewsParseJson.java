package NewsArticles;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Egozy on 06/11/2014.
 */
public class NewsParseJson {
    public static NewsPage getNewsPageFromJson(InputStream is) throws IOException {
        String title, author, content;
        int id;
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        reader.beginObject();
        assert (reader.nextName().equals("post_number"));
        id = reader.nextInt();
        assert (reader.nextName().equals("crawl_date"));
        reader.skipValue();
        assert (reader.nextName().equals("topic_url"));
        reader.skipValue();
        assert (reader.nextName().equals("text"));
        content = reader.nextString();
        assert (reader.nextName().equals("site"));
        reader.skipValue();
        assert (reader.nextName().equals("datetime"));
        reader.skipValue();
        assert (reader.nextName().equals("likes"));
        reader.skipValue();
        assert (reader.nextName().equals("reported_replies_count"));
        reader.skipValue();
        assert (reader.nextName().equals("title"));
        title = reader.nextString();
        assert (reader.nextName().equals("url"));
        reader.skipValue();
        assert (reader.nextName().equals("author"));
        author = reader.nextString();
        assert (reader.nextName().equals("shares"));
        reader.skipValue();
        assert (reader.nextName().equals("category_url"));
        reader.skipValue();
        assert (reader.nextName().equals("type"));
        reader.skipValue();
        assert (reader.nextName().equals("category_title"));
        reader.skipValue();
        return new NewsPage(title, author, content, id);
    }
}
