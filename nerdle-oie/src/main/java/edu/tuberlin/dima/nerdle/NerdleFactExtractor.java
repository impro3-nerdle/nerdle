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

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

import edu.tuberlin.dima.nerdle.model.NerdleFact;

/**
 * Nerdle OpenIE Process with coreference resolution and lemmatization.
 * 
 * @author Umar Maqsud
 * 
 */
public class NerdleFactExtractor {

	public List<NerdleFact> process(String articleText, String source,
			boolean doCoreference, boolean doLemma, boolean doSynonyms) {
		
		OpenIEHelper openIEHelper = OpenIEHelper.INSTANCE;

		List<NerdleFact> nerdleFacts = new ArrayList<NerdleFact>();

		nerdleFacts.addAll(openIEHelper.extractFactsFromArticleText(
				articleText, source, false));

		if (doCoreference) {

			CoreferencesHelper coreferencesHelper = CoreferencesHelper.INSTANCE;

			String corefArticleText = coreferencesHelper
					.resolveCoreferences(articleText);

			nerdleFacts.addAll(openIEHelper.extractFactsFromArticleText(
					corefArticleText, source, false));
		}

		if (doLemma) {
			processLemma(nerdleFacts);
		}
		
		if (doSynonyms) {
			processSynonyms(nerdleFacts);
		}

		return nerdleFacts;

	}
	
	/* Lemma */
	
	public void processLemma(List<NerdleFact> nerdleFacts) {
		for (NerdleFact nerdleFact : nerdleFacts) {
			processLemma(nerdleFact);
		}
	}

	public void processLemma(NerdleFact nerdleFact) {

		StanfordCoreNLPHelper stanfordCoreNLPHelper = StanfordCoreNLPHelper
				.getInstance();

		List<String> lemmas = stanfordCoreNLPHelper.getLemma(nerdleFact
				.getPredicate().getText());
		String lemma = Joiner.on(" ").join(lemmas);
		nerdleFact.getPredicate().setLemma(lemma);
	}
	
	/* Synonyms */
	
	public void processSynonyms(List<NerdleFact> nerdleFacts) {
		for (NerdleFact nerdleFact : nerdleFacts) {
			processSynonyms(nerdleFact);
		}
	}

	public void processSynonyms(NerdleFact nerdleFact) {

		WordNetHelper wordNetHelper = WordNetHelper.getInstance();

		List<String> synonyms = wordNetHelper.getSynonyms(nerdleFact
				.getPredicate().getLemma());
		nerdleFact.getPredicate().setSynonyms(synonyms);
	}

}
