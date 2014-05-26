package ui;

/**
 * @author wTendera
 */


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpWindow extends Stage {
	public HelpWindow() {
		initModality(Modality.WINDOW_MODAL);
		setTitle("Help");
		try {
			WebView webView  = new WebView();
			WebEngine webEngine = webView.getEngine();
			InputStream in = getClass().getResourceAsStream("/help/help.html");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			webEngine.loadContent(sb.toString());
	        setScene(new Scene(webView, 300, 600));
		} catch(Exception e) {
			e.printStackTrace();
			close();
		}
	}
}
