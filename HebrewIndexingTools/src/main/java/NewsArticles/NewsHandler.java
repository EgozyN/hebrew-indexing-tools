package NewsArticles;

/**
 * Created by Egozy on 06/11/2014.
 */
public abstract class NewsHandler {
    public abstract void process(NewsPage page);
    public void finishRemaining(){}
}
