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
