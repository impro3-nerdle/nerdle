/**
 * Copyright 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
