package edu.tuberlin.dima.nerdle;

import java.util.List;

import org.junit.Test;

public class StanfordCoreNLPHelpTest {

	@Test
	public void test() {
		StanfordCoreNLPHelper stanfordCoreNLPHelper = StanfordCoreNLPHelper.getInstance();
		
		List<String> lemmas = stanfordCoreNLPHelper.getLemma("could have had been");
		
		for (String string : lemmas) {
			System.out.println();
			System.out.println(string);
		}

		// List<String> lemmas2 = helper.getLemma("burned");
		// for (String string : lemmas2) {
		// System.out.println();
		// System.out.println(string);
		// }
		//
		// List<String> lemmas3 = helper.getLemma("should be forbidden");
		// for (String string : lemmas3) {
		// System.out.println();
		// System.out.println(string);
		// }
	}

}
