package edu.tuberlin.dima.nerdle;

import scala.collection.Seq;
import edu.knowitall.tool.coref.StanfordCoreferenceResolver;
import edu.knowitall.tool.coref.Substitution;

public enum CoreferencesHelper {
	INSTANCE;
	
	private StanfordCoreferenceResolver coref = new StanfordCoreferenceResolver();
		
	/**
	 * removes coreferences in the given text
	 * 
	 * @param text
	 * @return coreferences substituted text
	 */
	public String resolveCoreferences(String text) {
		Seq<Substitution> subs = coref.substitutions(text);
		return StanfordCoreferenceResolver.substitute(text, subs);
	}

}
