package edu.tuberlin.dima.nerdle.crawler;

import java.io.File;

import org.apache.commons.configuration.PropertiesConfiguration;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	public static void main(String[] args) throws Exception {
		int numberOfCrawlers = 12;
		
		if(args.length != 1){
			System.err.println("Please specify a properties file.");
			System.exit(1);
		}

		PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(args[0]);
		
		WikiaCrawler.domain = propertiesConfiguration.getString("allowedDomain");
		WikiaCrawler.minContentSize = propertiesConfiguration.getInt("minContentSize");
		WikiaCrawler.subject = propertiesConfiguration.getString("subject");
		WikiaCrawler.wikia = propertiesConfiguration.getString("wikia");
		CrawlConfig config = new CrawlConfig();
		String crawlStorageFolder = "data/crawl/"+WikiaCrawler.subject+"/root";
		config.setCrawlStorageFolder(crawlStorageFolder);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher,
				robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		String[] seeds = propertiesConfiguration.getStringArray("seeds");
		for (int i = 0; i < seeds.length; i++) {
			controller.addSeed(seeds[i]);
		}
		
		String subject = propertiesConfiguration.getString("subject");
		new File("data/crawl/" + subject + "-urls.txt").createNewFile();
		new File("data/crawl/" + subject + "/").mkdirs();

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(WikiaCrawler.class, numberOfCrawlers);
	}
}
