package com.tek.rssreader;

public class Main {
	public static void main(String[] args) {
		String url = "http://feeds.reuters.com/reuters/topNews?format=xml";
		RssParser parser = new RssParser(url);
		RssFeed feed = parser.parseRss();
		
		if(feed!=null && feed.getItems()!=null) {
			for(RssItem item : feed.getItems())
				System.out.println(item);
		}
	}
}
