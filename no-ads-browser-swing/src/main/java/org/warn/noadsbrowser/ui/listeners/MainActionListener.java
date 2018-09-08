package org.warn.noadsbrowser.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.noadsbrowser.cache.BrowserCache;
import org.warn.noadsbrowser.config.BrowserConfig;
import org.warn.noadsbrowser.html.HtmlProcessor;
import org.warn.noadsbrowser.html.Page;
import org.warn.noadsbrowser.http.ApacheHttpClient;
import org.warn.noadsbrowser.http.HttpDTO;
import org.warn.noadsbrowser.text.TextExtractor;
import org.warn.noadsbrowser.ui.MainWindow;
import org.warn.noadsbrowser.ui.PopupWindow;
import org.warn.noadsbrowser.util.BrowserFileUtils;
import org.warn.noadsbrowser.util.Env;
import org.warn.noadsbrowser.util.GlobalConstants;
import org.warn.noadsbrowser.util.WebUtil;

public class MainActionListener implements ActionListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MainActionListener.class);
	
	private int currentPageIndex;
	private List<String> visitedPages = new ArrayList<String>(); // track page history in current session
	private JFrame mainWindow;
	private JTextField addressBar;
	private JEditorPane htmlTextArea;
	private JLabel statusBar;
	private Page page;
	
	public MainActionListener( JFrame mainWindow, JTextField addressBar, JEditorPane htmlTextArea, JLabel statusBar ) {
		this.mainWindow = mainWindow;
		this.addressBar = addressBar;
		this.htmlTextArea = htmlTextArea;
		this.statusBar = statusBar;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String url = null;
		String command = e.getActionCommand();
		
		switch(command) {
			
			case MainWindow.GO_BTN_ACTION:
				handleGoAction();
				break;
				
			case MainWindow.BACK_BTN_ACTION:
				if( visitedPages.size()>0 && currentPageIndex-1 >= 0 ) {
					currentPageIndex--;
					url = visitedPages.get(currentPageIndex);
					addressBar.setText(url);
					loadPage(url);
				}
				break;
				
			case MainWindow.FORWARD_BTN_ACTION:
				if( visitedPages.size()>0 && currentPageIndex+1 <= visitedPages.size()-1 ) {
					currentPageIndex++;
					url = visitedPages.get(currentPageIndex);
					addressBar.setText(url);
					loadPage(url);
				}
				break;
			
			case MainWindow.HTML_DISPLAY_MODE_ACTION:
				BrowserConfig.updateConfig( BrowserConfig.PROP_DISPLAY_MODE, BrowserConfig.PROP_VAL_DISPLAY_MODE_HTML );
				handleGoAction();
				break;
				
			case MainWindow.PLAIN_TEXT_DISPLAY_MODE_ACTION:
				BrowserConfig.updateConfig( BrowserConfig.PROP_DISPLAY_MODE, BrowserConfig.PROP_VAL_DISPLAY_MODE_TEXT );
				handleGoAction();
				break;
			
			case MainWindow.DISABLE_CSS_ACTION:
				JCheckBoxMenuItem disableCSS = (JCheckBoxMenuItem) e.getSource();
				BrowserConfig.updateConfig( BrowserConfig.PROP_DISABLE_CSS, String.valueOf( disableCSS.isSelected() ) );
				handleGoAction();
				break;
			
			case MainWindow.DISABLE_IMAGES_ACTION:
				JCheckBoxMenuItem disableImages = (JCheckBoxMenuItem) e.getSource();
				BrowserConfig.updateConfig( BrowserConfig.PROP_DISABLE_IMAGES, String.valueOf( disableImages.isSelected() ) );
				handleGoAction();
				break;
			
			case MainWindow.REMOVE_META_TAGS_ACTION:
				JCheckBoxMenuItem removeMetaTags = (JCheckBoxMenuItem) e.getSource();
				BrowserConfig.updateConfig( BrowserConfig.PROP_REMOVE_META_TAGS, String.valueOf( removeMetaTags.isSelected() ) );
				handleGoAction();
				break;
			
			case MainWindow.REMOVE_DATA_ATTRS_ACTION:
				JCheckBoxMenuItem removeDataAttrs = (JCheckBoxMenuItem) e.getSource();
				BrowserConfig.updateConfig( BrowserConfig.PROP_REMOVE_DATA_ATTRS, String.valueOf( removeDataAttrs.isSelected() ) );
				handleGoAction();
				break;
				
			case MainWindow.VIEW_INPUT_HTML_ACTION:
				if( this.page!=null && this.page.getInputHTML()!=null ) {
					new PopupWindow( MainWindow.VIEW_INPUT_HTML_TITLE + this.page.getTitle(), this.page.getInputHTML().toString() );
				}
				break;
				
			case MainWindow.VIEW_OUTPUT_HTML_ACTION:
				if( this.page!=null && this.page.getOutputHTML()!=null ) {
					new PopupWindow( MainWindow.VIEW_OUTPUT_HTML_TITLE + this.page.getTitle(), this.page.getOutputHTML().toString() );
				}
				break;
			
//			case MainWindow.RESET_BACKGROUND_COLOR_ACTION:
//				// TODO - debug and fix
//				this.htmlTextArea.setBackground( Color.WHITE );
//				this.htmlTextArea.repaint();
//				break;
				
			case MainWindow.CLEAR_CACHE_ACTION:
				int selectedOption = JOptionPane.showConfirmDialog( this.mainWindow, GlobalConstants.CACHE_CLEAR_CONFIRMATION_MSG, "", 
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
				if( selectedOption == JOptionPane.YES_OPTION ) {
					boolean cacheCleared = BrowserCache.clearCache();
    				if(cacheCleared) {
    					this.statusBar.setText( GlobalConstants.CACHE_CLEARED_MSG );
    				} else {
    					this.statusBar.setText( GlobalConstants.CACHE_NOT_CLEARED_MSG );
    				}
                }
				break;
				
			case MainWindow.PASTE_ACTION:
				url = Env.getClipboardContents();
				addressBar.setText(url);
				break;
			
			case MainWindow.PASTE_AND_GO_ACTION:
				addressBar.setText( Env.getClipboardContents() );
				handleGoAction();
				break;
		}
	}
	
	private void handleGoAction() {
		this.page = null;
		String url = this.addressBar.getText();
		this.statusBar.setText( GlobalConstants.CONNECTING_STATUS_MSG + url );
		url = WebUtil.formatUrl(url);
		if( WebUtil.validUrl(url) ) {
			BrowserCache.addVisitedUrl(url);
			loadPage(url);
			currentPageIndex = visitedPages.size()-1;
		} else {
			this.statusBar.setText( GlobalConstants.INVALID_URL_MSG );
		}
	}
	
	private void loadPage( String url ) {
		try {
			StringBuilder webContent = getContent(url);
			this.page = HtmlProcessor.process(webContent);
			if( this.page != null ) {
				this.addressBar.setText(url);
				this.mainWindow.setTitle( this.page.getTitle()!=null?this.page.getTitle():MainWindow.TITLE + " - " + url );
				String displayMode = BrowserConfig.getProperty( BrowserConfig.PROP_DISPLAY_MODE );
				if( displayMode.equals( BrowserConfig.PROP_VAL_DISPLAY_MODE_TEXT) ) {
					this.page.setOutputText( TextExtractor.extract( this.page.getDocument() ) );
					this.htmlTextArea.setContentType("text/plain");
					this.htmlTextArea.setText( page.getOutputText()==null?"":page.getOutputText().toString() );
				} else {
					this.htmlTextArea.setContentType("text/html");
					this.htmlTextArea.setText( page.getOutputHTML()==null?"":page.getOutputHTML().toString() );
				}
				this.htmlTextArea.setCaretPosition(0);
				this.statusBar.setText( GlobalConstants.COMPLETED_STATUS_MSG );
				if( !this.visitedPages.contains(url) ) {
					this.visitedPages.add(url);
				}
			} else {
				loadGenericErrorPage( null );
			}
		} catch( RuntimeException e ) {
			LOGGER.debug("Error while loading page", e );
			this.page = null;
			loadGenericErrorPage( e.getMessage() );
		} catch (IOException e) {
			LOGGER.debug("Error while loading page", e );
			this.page = null;
			loadGenericErrorPage( e.getMessage() );
		}
	}
	
	private StringBuilder getContent( String url ) throws IOException {
		StringBuilder webContent = null;
		if( url.startsWith( GlobalConstants.FILE_PROTOCOL ) ) {
			LOGGER.debug("Loading local file - " + url );
			String filePath = url.replace( GlobalConstants.FILE_PROTOCOL, "" );
			webContent = BrowserFileUtils.read(filePath);
			if( webContent == null ) {
				webContent = BrowserFileUtils.readResource( GlobalConstants.NOT_FOUND_ERROR_HTML );
			}
		} else {
			webContent = BrowserCache.loadFromCache(url);
			if( webContent == null ) {
				LOGGER.debug("Page not found in cache. Loading from network - " + url );
				HttpDTO httpDTO = ApacheHttpClient.read(url);
				if( httpDTO.getStatusCode() == 404 && ( httpDTO.getResponseBody()==null || httpDTO.getResponseBody().length()==0 ) ) {
					webContent = BrowserFileUtils.readResource( GlobalConstants.NOT_FOUND_ERROR_HTML );
				} else {
					webContent = httpDTO.getResponseBody();
					BrowserCache.writeToCache( url, webContent.toString() );
				}
			}
		}
		return webContent;
	}
	
	private void loadGenericErrorPage( String message ) {
		StringBuilder webContent = BrowserFileUtils.readResource( GlobalConstants.RENDERING_ERROR_HTML );
		this.statusBar.setText( GlobalConstants.ERROR_LOADING_PAGE_MSG + (message==null?"":" - " + message) );
		this.htmlTextArea.setText( webContent==null?"":webContent.toString() );
		this.htmlTextArea.setCaretPosition(0);
	}
	
}
