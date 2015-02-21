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

import scala.collection.Iterator;

import com.google.common.base.Joiner;

import edu.knowitall.openie.Argument;
import edu.knowitall.openie.Extraction;
import edu.knowitall.openie.Instance;
import edu.knowitall.openie.OpenIE;
import edu.knowitall.tool.parse.StanfordParser;
import edu.knowitall.tool.postag.StanfordPostagger;
import edu.knowitall.tool.segment.Segment;
import edu.knowitall.tool.sentence.OpenNlpSentencer;
import edu.knowitall.tool.srl.ClearSrl;
import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.model.NerdlePredicate;
import edu.tuberlin.dima.nerdle.model.NerdleSubject;

/**
 * Singleton class for the OpenIE pipeline.
 * 
 * @author berken
 * 
 */
public enum OpenIEHelper {
	INSTANCE;

	private OpenNlpSentencer sentencer = new OpenNlpSentencer();

	private OpenIE open = new OpenIE(
			new StanfordParser(new StanfordPostagger()), new ClearSrl(), false);

	/**
	 * splits the text to sentences and returns an list containing all sentences
	 * 
	 * @param articleText
	 * @return sentence iterator
	 */
	public List<String> getSentences(String articleText) {
		List<String> sentences = new ArrayList<String>();

		Iterator<Segment> it = sentencer.apply(articleText).iterator();

		while (it.hasNext()) {
			Segment segment = (Segment) it.next();
			sentences.add(segment.text());
		}

		return sentences;
	}

	private List<NerdleFact> extractFactsFromSentence(String sentence,
			String source, boolean doLemma) {

		List<NerdleFact> nerdleFacts = new ArrayList<NerdleFact>();

		Iterator<Instance> instances = open.apply(sentence).iterator();

		while (instances.hasNext()) {
			Instance inst = instances.next();
			Extraction extraction = inst.extr();
			double confidence = inst.conf();

			NerdleFact nerdleFact = new NerdleFact(sentence, source, confidence);
			
			if (doLemma) {
				nerdleFact.setPredicate(new NerdlePredicate(
						extraction.rel().text(), Joiner.on(" ").join(StanfordCoreNLPHelper.getInstance().getLemma(extraction.rel().text()))));
			} else {
				nerdleFact.setPredicate(new NerdlePredicate(
						extraction.rel().text(), ""));
			}

			nerdleFact.setSubject(new NerdleSubject(extraction.arg1().text()));

			Iterator<Argument> arguments = extraction.arg2s().iterator();

			while (arguments.hasNext()) {
				Argument argument = (Argument) arguments.next();

				String ner = argument.displayText().matches("^(L|T).*") ? argument
						.displayText().substring(0, 1) : "";

				nerdleFact.addArgument(new NerdleArg(argument.text(), ner));
			}

			if (!nerdleFact.getSubject().getText().isEmpty()
					&& !nerdleFact.getPredicate().getText().isEmpty()
					&& nerdleFact.getArguments().size() > 0) {

				nerdleFacts.add(nerdleFact);
			}

		}

		return nerdleFacts;
	}

	/**
	 * creates a NerdleFact object
	 * 
	 * @param articleText
	 * @return nerdleFact object
	 */
	public List<NerdleFact> extractFactsFromArticleText(String articleText,
			String source, boolean doLemma) {

		List<String> sentences = getSentences(articleText);

		List<NerdleFact> nerdleFacts = new ArrayList<NerdleFact>();

		for (String sentence : sentences) {
			nerdleFacts.addAll(extractFactsFromSentence(sentence, source, doLemma));
		}

		return nerdleFacts;
	}

}
