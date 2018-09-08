package org.warn.noadsbrowser.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.noadsbrowser.util.BrowserFileUtils;
import org.warn.noadsbrowser.util.JsonPropertyUtils;

public class BrowserCache {
	
	private static final String CACHE_DIR = "cache";
	private static final String CACHE_CONTENT_DIR = "content";
	private static final String HISTORY_FILE = "history.json";
	private static final Logger LOGGER = LoggerFactory.getLogger( BrowserCache.class );
	
	private static List<String> visitedUrls = new ArrayList<String>();
	
	static {
		if( BrowserFileUtils.exists( CACHE_DIR, HISTORY_FILE ) ) {
			LOGGER.info("Loading history file - " + HISTORY_FILE);
			visitedUrls = JsonPropertyUtils.loadList( CACHE_DIR, HISTORY_FILE );
		}
	}

	public static void writeToCache( String url, String content ) {
		if( BrowserFileUtils.checkOrCreateDir( CACHE_DIR, CACHE_CONTENT_DIR ) ) {
			String fileName = DigestUtils.sha1Hex(url);
			LOGGER.debug("Adding page to cache - " + fileName + " = " + url);
			BrowserFileUtils.writeToHomeDir( content, CACHE_DIR, CACHE_CONTENT_DIR, fileName );
		}
	}
	
	public static StringBuilder loadFromCache( String url ) {
		if( BrowserFileUtils.checkOrCreateDir( CACHE_DIR, CACHE_CONTENT_DIR ) ) {
			String fileName = DigestUtils.sha1Hex(url);
			LOGGER.debug("Checking cache for file - " + fileName + " - " + url);
			StringBuilder content = BrowserFileUtils.readFromHomeDir( CACHE_DIR, CACHE_CONTENT_DIR, fileName );
			return content;
		}
		return null;
	}
	
	public static boolean clearCache() {
		if( BrowserFileUtils.checkOrCreateDir( CACHE_DIR ) ) {
			return BrowserFileUtils.clearDir( CACHE_DIR );
		}
		return false;
	}
	
	public static void addVisitedUrl( String url ) {
		if( !visitedUrls.contains(url) ) {
			visitedUrls.add(url);
			JsonPropertyUtils.updateList( visitedUrls, CACHE_DIR, HISTORY_FILE );
		}
	}
	
	public static List<String> getVisitedUrls( String text ) {
		List<String> resultUrls = new ArrayList<String>();
		for( String url: visitedUrls ) {
			if( url.contains(text) ) {
				resultUrls.add(url);
			}
		}
		return resultUrls;
	}
	
	public static List<String> getVisitedUrls() {
		return visitedUrls;
	}
}
