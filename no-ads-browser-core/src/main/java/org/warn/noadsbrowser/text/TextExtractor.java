package org.warn.noadsbrowser.text;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.warn.noadsbrowser.html.HtmlProcessor;
import org.warn.noadsbrowser.util.GlobalConstants;

public class TextExtractor {
	
	private static final List<String> DATA_TAGS = new ArrayList<String>();
	private static final List<String> SINGLE_LINE_TAGS = new ArrayList<String>();
	private static final List<String> DOUBLE_LINE_TAGS = new ArrayList<String>();
	private static final List<String> SINGLE_TAB_TAGS = new ArrayList<String>();
	
	static  {
		DATA_TAGS.add( GlobalConstants.TITLE_TAG );
		DATA_TAGS.add( GlobalConstants.H1_TAG );
		DATA_TAGS.add( GlobalConstants.H2_TAG );
		DATA_TAGS.add( GlobalConstants.H3_TAG );
		DATA_TAGS.add( GlobalConstants.P_TAG );
		DATA_TAGS.add( GlobalConstants.SPAN_TAG );
		//DATA_TAGS.add( GlobalConstants.DIV_TAG );
		DATA_TAGS.add( GlobalConstants.B_TAG );
		DATA_TAGS.add( GlobalConstants.STRONG_TAG );
		DATA_TAGS.add( GlobalConstants.I_TAG );
		DATA_TAGS.add( GlobalConstants.U_TAG );
//		DATA_TAGS.add( GlobalConstants.UL_TAG );
//		DATA_TAGS.add( GlobalConstants.OL_TAG );
//		DATA_TAGS.add( GlobalConstants.LI_TAG );
		DATA_TAGS.add( GlobalConstants.TABLE_TAG );
		DATA_TAGS.add( GlobalConstants.TR_TAG );
		DATA_TAGS.add( GlobalConstants.TD_TAG );
		
		DOUBLE_LINE_TAGS.add( GlobalConstants.TITLE_TAG );
		DOUBLE_LINE_TAGS.add( GlobalConstants.H1_TAG );
		DOUBLE_LINE_TAGS.add( GlobalConstants.H2_TAG );
		DOUBLE_LINE_TAGS.add( GlobalConstants.H3_TAG );
		
		SINGLE_LINE_TAGS.add( GlobalConstants.P_TAG );
		SINGLE_LINE_TAGS.add( GlobalConstants.SPAN_TAG );
		SINGLE_LINE_TAGS.add( GlobalConstants.DIV_TAG );
		SINGLE_LINE_TAGS.add( GlobalConstants.TABLE_TAG );
		SINGLE_LINE_TAGS.add( GlobalConstants.TR_TAG );
		SINGLE_LINE_TAGS.add( GlobalConstants.UL_TAG );
		SINGLE_LINE_TAGS.add( GlobalConstants.OL_TAG );
		SINGLE_LINE_TAGS.add( GlobalConstants.LI_TAG );
		
		SINGLE_TAB_TAGS.add( GlobalConstants.TD_TAG );
	}
	
	public static StringBuilder extract( Document doc ) {
		StringBuilder outputText = null;
		if( doc != null ) {
			outputText = new StringBuilder();
			doc.normalise();
			HtmlProcessor.sanitize( doc, true, true, true, true );
			process( outputText, doc.children() );
		}
		return outputText;
	}
	
	private static void process( StringBuilder outputText, Elements elements ) {
		for( Element el: elements ) {
			String tagName = el.tagName();
			if( el.attr("id").contains("nav") ) {
				el.remove();
			} else if( el.hasText() && DATA_TAGS.contains( tagName.toLowerCase() ) ) {
				String text = el.text();
				outputText.append( text );
				appendLineEnd( outputText, tagName );
			} else {
				Elements children = el.children();
				if( children != null && children.size() > 0 ) {
					process( outputText, children );
				}
			}
		}
	}
	
	private static void appendLineEnd( StringBuilder outputText, String tagName ) {
		if( SINGLE_LINE_TAGS.contains( tagName ) ) {
			char lastChar = outputText.charAt( outputText.length()-1 );
			if( lastChar == '\n' || lastChar == '\r' ) {
				outputText.append("\n");
			} else {
				outputText.append("\n\n");
			}
		} else if( DOUBLE_LINE_TAGS.contains( tagName ) ) {
			outputText.append("\n\n");
		} else if ( SINGLE_TAB_TAGS.contains( tagName ) ) {
			outputText.append("\t");
		}
	}
	
}