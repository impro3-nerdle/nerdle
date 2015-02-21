package edu.tuberlin.dima.nerdle;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordNetHelperTest{
	
	@Test
	public void test(){
		
		WordNetHelper wordNetHelper = WordNetHelper.getInstance();
		 wordNetHelper = WordNetHelper.getInstance();
		 wordNetHelper = WordNetHelper.getInstance();
		List<String> synonyms = wordNetHelper.getSynonyms("bear");
		
		List<String> expectedSynonyms = Arrays.asList("give birth", "bear", "yield", "have a bun in the oven", "hold", "digest", "behave", "wear");
		
		assertEquals(expectedSynonyms, synonyms);
		
	}
	
}
