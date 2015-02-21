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

package edu.tuberlin.dima.nerdle.crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class WikiaCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz)|[?].*)$");

	private final static String NAMESPACE_FILTERS = ".*(User:|Talk:|User_talk:|Help:|Category:|Blog:|User_blog:|Forum:|Special:|Template:|File:|"
			+ "Main:|Image:|File_talk:|Help_talk:|Category_talk:|Project:|Project_talk:|Template_talk:|Shortcut:|";

	public static String wikia;
	public static String domain;
	public static String subject;
	public static int minContentSize;

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && href.startsWith(domain);
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().toString();
		String text = "";
		String pageText = "";
		int id = page.getWebURL().getDocid();
		try {
			if (!url.matches(NAMESPACE_FILTERS + wikia + ").*")) {
				System.out.println(url);
				new FileWriter("data/crawl/" + subject + "-urls.txt",true).append(
						id + " - " + url+"\n\r").close();
				if (page.getParseData() instanceof HtmlParseData) {
					HtmlParseData htmlParseData = (HtmlParseData) page
							.getParseData();
					Document doc = Jsoup.parse(htmlParseData.getHtml());
					Elements elems = doc.select("#WikiaArticle").select("#mw-content-text");
					Elements Contents = elems.select("p");
					Elements dlTags = elems.select("dl");
					Elements liTags = elems.select("ul").select("li");
					for(Element p: Contents){
						if (p.text().length() >= minContentSize){
							pageText += p.text() + " \n";
						}
					}
					for(Element dl: dlTags){
						if (dl.text().length() >= minContentSize){
							pageText += dl.text() + " \n";
						}
					}
					for(Element li: liTags){
						if (li.text().length() >= minContentSize){
							pageText += li.text() + " \n";
						}
					}
					
					text = pageText;
					text = text.replaceAll("\\[[0-9]*\\]", "");
					text = text.replaceAll("\\s+", " ");

					File textFile = new File("data/crawl/" + subject + "/" + id
							+ ".txt");
					FileWriter fileWriter = new FileWriter(textFile);
					fileWriter.append(text);
					fileWriter.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
