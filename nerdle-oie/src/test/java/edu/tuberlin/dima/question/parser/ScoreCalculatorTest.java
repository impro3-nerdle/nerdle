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

package edu.tuberlin.dima.question.parser;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.model.NerdlePredicate;
import edu.tuberlin.dima.nerdle.model.NerdleSubject;
import edu.tuberlin.dima.nerdle.qa.ScoreCalculator;
import edu.tuberlin.dima.nerdle.stringmetric.FuzzyStringMatcher;

public class ScoreCalculatorTest {

	private static NerdleFact answerExtraction;

	@BeforeClass
	public static void setup() {

		// exact answer
		answerExtraction = new NerdleFact("Homer runs into trouble.", "", 0.5D);

		answerExtraction.setPredicate(new NerdlePredicate("runs", ""));
		answerExtraction.setSubject(new NerdleSubject("Homer"));
		answerExtraction.addArgument(new NerdleArg("into trouble", "L"));

	}

	@Test
	public void nGramTester() {
		System.out.println(FuzzyStringMatcher
				.fuzzyStringScoreNGramm("be", "bo"));
	}

	@Test
	public void levenshteinTester() {
		System.out.println(FuzzyStringMatcher.fuzzyStringScore("be", "bo"));
	}

	@Test
	public void scoreCalculator1() {
		NerdleFact extraction = new NerdleFact("Einstein was born on 4th May.",
				"", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was born", ""));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("on 4th May", "T"));

		double score = ScoreCalculator.calculateForSubject(extraction,
				answerExtraction);
		System.out.println("score1 " + score);

	}

	@Test
	public void scoreCalculator2() {
		NerdleFact extraction = new NerdleFact(
				"Einstein was born in Ulm on 4th May.", "", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was born", ""));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("on 4th May", "T"));
		extraction.addArgument(new NerdleArg("in Ulm", "L"));

		double score = ScoreCalculator.calculate(extraction, answerExtraction);
		System.out.println("score2 " + score);
	}

	@Test
	public void scoreCalculatorForSubject() {
		NerdleFact extraction = new NerdleFact("Homer runs into trouble.", "",
				0.5D);

		extraction.setPredicate(new NerdlePredicate("runs", ""));
		extraction.setSubject(new NerdleSubject("Homer"));
		extraction.addArgument(new NerdleArg("into trouble", "T"));

		double score = ScoreCalculator.calculateForSubject(extraction,
				answerExtraction);
		System.out.println("score3 " + score);
	}

	@Test
	public void scoreCalculatorForArgument() {
		NerdleFact extraction = new NerdleFact("Einstein was born in Ulm.", "",
				0.5D);

		extraction.setPredicate(new NerdlePredicate("born", ""));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("in Berlin", "L"));

		double score = ScoreCalculator.calculateForArguments(extraction,
				answerExtraction, "L");
		System.out.println("score4 " + score);
	}

}
