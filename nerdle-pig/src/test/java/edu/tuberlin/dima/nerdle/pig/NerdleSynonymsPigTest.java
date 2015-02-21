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

package edu.tuberlin.dima.nerdle.pig;

import org.apache.pig.pigunit.PigTest;
import org.junit.Test;

import com.google.common.io.Resources;

public class NerdleSynonymsPigTest extends PigUnitTest {

	@Test
	public void test() throws Exception {
		String pigFile = Resources.getResource("nerdle_synonyms.pig").getPath();

		String[] args = { "input="
				+ Resources.getResource("output_facts_lemma").getPath() };

		PigTest test = new PigTest(pigFile, args);

		test.unoverride("STORE");

		printAlias(test, "facts");
	}

}
