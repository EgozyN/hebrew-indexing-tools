package NewsArticles;

/**
 * Created by Egozy on 06/11/2014.
 */
public class NewsPage {
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public int getID() {
        return id;
    }

    private String title, author, content;
    private int id;

    public NewsPage(String title, String author, String content, int id) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.id = id;
    }
}
