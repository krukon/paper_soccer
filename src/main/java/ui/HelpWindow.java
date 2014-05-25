package ui;

/**
 * @author wTendera
 */


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import controller.PaperSoccer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
