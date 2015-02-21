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

package edu.tuberlin.dima.nerdle.qa;

import java.util.HashMap;

import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class QuestionAnswerResponse {
	
	private HashMap<NerdleFact, Double> answerToScore;
	private HashMap<NerdleFact, NerdleFact> answerToQuestion;
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
