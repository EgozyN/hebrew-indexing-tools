import org.junit.Test;

import java.io.File;
import java.io.IOException;import java.lang.String;import java.lang.System;

/**
 * Created by Egozy on 29/10/2014.
 */
public class BenYehudaTester {

    @Test
    public void test() throws IOException {
        testFolder("./BenYehuda/");
    }

    private void testFolder(String folderName) throws IOException {
        File folder = new File(folderName);
        for (File file: folder.listFiles()){
            if (file.isDirectory()){
                    testFolder(file.getAbsolutePath());
            }else{
                System.out.println(file.getAbsolutePath());
                if (!file.getName().equals("index.html"))
                BenYehudaParser.getPublish(file.getAbsolutePath());
            }
        }
    }
}
