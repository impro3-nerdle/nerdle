package edu.tuberlin.dima.nerdle.qa;

import java.util.HashMap;

import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class QuestionAnswerResponse {
	
	private HashMap<NerdleFact, Double> answerToScore;
	private HashMap<NerdleFact, NerdleFact> answerToQuestion;
	
//	private HashMap<NerdleExtraction, HashMap<NerdleExtraction, Double>> map;
	
	private QuestionType questionType;

	public QuestionAnswerResponse(
			HashMap<NerdleFact, Double> answerToScore,
			HashMap<NerdleFact, NerdleFact> answerToQuestion,
			QuestionType questionType) {
		super();
		this.answerToScore = answerToScore;
		this.answerToQuestion = answerToQuestion;
		this.questionType = questionType;
	}

	/**
	 * @return the answerToScore
	 */
	public HashMap<NerdleFact, Double> getAnswerToScore() {
		return answerToScore;
	}

	/**
	 * @param answerToScore the answerToScore to set
	 */
	public void setAnswerToScore(HashMap<NerdleFact, Double> answerToScore) {
		this.answerToScore = answerToScore;
	}

	/**
	 * @return the answerToQuestion
	 */
	public HashMap<NerdleFact, NerdleFact> getAnswerToQuestion() {
		return answerToQuestion;
	}

	/**
	 * @param answerToQuestion the answerToQuestion to set
	 */
	public void setAnswerToQuestion(
			HashMap<NerdleFact, NerdleFact> answerToQuestion) {
		this.answerToQuestion = answerToQuestion;
	}

	/**
	 * @return the questionType
	 */
	public QuestionType getQuestionType() {
		return questionType;
	}

	/**
	 * @param questionType the questionType to set
	 */
	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}

}
