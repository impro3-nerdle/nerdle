package edu.tuberlin.dima.nerdle;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class NerdleFactExtractorTest {
	
	@Test
	public void testProcessOIE(){
		
		NerdleFactExtractor nerdleFactExtractor = new NerdleFactExtractor();
		
		String articleText = "Homer works as a low level safety inspector at the Springfield Nuclear Power Plant. "
				+ "He spends a great deal of his time at Moe's Tavern.";
		
		String source = "http://simpsons.wikia.com/wiki/Homer_Simpson";
		
		List<NerdleFact> nerdleFacts = nerdleFactExtractor.process(articleText, source, true, true, true);
		
		for (NerdleFact nerdleFact : nerdleFacts) {
			System.out.println(nerdleFact);
			assertFalse(nerdleFact.getPredicate().getLemma().equals(""));
		}
		
		assertEquals(nerdleFacts.size(), 4);
		
		// without both
		
		nerdleFacts = nerdleFactExtractor.process(articleText, source, false, false, false);
		
		for (NerdleFact nerdleFact : nerdleFacts) {
			System.out.println(nerdleFact);
			assertEquals("", nerdleFact.getPredicate().getLemma());
		}
		
		assertEquals(nerdleFacts.size(), 2);
				
	}
	
	@Test
	public void testProcessLemma() throws Exception {
		
		NerdleFactExtractor nerdleFactExtractor = new NerdleFactExtractor();
		
		String articleText = "Homer works as a low level safety inspector at the Springfield Nuclear Power Plant. "
				+ "He spends a great deal of his time at Moe's Tavern.";
		
		String source = "http://simpsons.wikia.com/wiki/Homer_Simpson";
		
		List<NerdleFact> nerdleFacts = nerdleFactExtractor.process(articleText, source, false, false, false);
		
		nerdleFactExtractor.processLemma(nerdleFacts);
				
		for (NerdleFact nerdleFact : nerdleFacts) {
			System.out.println(nerdleFact);
			assertFalse(nerdleFact.getPredicate().getLemma().equals(""));
		}
		
		assertEquals(nerdleFacts.size(), 2);
		
	}
	
}
