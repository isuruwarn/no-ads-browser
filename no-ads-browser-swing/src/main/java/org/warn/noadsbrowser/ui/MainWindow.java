package org.warn.noadsbrowser.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.warn.noadsbrowser.config.BrowserConfig;
import org.warn.noadsbrowser.ui.listeners.AddressBarKeyListener;
import org.warn.noadsbrowser.ui.listeners.AddressBarMouseAdapter;
import org.warn.noadsbrowser.ui.listeners.MainActionListener;
import org.warn.noadsbrowser.ui.listeners.PageHyperlinkListener;
import org.warn.noadsbrowser.ui.listeners.UrlsPopupMouseAdapter;

public class MainWindow {
	
	public static final String TITLE = "Webpage Reader";
	public static final String VIEW_INPUT_HTML_TITLE = "Input HTML - ";
	public static final String VIEW_OUTPUT_HTML_TITLE = "Output HTML - ";
	public static final String BACK_BTN_ACTION = "\u21e6";
	public static final String FORWARD_BTN_ACTION = "\u21e8";
	public static final String GO_BTN_ACTION = "Go";
	public static final String SETTINGS_BTN_ACTION = "\u2630";
	public static final String PASTE_ACTION = "Paste";
	public static final String PASTE_AND_GO_ACTION = "Paste and go";
	public static final String HTML_DISPLAY_MODE_ACTION = "Html";
	public static final String PLAIN_TEXT_DISPLAY_MODE_ACTION = "Plain Text";
	public static final String VIEW_INPUT_HTML_ACTION = "View input HTML";
	public static final String VIEW_OUTPUT_HTML_ACTION = "View output HTML";
	public static final String DISABLE_CSS_ACTION = "Disable CSS";
	public static final String DISABLE_IMAGES_ACTION = "Disable Images";
	public static final String REMOVE_META_TAGS_ACTION = "Remove meta tags";
	public static final String REMOVE_DATA_ATTRS_ACTION = "Remove data attributes";
	//public static final String RESET_BACKGROUND_COLOR_ACTION = "Reset background color";
	public static final String CLEAR_CACHE_ACTION = "Clear cache";
	
	private static final int PREF_FRAME_WIDTH = 1000;
	private static final int PREF_FRAME_HEIGHT = 1000;
	private static final int MIN_FRAME_WIDTH = 1000;
	private static final int MIN_FRAME_HEIGHT = 1000;
	//private static final int PREF_TEXTAREA_WIDTH = 980;
	//private static final int PREF_TEXTAREA_HEIGHT = 900;
	//private static final int MIN_TEXTAREA_WIDTH = 980;
	//private static final int MIN_TEXTAREA_HEIGHT = 900;
	//private static final int PREF_ADDRESS_BAR_WIDTH = 775;
	private static final int PREF_ADDRESS_BAR_HEIGHT = 25;
	//private static final int MIN_ADDRESS_BAR_WIDTH = 775;
	//private static final int MIN_ADDRESS_BAR_HEIGHT = 25;
	private static final int PREF_GO_BTN_WIDTH = 50;
	private static final int PREF_GO_BTN_HEIGHT = 25;
	private static final int MIN_GO_BTN_WIDTH = 50;
	private static final int MIN_GO_BTN_HEIGHT = 25;
	private static final int PREF_SETTINGS_BTN_WIDTH = 25;
	private static final int PREF_SETTINGS_BTN_HEIGHT = 25;
	private static final int MIN_SETTINGS_BTN_WIDTH = 25;
	private static final int MIN_SETTINGS_BTN_HEIGHT = 25;
	private static final int PREF_STATUS_BAR_WIDTH = 975;
	private static final int PREF_STATUS_BAR_HEIGHT = 20;
	private static final int MIN_STATUS_BAR_WIDTH = 975;
	private static final int MIN_STATUS_BAR_HEIGHT = 20;
	
	private static final int ADDR_BAR_BTN_FONT_SIZE = 12;
	private static final String ADDR_BAR_BTN_FONT = "Arial";
	
