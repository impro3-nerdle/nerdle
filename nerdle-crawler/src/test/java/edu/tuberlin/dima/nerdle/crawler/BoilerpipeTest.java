package edu.tuberlin.dima.nerdle.crawler;

import java.net.URL;

import org.junit.Test;

import com.google.common.io.Resources;

import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class BoilerpipeTest {
	
	@Test
	public void testBoilerpipeWeb() throws Exception{
		URL url = new URL("http://memory-beta.wikia.com/wiki/Star_Trek:_Destiny");
		String text = ArticleExtractor.INSTANCE.getText(url);
		System.out.println(text);
	}
	
	@Test
	public void testBoilerpipeLocal() throws Exception{
		URL url = Resources.getResource(BoilerpipeTest.class,"wikia/sample.html");
		String text = ArticleExtractor.INSTANCE.getText(url);
		System.out.println(text);
	}

}
