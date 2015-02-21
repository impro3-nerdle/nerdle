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
import java.nio.charset.Charset;
import java.util.HashMap;

import com.google.common.io.Files;

public class ArticleCleaner {

	public static void main(String[] args) throws IOException {

		File inputDirSecond = new File(args[1]);
		if (!inputDirSecond.isDirectory()) {
			System.err.println(inputDirSecond);
			System.err.println("Directory Second does not exist.");
			System.exit(1);
		}

		File output = new File(args[2]);
		if (!output.isDirectory()) {
			System.err.println(output);
			System.err.println("Directory output does not exist.");
			System.exit(1);
		}

		File[] filesURLS = inputDirSecond.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		});

		HashMap<Integer, String> numberToURLMap = new HashMap<Integer, String>();

		File file = new File(args[0]);

		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(file));
		String originalText = null;
		while ((originalText = reader.readLine()) != null) {

			String[] splitted = originalText.split(" - ");
			String number = splitted[0];
			String url = splitted[1];
			String hashNumber = numberToURLMap.get(Integer.parseInt(number));
			if (hashNumber == null) {
				numberToURLMap.put(Integer.parseInt(number), url);
			}
		}

		reader.close();

		BufferedWriter writer = null;
		for (File filesecond : filesURLS) {
			String originalTextSecond = Files.toString(filesecond,
					Charset.forName("UTF-8"));

			String formattedText = originalTextSecond
					.replaceAll("\\t|\\n", " ");

			String fileWithoutTxt = filesecond.getName().replace(".txt", "");

			if (numberToURLMap.get(Integer.parseInt(fileWithoutTxt)) != null) {
				File outputFile = new File(args[2] + "/" + filesecond.getName());
				writer = new BufferedWriter(new FileWriter(outputFile));
				writer.write(numberToURLMap.get(Integer
						.parseInt(fileWithoutTxt)) + "\t" + formattedText);

			}

			writer.flush();

			writer.close();

		}

	}
}
