package org.warn.noadsbrowser.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.noadsbrowser.util.BrowserFileUtils;
import org.warn.noadsbrowser.util.JsonPropertyUtils;

public class BrowserConfig {
	
	public static final String PROP_DISPLAY_MODE = "displayMode";
	public static final String PROP_DISABLE_CSS = "disableCss";
	public static final String PROP_DISABLE_IMAGES = "disableImages";
	public static final String PROP_REMOVE_META_TAGS = "removeMetaTags";
	public static final String PROP_REMOVE_DATA_ATTRS = "removeDataAttrs";
	public static final String PROP_VAL_DISPLAY_MODE_HTML = "html";
	public static final String PROP_VAL_DISPLAY_MODE_TEXT = "text";
	
	private static final String CONFIG_FILE = "config.json";
	private static final Logger LOGGER = LoggerFactory.getLogger( BrowserConfig.class );
	
	private static ConcurrentMap<String, String> props;
	
	static {
		if( BrowserFileUtils.exists( CONFIG_FILE ) ) {
			LOGGER.info("Loading configurations from file - " + CONFIG_FILE);
			props = JsonPropertyUtils.loadMap(CONFIG_FILE);
		}
		if( props == null ) {
			LOGGER.info("Loading default configurations");
			props = new ConcurrentHashMap<String, String>();
			props.put( PROP_DISPLAY_MODE, PROP_VAL_DISPLAY_MODE_HTML );
		}
	}
	
	public static void updateConfig( String property, String value ) {
		String existingValue = props.get(property);
		if( existingValue == null || existingValue.equals("") || !existingValue.equals(value) ) {
			LOGGER.debug("Updating Property={}, ExistingValue={}, NewValue={}", property, existingValue, value );
			props.put( property, value );
			JsonPropertyUtils.updateMap( props, CONFIG_FILE );
		}
	}
	
	public static String getProperty( String property ) {
		return (String) props.get(property);
	}
	
}
