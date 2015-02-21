package edu.tuberlin.dima.nerdle.pig;

import org.apache.pig.pigunit.PigTest;
import org.junit.Test;

import com.google.common.io.Resources;

public class NerdleSynonymsPigTest extends PigUnitTest {

	@Test
	public void test() throws Exception {
		String pigFile = Resources.getResource("nerdle_synonyms.pig").getPath();

		String[] args = { "input="
				+ Resources.getResource("output_facts_lemma").getPath() };

		PigTest test = new PigTest(pigFile, args);

		test.unoverride("STORE");

		printAlias(test, "facts");
	}

}
