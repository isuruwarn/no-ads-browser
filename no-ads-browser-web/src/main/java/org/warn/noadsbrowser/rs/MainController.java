package org.warn.noadsbrowser.rs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.noadsbrowser.html.HtmlProcessor;
import org.warn.noadsbrowser.html.Page;
import org.warn.noadsbrowser.http.ApacheHttpClient;
import org.warn.noadsbrowser.http.HttpDTO;
import org.warn.noadsbrowser.text.TextExtractor;
import org.warn.noadsbrowser.util.BrowserFileUtils;
import org.warn.noadsbrowser.util.GlobalConstants;
import org.warn.noadsbrowser.util.WebUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/svc/rs/main")
public class MainController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger( MainController.class );
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PageDTO dto = new PageDTO();
		String url = request.getQueryString();
		LOGGER.info( "Query string - " + url );
		
		url = url.replace( "nbturl=", "" );
		url = url.substring( 0, url.indexOf("&nbtxt=") );
		LOGGER.info( "URL string - " + url );
		
		if( url != null ) {
			
			url = url.trim();
			url = WebUtil.formatUrl(url);
			LOGGER.info( "Target URL - " + url );
			
			if( WebUtil.validUrl(url) ) {
				
				boolean plainText = request.getParameter("nbtxt")==null?false:Boolean.valueOf( request.getParameter("nbtxt") );
				boolean cssDisabled = request.getParameter("nbcss")==null?false:Boolean.valueOf( request.getParameter("nbcss") );
				boolean imagesDisabled = request.getParameter("nbimgs")==null?false:Boolean.valueOf( request.getParameter("nbimgs") );
				boolean removeMetaTags = request.getParameter("nbmeta")==null?false:Boolean.valueOf( request.getParameter("nbmeta") );
				boolean includeInputSrc = request.getParameter("nbinputsrc")==null?false:Boolean.valueOf( request.getParameter("nbinputsrc") );
				
				StringBuilder webContent = null;
				HttpDTO httpDTO = ApacheHttpClient.read(url);
				if( httpDTO.getStatusCode() == 404 && ( httpDTO.getResponseBody()==null || httpDTO.getResponseBody().length()==0 ) ) {
					webContent = BrowserFileUtils.readResource( GlobalConstants.NOT_FOUND_ERROR_HTML );
				} else {
					webContent = httpDTO.getResponseBody();
				}
				
				Page page = HtmlProcessor.process( webContent, cssDisabled, imagesDisabled, removeMetaTags, true );
				dto.setTitle( page.getTitle() );
				dto.setMessage( page.getMessage() );
				if( includeInputSrc ) {
					dto.setInput( page.getInputHTML() );
				}
				if( plainText ) {
					String text = TextExtractor.extract( page.getDocument() ).toString().replaceAll("\n", "<br>");
					dto.setOutput( new StringBuilder( text ) );
				} else {
					dto.setOutput( page.getOutputHTML() );
				}
				dto.setUrl(url);
				
				// this is required to properly render unicode characters
				response.setContentType("text/html; charset=UTF-8");
				
				ObjectMapper mapper = new ObjectMapper();
				response.getWriter().append( mapper.writeValueAsString(dto) );
			}
		}
		
		// TODO error handling
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
