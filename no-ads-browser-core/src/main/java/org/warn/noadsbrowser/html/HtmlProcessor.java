package org.warn.noadsbrowser.html;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.warn.noadsbrowser.config.BrowserConfig;
import org.warn.noadsbrowser.util.GlobalConstants;

public class HtmlProcessor {
	
	private static final List<String> BLACK_LISTED_TAGS = new ArrayList<String>();
	private static final List<String> CSS_TAGS = new ArrayList<String>();
	private static final List<String> IMAGE_TAGS = new ArrayList<String>();
	
	static  {
		BLACK_LISTED_TAGS.add( GlobalConstants.SCRIPT_TAG );
		BLACK_LISTED_TAGS.add( GlobalConstants.IFRAME_TAG );
		BLACK_LISTED_TAGS.add( GlobalConstants.FORM_TAG );
		BLACK_LISTED_TAGS.add( GlobalConstants.BUTTON_TAG );
		BLACK_LISTED_TAGS.add( GlobalConstants.CANVAS_TAG );
		BLACK_LISTED_TAGS.add( GlobalConstants.SVG_TAG );
		BLACK_LISTED_TAGS.add( GlobalConstants.MAP_TAG );
		CSS_TAGS.add( GlobalConstants.LINK_TAG );
		CSS_TAGS.add( GlobalConstants.STYLE_TAG );
		IMAGE_TAGS.add( GlobalConstants.IMG_TAG );
		IMAGE_TAGS.add( GlobalConstants.PICTURE_TAG );
		IMAGE_TAGS.add( GlobalConstants.FIGURE_TAG );
		IMAGE_TAGS.add( GlobalConstants.VIDEO_TAG );
	}
	
	public static Page process( StringBuilder data ) {
		Page dto = null;
		if( data != null && data.length()>0 ) {
			boolean cssDisabled = Boolean.valueOf( BrowserConfig.getProperty( BrowserConfig.PROP_DISABLE_CSS ) );
			boolean imagesDisabled = Boolean.valueOf( BrowserConfig.getProperty( BrowserConfig.PROP_DISABLE_IMAGES ) );
			boolean removeMetaTags = Boolean.valueOf( BrowserConfig.getProperty( BrowserConfig.PROP_REMOVE_META_TAGS ) );
			boolean removeDataAttrs = Boolean.valueOf( BrowserConfig.getProperty( BrowserConfig.PROP_REMOVE_DATA_ATTRS ) );
			dto = process( data, cssDisabled, imagesDisabled, removeMetaTags, removeDataAttrs );
		}
		return dto;
	}
	
	public static Page process( StringBuilder data, boolean cssDisabled, boolean imagesDisabled, boolean removeMetaTags, boolean removeDataAttrs ) {
		Page dto = null;
		if( data != null && data.length()>0 ) {
			Document doc = Jsoup.parse( data.toString() );
			if( doc != null ) {
				sanitize( doc, cssDisabled, imagesDisabled, removeMetaTags, removeDataAttrs );
				dto = new Page();
				dto.setDocument(doc);
				dto.setInputHTML(data);
				dto.setOutputHTML( new StringBuilder( doc.toString() ) );
				dto.setTitle( doc.getElementsByTag(GlobalConstants.TITLE_TAG).text() );
			}
		}
		return dto;
	}
	
	public static void sanitize( Document doc, boolean cssDisabled, boolean imagesDisabled, boolean removeMetaTags, boolean removeDataAttrs ) {
		Elements elements = doc.getAllElements();
		for( Element element: elements ) {
			element.removeAttr( GlobalConstants.ONCLICK_ATTR );
			String tagName = element.tagName();
			if( BLACK_LISTED_TAGS.contains( tagName.toLowerCase() ) ) {
				element.remove();
			} else {
				if( cssDisabled ) {
					element.removeAttr( GlobalConstants.STYLE_ATTR );
					element.removeAttr( GlobalConstants.CLASS_ATTR );
					if( CSS_TAGS.contains( tagName.toLowerCase() ) ) {
						element.remove();
					}
				}
				if( imagesDisabled && element != null ) {
					if( IMAGE_TAGS.contains( tagName.toLowerCase() ) ) {
						element.remove();
					}
				}
				if( removeMetaTags && element != null ) {
					if( GlobalConstants.META_TAG.equals( tagName.toLowerCase() ) ) {
						element.remove();
					}
				}
				if( removeDataAttrs && element != null ) {
					Attributes attrs = element.attributes();
					for( Attribute a: attrs ) {
						if( a.getKey().startsWith("data-") ) {
							element.removeAttr( a.getKey() );
						}
					}
				}
			}
		}
	}
	
}