package BenYehuda;

import org.junit.Test;

/**
 * Created by Egozy on 29/10/2014.
 */
public class BenYehudaTester {

    //    @Test
    public void test() throws Exception {
        BenYehudaParser parser = new BenYehudaParser("./ProjectBenYehuda_Dump.zip");
        parser.setPageCallBack(new BenYehudaHandler() {
            @Override
            public void process(BenYehudaPage page) {
//                System.out.println(page.getTitle());
            }
        });
        parser.parse();
    }
}
