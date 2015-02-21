package edu.tuberlin.dima.nerdle;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class OpenIEHelperTest {

	@Test
	public void test() throws Exception {
		OpenIEHelper openIEHelper = OpenIEHelper.INSTANCE;

		String articleText = "Homer works as a low level safety inspector at the Springfield Nuclear Power Plant. "
				+ "He spends a great deal of his time at Moe's Tavern.";
		
		String source = "http://simpsons.wikia.com/wiki/Homer_Simpson";
		
		List<String> sentences = openIEHelper.getSentences(articleText);
		
		assertEquals(2, sentences.size());
		
		System.out.println("-- Sentences --");
		for (String sentence : sentences) {
			System.out.println(sentence);
		}
		
		System.out.println();
		
		List<NerdleFact> nerdleFacts = openIEHelper.extractFactsFromText(articleText, source);
		
		System.out.println("-- Facts --");
		for (NerdleFact nerdleFact : nerdleFacts) {
			System.out.println(nerdleFact);
		}
		
		System.out.println();
		
		String corefArticleText = openIEHelper.resolveCoreferences(articleText);
		
		System.out.println("-- Coref Text --");
		System.out.println(corefArticleText);
		
		List<NerdleFact> corefNerdleFacts = openIEHelper.extractFactsFromText(articleText, source);
		
		System.out.println();
		
		System.out.println("-- corefFacts --");
		for (NerdleFact nerdleFact : corefNerdleFacts) {
			System.out.println(nerdleFact);
		}
		
	}
}