	public MainWindow( String url ) {
		
		JButton backBtn = new JButton(BACK_BTN_ACTION);
		backBtn.setPreferredSize( new Dimension( PREF_GO_BTN_WIDTH, PREF_GO_BTN_HEIGHT ) );
		backBtn.setMinimumSize( new Dimension( MIN_GO_BTN_WIDTH, MIN_GO_BTN_HEIGHT ) );
		
		JButton forwardBtn = new JButton(FORWARD_BTN_ACTION);
		forwardBtn.setPreferredSize( new Dimension( PREF_GO_BTN_WIDTH, PREF_GO_BTN_HEIGHT ) );
		forwardBtn.setMinimumSize( new Dimension( MIN_GO_BTN_WIDTH, MIN_GO_BTN_HEIGHT ) );
		
		JTextField addressBar = new JTextField();
		//addressBar.setPreferredSize( new Dimension( PREF_ADDRESS_BAR_WIDTH, PREF_ADDRESS_BAR_HEIGHT ) );
		//addressBar.setMinimumSize( new Dimension( MIN_ADDRESS_BAR_WIDTH, MIN_ADDRESS_BAR_HEIGHT ) );
		if(url!=null) {
			addressBar.setText(url);
		}
		
		JButton goBtn = new JButton(GO_BTN_ACTION);
		goBtn.setPreferredSize( new Dimension( PREF_GO_BTN_WIDTH, PREF_GO_BTN_HEIGHT ) );
		goBtn.setMinimumSize( new Dimension( MIN_GO_BTN_WIDTH, MIN_GO_BTN_HEIGHT ) );
		goBtn.setFont( new Font(ADDR_BAR_BTN_FONT, Font.BOLD, ADDR_BAR_BTN_FONT_SIZE) );
		
		JEditorPane htmlTextArea = new JEditorPane();
		htmlTextArea.setEditable(false);
		////htmlTextArea.setSize( new Dimension( MIN_TEXTAREA_WIDTH, MIN_TEXTAREA_HEIGHT ) );
		//htmlTextArea.setMinimumSize( new Dimension( MIN_TEXTAREA_WIDTH, MIN_TEXTAREA_HEIGHT ) );
		
		JScrollPane textScrollPane = new JScrollPane(htmlTextArea);
		textScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		textScrollPane.setViewportView(htmlTextArea);
		//textScrollPane.setBorder( BorderFactory.createLineBorder(Color.red) );
		//textScrollPane.setPreferredSize( new Dimension( PREF_TEXTAREA_WIDTH, PREF_TEXTAREA_HEIGHT ) );
		//textScrollPane.setMinimumSize( new Dimension( MIN_TEXTAREA_WIDTH, MIN_TEXTAREA_HEIGHT ) );
		
		JLabel statusBar = new JLabel();
		statusBar.setPreferredSize( new Dimension( PREF_STATUS_BAR_WIDTH, PREF_STATUS_BAR_HEIGHT ) );
		statusBar.setMinimumSize( new Dimension( MIN_STATUS_BAR_WIDTH, MIN_STATUS_BAR_HEIGHT ) );
		//statusBar.setBorder( BorderFactory.createLineBorder(Color.red) );
		
		JMenuItem paste = new JMenuItem( PASTE_ACTION );
		JMenuItem pasteAndGo = new JMenuItem( PASTE_AND_GO_ACTION );
		JPopupMenu addressBarPopupMenu = new JPopupMenu();
		addressBarPopupMenu.add(paste);
		addressBarPopupMenu.add(pasteAndGo);
		
		JMenuItem htmlFormat = new JRadioButtonMenuItem( HTML_DISPLAY_MODE_ACTION );
		JMenuItem plainTextFormat = new JRadioButtonMenuItem( PLAIN_TEXT_DISPLAY_MODE_ACTION );
		JMenuItem viewInputHtml = new JMenuItem( VIEW_INPUT_HTML_ACTION );
		JMenuItem viewOutputHtml = new JMenuItem( VIEW_OUTPUT_HTML_ACTION );
		JCheckBoxMenuItem disableCSS = new JCheckBoxMenuItem( DISABLE_CSS_ACTION );
		JCheckBoxMenuItem disableImages = new JCheckBoxMenuItem( DISABLE_IMAGES_ACTION );
		JCheckBoxMenuItem removeMetaTags = new JCheckBoxMenuItem( REMOVE_META_TAGS_ACTION );
		JCheckBoxMenuItem removeDataAttrs = new JCheckBoxMenuItem( REMOVE_DATA_ATTRS_ACTION );
		//JMenuItem rsetBgColor = new JMenuItem( RESET_BACKGROUND_COLOR_ACTION );
		JMenuItem clearCache = new JMenuItem( CLEAR_CACHE_ACTION );
		
		ButtonGroup displayFormatGroup = new ButtonGroup();
		displayFormatGroup.add(htmlFormat);
		displayFormatGroup.add(plainTextFormat);
		
		JMenu settingsMenu = new JMenu(SETTINGS_BTN_ACTION);
		settingsMenu.setPreferredSize( new Dimension( PREF_SETTINGS_BTN_WIDTH, PREF_SETTINGS_BTN_HEIGHT ) );
		settingsMenu.setMinimumSize( new Dimension( MIN_SETTINGS_BTN_WIDTH, MIN_SETTINGS_BTN_HEIGHT ) );
		settingsMenu.add(htmlFormat);
		settingsMenu.add(plainTextFormat);
		settingsMenu.add(htmlFormat);
		settingsMenu.add(plainTextFormat);
		settingsMenu.addSeparator();
		settingsMenu.add(viewInputHtml);
		settingsMenu.add(viewOutputHtml);
		settingsMenu.addSeparator();
		settingsMenu.add(disableCSS);
		settingsMenu.add(disableImages);
		settingsMenu.add(removeMetaTags);
		settingsMenu.add(removeDataAttrs);
		settingsMenu.addSeparator();
		//settingsMenu.add(rsetBgColor);
		settingsMenu.add(clearCache);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(settingsMenu);
		
		// get config settings
		String displayMode = BrowserConfig.getProperty( BrowserConfig.PROP_DISPLAY_MODE );
		if( displayMode.equals( BrowserConfig.PROP_VAL_DISPLAY_MODE_TEXT) ) {
			plainTextFormat.setSelected(true);
		} else { 
			// default display mode is html
			htmlFormat.setSelected(true);
			htmlTextArea.setContentType("text/html");
		}
		disableCSS.setSelected( Boolean.valueOf( BrowserConfig.getProperty( BrowserConfig.PROP_DISABLE_CSS ) ) );
		disableImages.setSelected( Boolean.valueOf( BrowserConfig.getProperty( BrowserConfig.PROP_DISABLE_IMAGES ) ) );
		removeMetaTags.setSelected( Boolean.valueOf( BrowserConfig.getProperty( BrowserConfig.PROP_REMOVE_META_TAGS ) ) );
		removeDataAttrs.setSelected( Boolean.valueOf( BrowserConfig.getProperty( BrowserConfig.PROP_REMOVE_DATA_ATTRS ) ) );
		
		// layout manager configurations
		GridBagConstraints backBtnGridCons = new GridBagConstraints();
		backBtnGridCons.gridx = 0;
		backBtnGridCons.gridy = 0;
		backBtnGridCons.anchor = GridBagConstraints.LINE_START;
		backBtnGridCons.insets = new Insets(0,5,0,0);
		
		GridBagConstraints forwardBtnGridCons = new GridBagConstraints();
		forwardBtnGridCons.gridx = 1;
		forwardBtnGridCons.gridy = 0;
		forwardBtnGridCons.insets = new Insets(0,0,0,5);
		
		GridBagConstraints addressBarGridCons = new GridBagConstraints();
		addressBarGridCons.gridx = 2;
		addressBarGridCons.gridy = 0;
		addressBarGridCons.insets = new Insets(0,0,0,5);
		
		GridBagConstraints goBtnGridCons = new GridBagConstraints();
		goBtnGridCons.gridx = 3;
		goBtnGridCons.gridy = 0;
		goBtnGridCons.insets = new Insets(0,0,0,0);

		GridBagConstraints settingsBtnGridCons = new GridBagConstraints();
		settingsBtnGridCons.gridx = 4;
		settingsBtnGridCons.gridy = 0;
		settingsBtnGridCons.insets = new Insets(0,0,0,0);
		
		GridBagConstraints htmlAreaGridCons = new GridBagConstraints();
		htmlAreaGridCons.gridwidth = 5;
		htmlAreaGridCons.gridx = 0;
		htmlAreaGridCons.gridy = 1;
		htmlAreaGridCons.anchor = GridBagConstraints.CENTER;
		htmlAreaGridCons.insets = new Insets(10,0,0,0);
		
		GridBagConstraints statusBarGridCons = new GridBagConstraints();
		statusBarGridCons.gridwidth = 5;
		statusBarGridCons.gridx = 0;
		statusBarGridCons.gridy = 2;
		statusBarGridCons.anchor = GridBagConstraints.CENTER;
		statusBarGridCons.insets = new Insets(0,5,0,0);
		
		JPanel pagePanel = new JPanel();
		pagePanel.setLayout( new GridBagLayout() );
		pagePanel.add( backBtn, backBtnGridCons );
		pagePanel.add( forwardBtn, forwardBtnGridCons );
		pagePanel.add( addressBar, addressBarGridCons );
		pagePanel.add( goBtn, goBtnGridCons );
		pagePanel.add( menuBar, settingsBtnGridCons );
		pagePanel.add( textScrollPane, htmlAreaGridCons ); 
		pagePanel.add( statusBar, statusBarGridCons ); 
		pagePanel.setOpaque(true);
		JScrollPane mainScrollPane = new JScrollPane(pagePanel);
		mainScrollPane.setPreferredSize( new Dimension( PREF_FRAME_WIDTH, PREF_FRAME_HEIGHT ) );
		mainScrollPane.setMinimumSize( new Dimension( MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT ) );
		mainScrollPane.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int w = mainScrollPane.getWidth();
				int h = mainScrollPane.getHeight();
				addressBar.setPreferredSize( new Dimension( w-215, PREF_ADDRESS_BAR_HEIGHT ) );
				addressBar.setSize( new Dimension( w-215, PREF_ADDRESS_BAR_HEIGHT ) );
				htmlTextArea.setPreferredSize( new Dimension( w-20, h-70 ) );
				htmlTextArea.setSize( new Dimension( w-20, h-70 ) );
				statusBar.setPreferredSize( new Dimension( w-10, PREF_STATUS_BAR_HEIGHT ) );
				statusBar.setSize( new Dimension( w-10, PREF_STATUS_BAR_HEIGHT ) );
				textScrollPane.setPreferredSize( new Dimension( w-20, h-70 ) );
				textScrollPane.setSize( new Dimension( w-20, h-70 ) );
			}
		});
		
		// frame for holding everything
		JFrame webpageFrame = new JFrame( TITLE );
		webpageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//webpageFrame.setPreferredSize( new Dimension( PREF_FRAME_WIDTH, PREF_FRAME_HEIGHT ) );
		//webpageFrame.setMinimumSize( new Dimension( MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT ) );
		webpageFrame.add( mainScrollPane );
		//webpageFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen
		webpageFrame.pack();
		addressBar.requestFocusInWindow(); // has to be called after pack() and before setVisible()
		webpageFrame.setVisible(true);
		
		MainActionListener mainActionListener = new MainActionListener( webpageFrame, addressBar, htmlTextArea, statusBar );
		goBtn.addActionListener(mainActionListener);
		backBtn.addActionListener(mainActionListener);
		forwardBtn.addActionListener(mainActionListener);
		paste.addActionListener(mainActionListener);
		pasteAndGo.addActionListener(mainActionListener);
		htmlFormat.addActionListener(mainActionListener);
		plainTextFormat.addActionListener(mainActionListener);
		viewInputHtml.addActionListener(mainActionListener);
		viewOutputHtml.addActionListener(mainActionListener);
		disableCSS.addActionListener(mainActionListener);
		disableImages.addActionListener(mainActionListener);
		removeMetaTags.addActionListener(mainActionListener);
		removeDataAttrs.addActionListener(mainActionListener);
		//rsetBgColor.addActionListener(mainActionListener);
		clearCache.addActionListener(mainActionListener);
		
		UrlsPopupMouseAdapter  urlsPopupMouseAdapter = new UrlsPopupMouseAdapter( addressBar, goBtn );
		AddressBarKeyListener addressBarKeyListener = new AddressBarKeyListener( goBtn, urlsPopupMouseAdapter );
		addressBar.addKeyListener(addressBarKeyListener);
		
		MouseListener popupListener = new AddressBarMouseAdapter( addressBarPopupMenu );
		addressBar.addMouseListener(popupListener);
	    
		PageHyperlinkListener readerHyperlinkListener = new PageHyperlinkListener( goBtn, addressBar, statusBar );
		htmlTextArea.addHyperlinkListener(readerHyperlinkListener);
		
	}
	
}
