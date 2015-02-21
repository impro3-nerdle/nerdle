package edu.tuberlin.dima.nerdle.model;

import java.util.ArrayList;
import java.util.List;

public class NerdleFact {

	private String sentence;

	private String source;

	private NerdlePredicate predicate;

	private NerdleSubject subject;

	private List<NerdleArg> arguments;

	private double confidence;
	
	private double matchingScore;

	public double getMatchingScore() {
		return matchingScore;
	}

	public void setMatchingScore(double matchingScore) {
		this.matchingScore = matchingScore;
	}

	/**
	 * @return the sentence
	 */
	public String getSentence() {
		return sentence;
	}

	/**
	 * @param sentence
	 *            the sentence to set
	 */
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the predicate
	 */
	public NerdlePredicate getPredicate() {
		return predicate;
	}

	/**
	 * @param predicate
	 *            the predicate to set
	 */
	public void setPredicate(NerdlePredicate predicate) {
		this.predicate = predicate;
	}

	/**
	 * @return the subject
	 */
	public NerdleSubject getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(NerdleSubject subject) {
		this.subject = subject;
	}

	/**
	 * @return the arguments
	 */
	public List<NerdleArg> getArguments() {
		return arguments;
	}

	/**
	 * @param arguments
	 *            the arguments to set
	 */
	public void setArguments(List<NerdleArg> arguments) {
		this.arguments = arguments;
	}

	/**
	 * @return the confidence
	 */
	public double getConfidence() {
		return confidence;
	}

	/**
	 * @param confidence
	 *            the confidence to set
	 */
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public NerdleFact() {
		this.arguments = new ArrayList<NerdleArg>();
	}

	public NerdleFact(String sentence, String source,
			NerdlePredicate predicate, NerdleSubject subject,
			List<NerdleArg> arguments, double confidence) {
		this.sentence = sentence;
		this.source = source;
		this.predicate = predicate;
		this.subject = subject;
		this.arguments = arguments;
		this.confidence = confidence;
	}

	public NerdleFact(String sentence, String source, double confidence) {
		this.sentence = sentence;
		this.source = source;
		this.confidence = confidence;
		this.arguments = new ArrayList<NerdleArg>();
	}

	public void addArgument(NerdleArg arg) {
		arguments.add(arg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NerdleFact [predicate=" + predicate + ", subject=" + subject
				+ ", source=" + source + ", arguments=" + arguments
				+ ", confidence=" + confidence + ", matchingScore=" + matchingScore + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		long temp;
		temp = Double.doubleToLongBits(confidence);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((predicate == null) ? 0 : predicate.hashCode());
		result = prime * result
				+ ((sentence == null) ? 0 : sentence.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NerdleFact other = (NerdleFact) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (Double.doubleToLongBits(confidence) != Double
				.doubleToLongBits(other.confidence))
			return false;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		if (sentence == null) {
			if (other.sentence != null)
				return false;
		} else if (!sentence.equals(other.sentence))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}
	


}
