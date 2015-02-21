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

package edu.tuberlin.dima.nerdle.stringmetric;

import java.util.List;

import scala.Option;

import com.rockymadden.stringmetric.similarity.LevenshteinMetric;

import edu.tuberlin.dima.nerdle.model.NerdleArg;

public class FuzzyStringMatcher {

	private final static double THRESHOLD = 60.0;

	public static double fuzzyMaxArgumentScore(List<NerdleArg> arguments,
			String argument) {
		double max = 0.0;
		for (NerdleArg a : arguments) {
			double score = fuzzyStringScoreNGramm(a.getText(), argument);
			if (max < score) {
				max = score;
			}
		}
		return max;
	}

	public static double fuzzyStringScore(String a, String b) {
		Option<Object> value = LevenshteinMetric.apply().compare(a, b, null);
		String valueString = value.toString().replace("Some(", "")
				.replace(")", "");
		if (!valueString.equals("None")) {
			double comparedValue = Double.parseDouble(valueString);
			return getScore(a, b, comparedValue);
		} else {
			return 0.0D;
		}
	}

	public static double fuzzyStringScoreNGramm(String a, String b) {
		NGramKondrakDistance nGram = new NGramKondrakDistance(5);
		return getScoreNGramm(nGram.getDistance(a, b));
	}

	private static double getScoreNGramm(double comparedValue) {
		double score = comparedValue * 100;
		return score;
	}

	private static double getScore(String verb, String text,
			double comparedValue) {
		double score = (1 - (comparedValue / Math.max(verb.length(),
				text.length()))) * 100;
		return score;
	}

	/* Matchers */

	public static boolean fuzzyArgumentMatcher(List<NerdleArg> arguments,
			String argument) {
		for (NerdleArg a : arguments) {
			double score = fuzzyStringScoreNGramm(a.getText(), argument);

			if (score >= THRESHOLD) {
				return true;
			}
		}
		return false;
	}

	public static boolean fuzzySubjectMatcher(String subject1, String subject2) {
		return fuzzyStringScoreNGramm(subject1, subject2) >= THRESHOLD;
	}

	public static boolean fuzzyVerbMatcher(String verb, String text) {
		return fuzzyStringScoreNGramm(verb, text) >= THRESHOLD;
	}

}
