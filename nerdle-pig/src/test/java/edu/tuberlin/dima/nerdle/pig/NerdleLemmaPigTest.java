package edu.tuberlin.dima.nerdle.pig;

import org.apache.pig.pigunit.PigTest;
import org.junit.Test;

import com.google.common.io.Resources;

public class NerdleLemmaPigTest extends PigUnitTest {

	@Test
	public void test() throws Exception {
		String pigFile = Resources.getResource("nerdle_lemma.pig").getPath();

		String[] args = {
				"input=" + Resources.getResource("output_facts").getPath(),
				"output=output_lemma" };

		PigTest test = new PigTest(pigFile, args);

		test.unoverride("STORE");

		printAlias(test, "facts");
	}

}
