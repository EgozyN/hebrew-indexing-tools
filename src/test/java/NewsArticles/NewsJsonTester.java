package NewsArticles;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Egozy on 06/11/2014.
 */
public class NewsJsonTester {

    @Test
    public void simpleTestSingleJson() throws IOException {
        InputStream is = getClass().getResourceAsStream("/intrestsept.json");
        NewsPage page = NewsParseJson.getNewsPageFromJson(is);
        assert(page.getTitle().equals("נגידת בנק ישראל החליטה להותיר את הריבית לחודש נובמבר 2014 ללא שינוי ברמה של 0.25%"));
        assert(page.getAuthor().equals("קרן מרדכי"));
    }
}
