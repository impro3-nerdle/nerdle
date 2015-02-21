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
