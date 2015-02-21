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
