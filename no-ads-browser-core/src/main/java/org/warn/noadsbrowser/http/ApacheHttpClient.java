package org.warn.noadsbrowser.http;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.noadsbrowser.util.BrowserFileUtils;
import org.warn.noadsbrowser.util.GlobalConstants;

public class ApacheHttpClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApacheHttpClient.class);
	
	public static HttpDTO read( String url) {
		
		HttpDTO dto = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(url);
			LOGGER.debug("Request URL - " + httpget.getRequestLine());
			
			HttpResponseHandler responseHandler = new HttpResponseHandler();
			httpclient.execute( httpget, responseHandler );
			dto = responseHandler.getHttpDTO();
			
		} catch (ClientProtocolException e) {
			LOGGER.error("Error while establishing connection", e);
		
		} catch( UnknownHostException e ) {
			LOGGER.error("Error while establishing connection - " + e.getMessage());
			dto = new HttpDTO( 0, null, BrowserFileUtils.readResource( GlobalConstants.UNKNOWN_HOST_ERROR_HTML ) );
			
		} catch (IOException e) {
			LOGGER.error("Error while establishing connection", e);
			
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("Error while closing connection", e);
			}
		}
		return dto;
	}
	
	private static class HttpResponseHandler implements ResponseHandler<String> {
		private HttpDTO httpDTO = null;
		@Override
		public String handleResponse( final HttpResponse response ) throws ClientProtocolException, IOException {
			int statusCode = response.getStatusLine().getStatusCode();
			String status = response.getStatusLine().getReasonPhrase();
			LOGGER.debug("Response Status - " + statusCode + " " + status);
			HttpEntity entity = response.getEntity();
			String responseBody = entity != null ? EntityUtils.toString(entity) : null;
			//LOGGER.debug("Response Body - " + responseBody);
			httpDTO = new HttpDTO( statusCode, status, new StringBuilder(responseBody) );
			return responseBody;
		}
		public HttpDTO getHttpDTO() {
			return httpDTO;
		}
	};
	
	
}