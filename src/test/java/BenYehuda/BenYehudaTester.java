package BenYehuda;

import BenYehuda.BenYehudaParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;import java.lang.String;import java.lang.System;

/**
 * Created by Egozy on 29/10/2014.
 */
public class BenYehudaTester {

    @Test
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
