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

import java.util.List;

import org.junit.Test;

public class StanfordCoreNLPHelpTest {

	@Test
	public void test() {
		StanfordCoreNLPHelper stanfordCoreNLPHelper = StanfordCoreNLPHelper.getInstance();
		
		List<String> lemmas = stanfordCoreNLPHelper.getLemma("murder");
		
		for (String string : lemmas) {
			System.out.println();
			System.out.println(string);
		}

		// List<String> lemmas2 = helper.getLemma("burned");
		// for (String string : lemmas2) {
		// System.out.println();
		// System.out.println(string);
		// }
		//
		// List<String> lemmas3 = helper.getLemma("should be forbidden");
		// for (String string : lemmas3) {
		// System.out.println();
		// System.out.println(string);
		// }
	}

}
