package org.warn.noadsbrowser.html;

import org.warn.noadsbrowser.html.dom.DOMTree;

public class CustomHtmlParser {
	
	private char[] chars;
	private StringBuilder data;
	
	public CustomHtmlParser( StringBuilder data ) {
		this.data = data;
		if( data != null && data.length()>0 ) {
			this.chars = data.toString().toCharArray();
		}
	}
	
	public DOMTree parse( int currentIndex ) {
		DOMTree tree = null;
		if( chars != null && chars.length>0 ) {
			tree = new DOMTree();
			while( currentIndex<chars.length ) {
				char c = chars[currentIndex];
				//System.out.println(c);
				if( c == '<') {
				}
				currentIndex++;
			}
		}
		return tree;
	}

	public StringBuilder getData() {
		return data;
	}
	
	
	/*
	private static final String DOC_TAG = "<!DOCTYPE";
	private static final String DOC__TAG = "<!DOCTYPE ";
	private static final String META__TAG = "<meta ";
	private static final String META_END_TAG = "</meta>";
	private static final String IFRAME_TAG = "<iframe>";
	private static final String IFRAME__TAG = "<iframe ";
	private static final String IFRAME_END_TAG = "</iframe>";
	private static final String HTML_TAG = "<html>";
	private static final String HTML__TAG = "<html ";
	private static final String HTML_END_TAG = "</html>";
	private static final String HEAD_TAG = "<head>";
	private static final String HEAD__TAG = "<head ";
	private static final String HEAD_END_TAG = "</head>";
	private static final String TITLE_TAG = "<title>";
	private static final String TITLE__TAG = "<title ";
	private static final String TITLE_END_TAG = "</title>";
	private static final String BODY_TAG = "<body>";
	private static final String BODY__TAG = "<body ";
	private static final String BODY_END_TAG = "</body>";
	private static final String SCRIPT_TAG = "<script>";
	private static final String SCRIPT__TAG = "<script ";
	private static final String SCRIPT_END_TAG = "</script>";
	private static final String DIV_TAG = "<div>";
	private static final String DIV__TAG = "<div ";
	private static final String DIV_END_TAG = "</div>";
	private static final String SPAN_TAG = "<span>";
	private static final String SPAN__TAG = "<span ";
	private static final String SPAN_END_TAG = "</span>";
	private static final String STYLE_TAG = "<style>";
	private static final String STYLE__TAG = "<style ";
	private static final String STYLE_END_TAG = "</style>";
	private static final String P_TAG = "<p>";
	private static final String P__TAG = "<p ";
	private static final String P_END_TAG = "</p>";
	private static final String A_TAG = "<a>";
	private static final String A__TAG = "<a ";
	private static final String A_END_TAG = "</a>";
	private static final String B_TAG = "<b>";
	private static final String B__TAG = "<b ";
	private static final String B_END_TAG = "</b>";
	private static final String U_TAG = "<u>";
	private static final String U__TAG = "<u ";
	private static final String U_END_TAG = "</u>";
	private static final String I_TAG = "<i>";
	private static final String I__TAG = "<i ";
	private static final String I_END_TAG = "</i>";
	private static final String UL_TAG = "<ul>";
	private static final String UL__TAG = "<ul ";
	private static final String UL_END_TAG = "</ul>";
	private static final String LI_TAG = "<li>";
	private static final String LI__TAG = "<li ";
	private static final String LI_END_TAG = "</li>";
	private static final String END_INLINE_EL = "/>";
	
	private int currentTagSize;
	private char[] chars;
	private StringBuilder data;
	private StringBuilder parsedHTML = new StringBuilder();
	private Integer currentIndex = new Integer(0);
	private List<String> currentEndTags = new ArrayList<String>(); // end tags we are interested in
	private List<String> dataTags = new ArrayList<String>();
	
	public CustomHtmlParser( StringBuilder data ) {
		this.data = data;
		chars = data.toString().toCharArray();
		dataTags.add(TITLE_TAG);
		dataTags.add(TITLE__TAG);
		dataTags.add(P_TAG);
		dataTags.add(P__TAG);
		dataTags.add(SPAN_TAG);
		dataTags.add(SPAN__TAG);
		dataTags.add(A_TAG);
		dataTags.add(A__TAG);
		dataTags.add(B_TAG);
		dataTags.add(B__TAG);
		dataTags.add(I_TAG);
		dataTags.add(I__TAG);
		dataTags.add(U_TAG);
		dataTags.add(U__TAG);
	}

	public StringBuilder read() {
		if( chars != null && chars.length>0 ) {
			while( currentIndex<chars.length ) {
				char c = chars[currentIndex];
				System.out.println(c);
				if( c == '<') {
					if( reachedClosingTag() ) {
						moveToCurrentTagEnd();
					} else {
						Tag tag = readTag(); // returns tag object if current tag is valid
						if( tag!=null ) {
							//System.out.println(tag.getElementData());
							print( readData() );
						}
					}
				} else if( currentEndTags.size() > 0 ) { // still in data state
					print( readData() );
				} else {
					currentIndex++;
				}
			}
		}
		return parsedHTML;
	}
	
	private Tag readTag() {
		Tag tag = null;
		int beginIndex = currentIndex;
		for( currentIndex++; currentIndex<chars.length-1; currentIndex++ ) {
			char c = chars[currentIndex];
			if( c == '>' ) {
				String tagElement = data.substring( beginIndex, currentIndex+1 );
				tag = getTag( tagElement, dataTags );
				if( tag!=null ) {
					currentEndTags.add(tag.getTagCloseType());
				}
				currentIndex++;
				break;
			}
		}
		return tag;
	}

	private Tag getTag( String tagElement, List<String> tagTypes ) {
		if( tagElement!=null ) {
			for( String tagType: tagTypes ) {
				if( tagElement.toLowerCase().startsWith( tagType.toLowerCase() ) ) {
					currentTagSize = tagElement.length();
					return new Tag( tagType, tagElement );
				}
			}
		}
		return null;
	}
	
	private String readData() {
		int beginIndex = currentIndex;
		for( ; currentIndex<chars.length; ) {
			char c = chars[currentIndex];
			if( c == '<' ) {
				if( reachedClosingTag() ) {
					String tagContent = data.substring( beginIndex, currentIndex );
					moveToCurrentTagEnd();
					return tagContent;
				} else {
					// reached new open tag
					String tagContent = data.substring( beginIndex, currentIndex );
					return tagContent;
				}
			} else {
				currentIndex++;
			}
		}
		return null;
	}
	
	private boolean reachedClosingTag() {
		if( currentEndTags.size() == 0 ) {
			return false;
		}
		String closingTag = currentEndTags.get( currentEndTags.size()-1 );
		for(int i=0; i<closingTag.length(); i++) {
			char c1 = closingTag.charAt(i);
			char c2 = chars[currentIndex+i];
			if( c1 != c2 )
				return false;
		}
		currentTagSize = closingTag.length();
		currentEndTags.remove( currentEndTags.size()-1 );
		return true;
	}
	
	private void moveToCurrentTagEnd() {
		currentIndex = currentIndex + currentTagSize;
	}
	
	private void print(String tagContent) {
		if( tagContent!=null && !tagContent.equals("") ) {
			if( currentEndTags.size() > 0 ) { // data state
				System.out.print(tagContent);
				parsedHTML.append(tagContent);
			} else {
				System.out.println(tagContent);
				parsedHTML.append(tagContent + "\n");
			}
		}
	}
	
	public static class Tag {
		
		private String tagType; // <html>, </html>, <script , <script>, etc
		private String tagCloseType;
		private String elementData; // actual content, including name and attributes. eg - <html lang="en" class="story....> 
		
		public Tag( String tagType, String elementData ) {
			this.tagType = tagType;
			this.elementData = elementData;
			if( this.tagType!=null ) {
				this.tagCloseType = this.tagType.replace("<", "</").replace(" ", ">");
			}
		}

		public String getTagType() {
			return tagType;
		}

		public void setTagType(String tagType) {
			this.tagType = tagType;
		}

		public String getElementData() {
			return elementData;
		}

		public void setElementData(String elementData) {
			this.elementData = elementData;
		}

		public String getTagCloseType() {
			return tagCloseType;
		}

		public void setTagCloseType(String tagCloseType) {
			this.tagCloseType = tagCloseType;
		}
		
	}
	*/
}