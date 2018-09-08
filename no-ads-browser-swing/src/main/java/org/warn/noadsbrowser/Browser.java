package org.warn.noadsbrowser;

import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.warn.noadsbrowser.ui.MainWindow;

public class Browser {
	
	public static void main( String[] args ) throws IOException {
		
		//String url = "http://time.com/4862536/how-to-cook-vegetables";
		//String url = "https://www.nytimes.com/2018/08/08/nyregion/guarding-the-throats-of-broadway.html?smtyp=cur&smid=tw-nytmetro";
		//String url = "https://www.bbc.com/news/world-africa-45146603?ocid=socialflow_twitter";
		//String url = "https://docs.oracle.com/javase/tutorial/essential/io/index.html";
		//String url = "https%3A%2F%2Fwww.google.com%2Fsearch%3Fq%3Djava+objectmapper+jsonobject%26oq%3Djava+objectmapper+jsonobject";
		//String url = "https://www.google.com/search%3Fq%253Djava%2Bobjectmapper%2Bjsonobject%2526oq%253Djava%2Bobjectmapper%2Bjsonobject";
		//String url = "https%3A%2F%2Fexamples.javacodegeeks.com%2Fenterprise-java%2Fslf4j%2Fslf4j-format-string-example/";
		//String url = "https://www.google.com/";
		//String url = "https://en.wikipedia.org/wiki/Security_Assertion_Markup_Language";
		//String url = "https://docs.oracle.com/javase/tutorial/uiswing/components/editorpane.html";
		//String url = "https%3A%2F%2Fwww.google.com%2Fsearch%3Fq%3Djava+objectmapper+jsonobject%26oq%3Djava+objectmapper+jsonobject";
		//String url = "https%3A%2F%2Fexamples.javacodegeeks.com%2Fenterprise-java%2Fslf4j%2Fslf4j-format-string-example/";
		//String url = "https%3A%2F%2Flifehacker.com%2F5918086%2Funderstanding-oauth-what-happens-when-you-log-into-a-site-with-google-twitter-or-facebook";
		
		//String url = "file://src/test/res/lifehacker2.html";
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		//String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		//System.out.println( System.getProperty("file.separator") );
		
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				new MainWindow( null );
			}
		});
		
	}
	
}
