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
