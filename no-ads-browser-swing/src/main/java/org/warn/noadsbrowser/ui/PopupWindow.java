package org.warn.noadsbrowser.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class PopupWindow {
	
	private static final int POPUP_FRAME_WIDTH = 800;
	private static final int POPUP_FRAME_HEIGHT = 750;
	
	public PopupWindow( String title, String text ) {
		
		JEditorPane popupTextArea = new JEditorPane();
		popupTextArea.setAlignmentX(JTextField.LEFT);
		popupTextArea.setAlignmentY(JTextField.TOP_ALIGNMENT);
		popupTextArea.setEditable(false);
		popupTextArea.setForeground (Color.DARK_GRAY); // set text color
		popupTextArea.setBackground( Color.WHITE );
		popupTextArea.setText(text);
		popupTextArea.setEnabled(true);
		
		JScrollPane textScrollPane = new JScrollPane(popupTextArea);
		
		GridBagConstraints textAreaGridCons = new GridBagConstraints();
		textAreaGridCons.gridx = 0;
		textAreaGridCons.gridy = 0;
		textAreaGridCons.anchor = GridBagConstraints.LINE_START;
		textAreaGridCons.insets = new Insets(0,0,0,0);
		
		JPanel mainPanel = new JPanel();
		mainPanel.add( textScrollPane, textAreaGridCons );
		mainPanel.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width = mainPanel.getWidth();
				int height = mainPanel.getHeight();
				textScrollPane.setPreferredSize( new Dimension( width, height ) );
				textScrollPane.setSize( new Dimension( width, height ) );
				textScrollPane.setLocation(0, 0);
				popupTextArea.setPreferredSize( new Dimension( width, height ) );
				popupTextArea.setSize( new Dimension( width, height ) );
			}
		});
		
		JFrame webpageFrame = new JFrame( title );
		webpageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		webpageFrame.setPreferredSize( new Dimension( POPUP_FRAME_WIDTH, POPUP_FRAME_HEIGHT ) );
		webpageFrame.setMinimumSize( new Dimension( POPUP_FRAME_WIDTH, POPUP_FRAME_HEIGHT ) );
		webpageFrame.add( mainPanel );
		webpageFrame.setLocationRelativeTo(null); // position to center of screen
		webpageFrame.pack();
		webpageFrame.setVisible(true);
		
	}
	
}
