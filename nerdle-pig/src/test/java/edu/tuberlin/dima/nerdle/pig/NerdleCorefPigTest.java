package edu.tuberlin.dima.nerdle.pig;

import org.apache.pig.pigunit.PigTest;
import org.junit.Test;

import com.google.common.io.Resources;

public class NerdleCorefPigTest extends PigUnitTest {

	@Test
	public void test() throws Exception {
		String pigFile = Resources.getResource("nerdle_coref.pig").getPath();

		String[] args = { "input=" + Resources.getResource("input").getPath(),
				"output=output_coref" };

		PigTest test = new PigTest(pigFile, args);

		test.unoverride("STORE");
		
		printAlias(test, "articleTexts");
		// printAlias(test, "articleTexts_coref");
	}

}
