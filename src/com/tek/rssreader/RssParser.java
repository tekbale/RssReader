package com.tek.rssreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

public class RssParser {
	private URL url;

	public RssParser(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public RssFeed parseRss() {
		RssFeed rssFeed = null;
		RssItem item = null;
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream inputStream = this.url.openStream();
			XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);
			boolean newItem=false;
			
			while(eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if(event.isStartElement()) {
					String localPart = event.asStartElement().getName().getLocalPart();
					//System.out.println("<"+localPart+">");
					switch(localPart) {
						case "channel":
							rssFeed = new RssFeed();
							break;
						
						case "item":
							item = new RssItem();
							newItem=true;
							break;
							
						case "title":
							if(newItem) {
								item.setTitle(getNodeData(event, eventReader));
								//System.out.println("\t\t"+item.getTitle());
							}
							break;
							
						case "link":
							if(newItem) {
								item.setLink(getNodeData(event, eventReader));
							}
							break;
							
						case "description":
							if(newItem) {
								item.setDesc(getNodeData(event, eventReader));
							}
							break;
							
						case "pubDate":
							if(newItem) {
								item.setPubDate(getNodeData(event, eventReader));
							}
							break;
							
						case "category":
							if(newItem) {
								item.setCategory(getNodeData(event, eventReader));
							}
							break;
							
						default:
							break;
					}
				} else if(event.isEndElement()) {
					String localPart = event.asEndElement().getName().getLocalPart();
					//System.out.println("</"+localPart+">");
					
					switch(localPart) {
						case "item":
							rssFeed.getItems().add(item);
							newItem=false;
							break;
						
						default:
							break;
					}
				}
			}
			
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rssFeed;
	}
	
	private String getNodeData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
		String res="";
		event = eventReader.nextEvent();
		if(event instanceof Characters)
			res = event.asCharacters().getData();
		return res;
	}
}
