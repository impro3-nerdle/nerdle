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
		answerExtraction = new NerdleFact(
				"Einstein was born in Ulm on 4th May.", "", 0.5D);

		answerExtraction.setPredicate(new NerdlePredicate("was born", ""));
		answerExtraction.setSubject(new NerdleSubject("Einstein"));
		answerExtraction.addArgument(new NerdleArg("in Ulm", "L"));
		answerExtraction.addArgument(new NerdleArg("on 4th May", "T"));

	}

	@Test
	public void nGramTester() {
		System.out.println(FuzzyStringMatcher.fuzzyStringScoreNGramm(
				"who is a Klingon dish", "Racht is a Klingon dish"));
	}

	@Test
	public void levenshteinTester() {
		System.out.println(FuzzyStringMatcher.fuzzyStringScore(
				"who is a Klingon dish", "Racht is a Klingon dish"));
	}

	@Test
	public void scoreCalculator1() {
		NerdleFact extraction = new NerdleFact("Einstein was born on 4th May.",
				"", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was born", ""));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("on 4th May", "T"));

		double score = ScoreCalculator.calculate(extraction, answerExtraction);
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
		NerdleFact extraction = new NerdleFact(
				"Einstein was born in Ulm on 4th May.", "", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was born", ""));
		extraction.setSubject(new NerdleSubject("Peter"));
		extraction.addArgument(new NerdleArg("on 4th May", "T"));
		extraction.addArgument(new NerdleArg("in Berlin", "L"));

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
