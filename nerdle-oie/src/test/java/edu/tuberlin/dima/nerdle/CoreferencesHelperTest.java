package edu.tuberlin.dima.nerdle;

import java.util.List;

import org.junit.Test;

import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class CoreferencesHelperTest {
	
	@Test
	public void test(){
		
		CoreferencesHelper coreferencesHelper = CoreferencesHelper.INSTANCE;
		
		OpenIEHelper openIEHelper = OpenIEHelper.INSTANCE;
		
		String articleText = "Homer works as a low level safety inspector at the Springfield Nuclear Power Plant. "
				+ "He spends a great deal of his time at Moe's Tavern.";
		
		String source = "http://simpsons.wikia.com/wiki/Homer_Simpson";
		
		System.out.println();
		
		String corefArticleText = coreferencesHelper.resolveCoreferences(articleText);
		
		System.out.println("-- Coref Text --");
		System.out.println(corefArticleText);
		
		List<NerdleFact> corefNerdleFacts = openIEHelper.extractFactsFromArticleText(articleText, source, true);
		
		System.out.println();
		
		System.out.println("-- corefFacts --");
		for (NerdleFact nerdleFact : corefNerdleFacts) {
			System.out.println(nerdleFact);
		}
		
	}
	
}
