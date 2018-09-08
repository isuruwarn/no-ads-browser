package org.warn.noadsbrowser.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Env {
	
	public static final String FILE_SEPERATOR = System.getProperty("file.separator");
	public static final String USER_HOME_DIR = System.getProperty("user.home");
	public static final String READER_HOME_DIR_NAME = ".wpreader";
	
	public static String getReaderHomeDir() {
		return USER_HOME_DIR + FILE_SEPERATOR + READER_HOME_DIR_NAME + FILE_SEPERATOR;
	}
	
	/**
		* http://www.javapractices.com/topic/TopicAction.do?Id=82
		* Get the String residing on the clipboard.
		*
		* @return any text found on the Clipboard; if none found, return an
		* empty String.
		*/
	public static String getClipboardContents() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		//odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String)contents.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException | IOException ex){
				System.out.println(ex);
			}
		}
		return result;
	}

}
