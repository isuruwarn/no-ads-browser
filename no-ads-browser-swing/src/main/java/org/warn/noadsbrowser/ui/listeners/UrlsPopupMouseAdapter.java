package org.warn.noadsbrowser.ui.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

public class UrlsPopupMouseAdapter extends MouseAdapter  {

	private JTextField addressBar;
	private JButton goBtn;
	
	public UrlsPopupMouseAdapter( JTextField addressBar, JButton goBtn ) {
		this.addressBar = addressBar;
		this.goBtn = goBtn;
	}
	
	public void mousePressed(MouseEvent e) {
		JMenuItem item = (JMenuItem) e.getSource();
		String url = item.getText();
		this.addressBar.setText(url);
		this.goBtn.doClick();
	}

	public void mouseReleased(MouseEvent e) {
		
	}

}
