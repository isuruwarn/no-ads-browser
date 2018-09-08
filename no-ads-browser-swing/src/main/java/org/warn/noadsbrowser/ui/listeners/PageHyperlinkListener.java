package org.warn.noadsbrowser.ui.listeners;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.warn.noadsbrowser.util.GlobalConstants;

public class PageHyperlinkListener implements HyperlinkListener {
	
	private JButton goBtn;
	private JTextField addressBar;
	private JLabel statusBar;
	
	public PageHyperlinkListener( JButton goBtn, JTextField addressBar, JLabel statusBar ) {
		this.goBtn = goBtn;
		this.addressBar = addressBar;
		this.statusBar = statusBar;
	}
	
	public void hyperlinkUpdate(HyperlinkEvent e) {
		
		String prevUrl = this.addressBar.getText();
		String hoverUrl = e.getDescription();
		
		if( e.getEventType() == HyperlinkEvent.EventType.ENTERED ) {
			this.statusBar.setText( getHyperLink( prevUrl, hoverUrl ) );
		
		} else if( e.getEventType() == HyperlinkEvent.EventType.EXITED ) {
			this.statusBar.setText("");
		
		} else if( e.getEventType() == HyperlinkEvent.EventType.ACTIVATED ) {
			String url = getHyperLink( prevUrl, hoverUrl );
			this.addressBar.setText(url);
			this.goBtn.doClick();
		}
	}
	
	private String getHyperLink( String prevUrl, String hoverUrl ) {
		if( hoverUrl.startsWith( GlobalConstants.HTTP_PROTOCOL ) || hoverUrl.startsWith( GlobalConstants.HTTPS_PROTOCOL ) 
				|| hoverUrl.startsWith( GlobalConstants.FILE_PROTOCOL ) ) {
			return hoverUrl;
			
		} else {
			String [] tokens = prevUrl.split("/");
			String domain = tokens[2];
			String url = tokens[0] + "//" + domain + hoverUrl;
			return url;
		}
	}
}
