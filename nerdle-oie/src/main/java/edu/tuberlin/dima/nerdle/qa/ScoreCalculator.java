package edu.tuberlin.dima.nerdle.qa;

import java.util.ArrayList;
import java.util.List;

import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.stringmetric.FuzzyStringMatcher;

public class ScoreCalculator {

	/* EXTRACTIONS */

	public static double calculateForSubject(NerdleFact questionExtraction,
			NerdleFact answerExtraction) {

		double purity = 0.0;

		List<Double> scores = new ArrayList<Double>();

		// scores.add(getPredicateScore(questionExtraction, answerExtraction));

		for (NerdleArg nerdleArgument : questionExtraction.getArguments()) {
			scores.add(getArgumentScore(answerExtraction, nerdleArgument));
		}

		purity = calculatePurity(scores);

		return purity;

	}

	public static double calculateForArguments(NerdleFact questionExtraction,
			NerdleFact answerExtraction, String ner) {

		double purity = 0.0;
		List<Double> scores = new ArrayList<Double>();

		// scores.add(getPredicateScore(questionExtraction, answerExtraction));

		scores.add(getSubjectScore(questionExtraction, answerExtraction));

		for (NerdleArg nerdleArgument : questionExtraction.getArguments()) {
			if (nerdleArgument.getNer().equals(ner)) {
				scores.add(100.0D);
			} else {
				scores.add(getArgumentScore(answerExtraction, nerdleArgument));
			}
		}

		purity = calculatePurity(scores);

		return purity;
	}

	public static double calculate(NerdleFact questionExtraction,
			NerdleFact answerExtraction) {

		double purity = 0.0;

		List<Double> scores = new ArrayList<Double>();

		// if (questionExtraction.getPredicate().getText().length() > 0) {
		// scores.add(getPredicateScore(questionExtraction, answerExtraction));
		// }

		if (questionExtraction.getSubject().getText().length() > 0) {
			scores.add(getSubjectScore(questionExtraction, answerExtraction));
		}

		for (NerdleArg nerdleArgument : questionExtraction.getArguments()) {
			scores.add(getArgumentScore(answerExtraction, nerdleArgument));
		}

		purity = calculatePurity(scores);

		return purity;
	}

	/* HELPER */

	private static double getArgumentScore(NerdleFact answerExtraction,
			NerdleArg nerdleArgument) {
		return FuzzyStringMatcher.fuzzyMaxArgumentScore(
				answerExtraction.getArguments(), nerdleArgument.getText());
	}

	private static double getSubjectScore(NerdleFact questionExtraction,
			NerdleFact answerExtraction) {
		return FuzzyStringMatcher.fuzzyStringScoreNGramm(answerExtraction
				.getSubject().getText(), questionExtraction.getSubject()
				.getText());
	}

	private static double getPredicateScore(NerdleFact questionExtraction,
			NerdleFact answerExtraction) {
		return FuzzyStringMatcher.fuzzyStringScoreNGramm(answerExtraction
				.getPredicate().getText(), questionExtraction.getPredicate()
				.getText());
	}

	public static double calculatePurity(List<Double> scores) {
		double sum = 0.0;
		for (Double score : scores) {
			sum += score;
		}
		return sum / scores.size();
	}
}
