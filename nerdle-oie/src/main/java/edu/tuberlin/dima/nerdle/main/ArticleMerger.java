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

package edu.tuberlin.dima.nerdle.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

public class ArticleMerger {

	public static void main(String[] args) throws IOException {

		File inputDir = new File(args[0]);
		if (!inputDir.isDirectory()) {
			System.err.println(inputDir);
			System.err.println("Directory Second does not exist.");
			System.exit(1);
		}

		File output = new File(args[1]);
		if (!output.isDirectory()) {
			System.err.println(output);
			System.err.println("Directory output does not exist.");
			System.exit(1);
		}

		File[] filesURLS = inputDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		});

		BufferedReader reader = null;
		BufferedWriter writer = new BufferedWriter(new FileWriter(output
				+ "/output.txt"));

		for (File file : filesURLS) {
			reader = new BufferedReader(new FileReader(file));
			String originalText = null;
			while ((originalText = reader.readLine()) != null) {
				writer.write(originalText);
				writer.newLine();
			}

			reader.close();

		}
		writer.flush();
		writer.close();

	}
}
