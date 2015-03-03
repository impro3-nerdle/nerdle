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

package edu.tuberlin.dima.nerdle.model;

public class NerdleArg {
	
	private String text;
	
	/** Named Entity Relation (NER): 
	 * 'L': Location, 
	 * 'T': Time   
	 */
	private String ner;

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
	 * @return the ner
	 */
	public String getNer() {
		return ner;
	}

	/**
	 * @param ner the ner to set
	 */
	public void setNer(String ner) {
		this.ner = ner;
	}

	public NerdleArg(String text, String ner) {
		this.text = text;
		this.ner = ner;
	}

	public NerdleArg() {

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NerdleArg [text=" + text + ", ner=" + ner + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ner == null) ? 0 : ner.hashCode());
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
		NerdleArg other = (NerdleArg) obj;
		if (ner == null) {
			if (other.ner != null)
				return false;
		} else if (!ner.equals(other.ner))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

}
