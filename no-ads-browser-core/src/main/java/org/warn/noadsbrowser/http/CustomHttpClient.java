package org.warn.noadsbrowser.http;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.noadsbrowser.util.BrowserFileUtils;
import org.warn.noadsbrowser.util.GlobalConstants;

public class CustomHttpClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( CustomHttpClient.class );
	
	public static StringBuilder getWebContent( String url) {
		
		URLConnection con = null;
		StringBuilder webContent = new StringBuilder();
		
		try {
			LOGGER.debug("Request URL - " + url);
			URL urlObj = new URL(url);
			con = urlObj.openConnection();
			con.setRequestProperty("User-Agent", "Java Client");
			con.setRequestProperty("Accept", "text/html");
			con.setRequestProperty("Accept-Language", "en-US");
			con.setRequestProperty("Connection", "close");
			
			InputStream in = con.getInputStream();
			InputStreamReader insr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(insr);
			getReponseStatus( con );
			String input;
			while( (input = br.readLine()) != null ) {
				webContent.append(input);
			}
			br.close();
			
		} catch( MalformedURLException e ) {
			LOGGER.error("Invalid URL", e);
			String status = getReponseStatus( con );
			webContent.append( status );
			
		} catch( FileNotFoundException e ) {
			LOGGER.error("Invalid URL", e);
			String status = getReponseStatus( con );
			webContent.append( status );
			
		} catch( UnknownHostException e ) {
			LOGGER.error("Invalid URL", e);
			e.printStackTrace();
			return BrowserFileUtils.readResource( GlobalConstants.UNKNOWN_HOST_ERROR_HTML );
			
		} catch( IOException e ) {
			LOGGER.error("Error while connecting to website", e);
			e.printStackTrace();
			String status = getReponseStatus( con );
			webContent.append( status );
		}
		
		return webContent;
	}
	
	private static String getReponseStatus( URLConnection con ) {
		String status = con!=null?con.getHeaderField(null):"Unknown error";
		LOGGER.debug("Response Status - " + status);
		return status;
	}

}
