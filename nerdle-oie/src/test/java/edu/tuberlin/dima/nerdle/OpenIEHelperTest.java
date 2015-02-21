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
		
		List<NerdleFact> nerdleFacts = openIEHelper.extractFactsFromArticleText(articleText, source, true);
		
		System.out.println("-- Facts --");
		for (NerdleFact nerdleFact : nerdleFacts) {
			System.out.println(nerdleFact);
		}
				
	}
	
	public void testReproduceError(){
		OpenIEHelper openIEHelper = OpenIEHelper.INSTANCE;
		
		openIEHelper.extractFactsFromArticleText("", "source", true);
		
		openIEHelper.extractFactsFromArticleText(null, "source", true);
		
	}
}
