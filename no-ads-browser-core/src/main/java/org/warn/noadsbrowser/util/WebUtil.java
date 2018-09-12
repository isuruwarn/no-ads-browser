package org.warn.noadsbrowser.util;

public class WebUtil {
	
	private static final String GOOGLE_URL_PATTERN = "https://www.google.com/url?";
	private static final String FACEBOOK_URL_PATTERN = "https://l.facebook.com/l.php?";
	
	public static String formatUrl( String url ) {
		if( url!=null ) {
			url = url.trim()
					.replaceAll("%3A", ":")
					.replaceAll("%2F", "/")
					.replaceAll("%3D", "=")
					.replaceAll("%3F", "?")
					.replaceAll( "%23", "#");
			
			if( !url.startsWith( GlobalConstants.FILE_PROTOCOL ) ) {
				url = url.replaceAll(" ", "+");
			}
			
			if( url.startsWith(GOOGLE_URL_PATTERN) ) {
				url = getTargetUrl( url, GOOGLE_URL_PATTERN, "url=" );
			
			} else if( url.startsWith(FACEBOOK_URL_PATTERN) ) {
				url = getTargetUrl( url, FACEBOOK_URL_PATTERN, "u=" );
			}
		}
		return url;
	}
	
	private static String getTargetUrl( String url, String hostPattern, String param ) {
		String targetUrl = null;
		url = url.replace( hostPattern, "");
		String [] elements = url.split("&");
		for( String el: elements ) {
			if( el.startsWith( param ) ) {
				targetUrl = el.substring( el.indexOf("=")+1 );
			}
		}
		return targetUrl;
	}
	
	public static boolean validUrl( String url ) {
		if( url!=null && !url.equals("") && 
				( url.startsWith( GlobalConstants.HTTP_PROTOCOL ) || url.startsWith( GlobalConstants.HTTPS_PROTOCOL )
						|| url.startsWith( GlobalConstants.FILE_PROTOCOL ) ) ) {
			return true;
		}
		return false;
	}

}
