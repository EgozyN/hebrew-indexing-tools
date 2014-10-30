/**
 * Created by Egozy on 29/10/2014.
 */
public class Publish {
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getExtra() {
        return extra;
    }

    String title, author, content, extra;

    public Publish(String title, String author, String content, String extra) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.extra = extra;
    }
}
