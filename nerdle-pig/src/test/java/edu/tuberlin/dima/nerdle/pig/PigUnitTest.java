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

import java.io.IOException;
import java.util.Iterator;

import org.apache.pig.data.Tuple;
import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;

public class PigUnitTest {

	protected void printAlias(PigTest test, String alias) throws IOException,
			ParseException {
		System.out.println("Printing: " + alias);
		Iterator<Tuple> iterator = test.getAlias(alias);
		while (iterator.hasNext()) {
			Tuple next = iterator.next();
			System.out.println(next);
		}
	}

}
