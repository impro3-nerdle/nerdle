package edu.tuberlin.dima.nerdle;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class StanfordCoreNLPHelper {
	private static StanfordCoreNLPHelper instance = null;
	private StanfordCoreNLP pipeline;

	private StanfordCoreNLPHelper() {
		Properties prop = new Properties();
		prop.put("annotators", "tokenize, ssplit, pos, lemma");
		pipeline = new StanfordCoreNLP(prop);
	}

	public static StanfordCoreNLPHelper getInstance() {
		if (instance == null) {
			instance = new StanfordCoreNLPHelper();
		}
		return instance;
	}

	public StanfordCoreNLP getPipeline() {
		return pipeline;
	}

	public void setPipeline(StanfordCoreNLP pipeline) {
		this.pipeline = pipeline;
	}
	
	public List<String> getLemma(String text) {

		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER
				
		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		List<String> lemmas = new ArrayList<String>();

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the lemma

				String pos = token.get(PartOfSpeechAnnotation.class);

				if (!pos.equals("MD")) {

					String lemma = token.get(LemmaAnnotation.class);

					lemmas.add(lemma);

				}

			}
		}

		List<String> finalList = new ArrayList<String>();

		if (lemmas.size() >= 2) {

			for (String lemma : lemmas) {
				if (!lemma.equals("be") && !lemma.equals("have")
						&& !lemma.equals("do")) {
					finalList.add(lemma);
				}
			}

			if (finalList.size() == 0) {
				finalList.add(lemmas.get((lemmas.size() - 1)));
			}

		} else {
			finalList.add(lemmas.get(0));
		}

		return finalList;
	}

}