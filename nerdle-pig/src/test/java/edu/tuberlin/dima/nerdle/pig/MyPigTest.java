package edu.tuberlin.dima.nerdle.pig;

import org.apache.pig.pigunit.PigTest;
import org.junit.Test;

import com.google.common.io.Resources;

public class MyPigTest {

    @Test
    public void testTop2Queries() throws Exception {
        String[] args = {
                "n=2",
        };
        
        String pigFile = Resources.getResource("top_queries.pig").getPath();

        PigTest test = new PigTest(pigFile, args);
        
        String[] input = {
                "yahoo",
                "yahoo",
                "yahoo",
                "twitter",
                "facebook",
                "facebook",
                "linkedin",
        };

        String[] output = {
                "(yahoo,3)",
                "(facebook,2)",
        };

        test.assertOutput("data", input, "queries_limit", output);

    }

}
