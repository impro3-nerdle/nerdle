package edu.tuberlin.dima.nerdle.model;

import java.util.ArrayList;
import java.util.List;

public class NerdlePredicate {
	
	private String text;
	
	private String lemma;
	
	private List<String> synonyms;

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the lemma
	 */
	public String getLemma() {
		return lemma;
	}

	/**
	 * @param lemma the lemma to set
	 */
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	/**
	 * @return the synonyms
	 */
	public List<String> getSynonyms() {
		return synonyms;
	}

	/**
	 * @param synonyms the synonyms to set
	 */
	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}

	public NerdlePredicate(String text, String lemma, List<String> synonyms) {
		this.text = text;
		this.lemma = lemma;
		this.synonyms = synonyms;
	}

	public NerdlePredicate(String text, String lemma) {
		this.text = text;
		this.lemma = lemma;
		this.synonyms = new ArrayList<String>();
	}

	public NerdlePredicate() {
		this.synonyms = new ArrayList<String>();
	}
	
	public void addSynonyms(String synonym) {
		this.synonyms.add(synonym);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NerdlePredicate [text=" + text + ", lemma=" + lemma
				+ ", synonyms=" + synonyms + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lemma == null) ? 0 : lemma.hashCode());
		result = prime * result
				+ ((synonyms == null) ? 0 : synonyms.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		NerdlePredicate other = (NerdlePredicate) obj;
		if (lemma == null) {
			if (other.lemma != null)
				return false;
		} else if (!lemma.equals(other.lemma))
			return false;
		if (synonyms == null) {
			if (other.synonyms != null)
				return false;
		} else if (!synonyms.containsAll(other.synonyms))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
}
