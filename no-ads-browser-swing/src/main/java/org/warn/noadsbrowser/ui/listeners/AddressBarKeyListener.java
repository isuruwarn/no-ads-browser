package org.warn.noadsbrowser.ui.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import org.warn.noadsbrowser.cache.BrowserCache;

public class AddressBarKeyListener implements KeyListener {
	
	private JButton goBtn;
	private UrlsPopupMouseAdapter urlsPopupMouseAdapter;
	
	public AddressBarKeyListener( JButton goBtn, UrlsPopupMouseAdapter  urlsPopupMouseAdapter ) {
		this.goBtn = goBtn;
		this.urlsPopupMouseAdapter = urlsPopupMouseAdapter;
	}
	
	public void keyTyped(KeyEvent e) {
		String inputKey = String.valueOf( e.getKeyChar() );
		if( inputKey!=null && inputKey.equals("\n") ) {
			this.goBtn.doClick();
		} else {
			JTextField addressBar = (JTextField) e.getComponent();
			String text = addressBar.getText();
			if( text!=null && text.length()>2 ) {
				List<String> matchingUrls = BrowserCache.getVisitedUrls( text );
				if( matchingUrls!=null && matchingUrls.size()>0 ) {
					JPopupMenu urlsPopupMenu = new JPopupMenu();
					urlsPopupMenu.removeAll();
					for( String url: matchingUrls ) {
						JMenuItem menuItem = new JMenuItem(url);
						menuItem.addMouseListener(urlsPopupMouseAdapter);
						urlsPopupMenu.add( menuItem );
					}
					urlsPopupMenu.show( addressBar, 0, 20);
					addressBar.requestFocusInWindow();
				}
			}
		}
	}
	
	public void keyPressed( KeyEvent e ) {
		
	}
	
	public void keyReleased(KeyEvent e) {
		
	}
	
}
