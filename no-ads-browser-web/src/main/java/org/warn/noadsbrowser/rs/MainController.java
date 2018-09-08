package org.warn.noadsbrowser.rs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.warn.noadsbrowser.html.HtmlProcessor;
import org.warn.noadsbrowser.html.Page;
import org.warn.noadsbrowser.http.ApacheHttpClient;
import org.warn.noadsbrowser.http.HttpDTO;
import org.warn.noadsbrowser.util.BrowserFileUtils;
import org.warn.noadsbrowser.util.GlobalConstants;
import org.warn.noadsbrowser.util.WebUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/svc/rs")
public class MainController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PageDTO dto = new PageDTO();
		String url = request.getParameter("url");
		if( url != null ) {
			url = WebUtil.formatUrl(url);
			if( WebUtil.validUrl(url) ) {
				StringBuilder webContent = null;
				HttpDTO httpDTO = ApacheHttpClient.read(url);
				if( httpDTO.getStatusCode() == 404 && ( httpDTO.getResponseBody()==null || httpDTO.getResponseBody().length()==0 ) ) {
					webContent = BrowserFileUtils.readResource( GlobalConstants.NOT_FOUND_ERROR_HTML );
				} else {
					webContent = httpDTO.getResponseBody();
				}
				Page page = HtmlProcessor.process(webContent);
				dto.setTitle( page.getTitle() );
				dto.setMessage( page.getMessage() );
				dto.setOutput( page.getOutputHTML() );
				dto.setUrl(url);
				
				ObjectMapper mapper = new ObjectMapper();
				response.getWriter().append( mapper.writeValueAsString(dto) );
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
