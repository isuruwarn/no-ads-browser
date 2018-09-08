package org.warn.noadsbrowser.ui.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTextField;

public class AddressBarMouseAdapter extends MouseAdapter {
	
	private JPopupMenu addressBarPopupMenu;
	
	public AddressBarMouseAdapter( JPopupMenu popup ) {
		this.addressBarPopupMenu = popup;
	}
	
	public void mousePressed(MouseEvent e) {
		showPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}

	private void showPopup(MouseEvent e) {
		Object obj = e.getSource();
		if( obj instanceof JTextField ) {
			if( e.isPopupTrigger() ) {
				addressBarPopupMenu.show( e.getComponent(), e.getX(), e.getY() );
			}
		}
	}
}
