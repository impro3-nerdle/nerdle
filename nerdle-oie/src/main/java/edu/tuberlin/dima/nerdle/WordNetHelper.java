package edu.tuberlin.dima.nerdle;

import java.util.HashSet;
import java.util.List;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.dictionary.Dictionary;

import com.google.common.collect.Lists;

public class WordNetHelper {

	private static WordNetHelper instance = null;

	private Dictionary dictionary;

	private WordNetHelper() {
		try {
			dictionary = Dictionary.getDefaultResourceInstance();
		} catch (JWNLException e) {
			e.printStackTrace();
		}
	}
	
	public static WordNetHelper getInstance() {
		if (instance == null) {
			instance = new WordNetHelper();
		}
		return instance;
	}

	public List<String> getSynonyms(String lemma) {
		HashSet<String> words = new HashSet<String>();
		try {
			IndexWord lemmaIndex = dictionary.lookupIndexWord(POS.VERB, lemma);

			if (lemmaIndex != null) {

				List<Synset> synsets = lemmaIndex.getSenses();

				for (Synset synset : synsets) {
					words.add(synset.getWords().get(0).getLemma());
				}

			}

		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return Lists.newArrayList(words);
	}

}
