package ui;

import java.util.concurrent.atomic.AtomicBoolean;

import controller.PaperSoccer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SettingsWindow extends BorderPane {

	private Text windowTitle = new Text("Settings");
	private Insets insets = new Insets(25, 25, 25, 25);
	
	private TextField serverAddress;
	private TextField portName;
	private final AtomicBoolean correctAddress = new AtomicBoolean(true);
	private final AtomicBoolean correctPort = new AtomicBoolean(true);
	private Button okButton;
	
	public SettingsWindow() {
		this.serverAddress = new TextField("SERVER");
		this.portName = new TextField("PORT");
		
		windowTitle.setFill(Color.WHITE);
		
		setPadding(insets);
		setTop(addWindowTitle());
		setCenter(addGridPane());
		setBottom(addOkBackButton());
	}
	
	/**
	 * Constructs window title.
	 * @return HBox with a title
	 */
	private HBox addWindowTitle() {
		HBox textBox = new HBox();
		textBox.setAlignment(Pos.CENTER);
		textBox.getChildren().add(windowTitle);
		
		return textBox;
	}
	
	/**
	 * Constructs a start button.
	 * @return constructed button
	 */
	private Button getOkButton() {
		okButton = new Button("OK");
		okButton.setMinSize(100, 50);
		okButton.setDefaultButton(true);
		
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			
			//TODO Showing board and size validation
			@Override
			public void handle(ActionEvent event) {				
				try {
					String server = serverAddress.getCharacters().toString();
					String port = portName.getCharacters().toString();
					
					System.out.println("Passing:");
					System.out.println("server: " + server);
					System.out.println("port: " + port);
					
					//TODO apply changes
					PaperSoccer.getMainWindow().showNetworkWindow();
				} catch (Exception e) {
					System.out.println("Wrong server addres or port.");
				}
			
			}
		});
		
		return okButton;
	}
	
	/**
	 * Constructs an exit button.
	 * @return constructed button
	 */
	private Button getBackButton() {
		Button exitButton = new Button("BACK");
		exitButton.setMinSize(100, 50);
		exitButton.setCancelButton(true);
		
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {				
				System.out.println("Back to network window");
				PaperSoccer.getMainWindow().showNetworkWindow();
			
			}
		});
		
		return exitButton;
	}
	
	/**
	 * Adds start and exit buttons to the window.
	 * @return HBox with a buttons
	 */
	private HBox addOkBackButton() {
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(getOkButton(), getBackButton());
		
		return buttonBox;
	}
	
	
	private void addLabels(GridPane grid) {
		Label server = new Label("Server:");
		Label port = new Label("Port:");
		
		server.setTextFill(Color.WHITE);
		port.setTextFill(Color.WHITE);
		
		grid.add(server, 0, 0);
		grid.add(port, 0, 1);
	}

	
	
	private void addTextFields(GridPane grid) {
		grid.add(serverAddress, 1, 0);
		
		serverAddress.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
				if(newValue.matches("^(?=\\s*\\S).*$")){
					correctAddress.set(true);
					
					if(correctPort.get())
						okButton.setDisable(false);
				} else {
					correctAddress.set(false);
					okButton.setDisable(true);
				}
			}
		});

		grid.add(portName, 1, 1);
		
		portName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
				if(newValue.matches("^(?=\\s*\\S).*$")){
					correctPort.set(true);
					
					if(correctAddress.get())
						okButton.setDisable(false);
				} else {
					correctPort.set(false);
					okButton.setDisable(true);
				}
			}
		});

	}

	
	/**
	 * Constructs base grid and its components for window.
	 * @return constructed grid with components
	 */
	private GridPane addGridPane() {
		GridPane grid = new GridPane();
		grid.setMinSize(300, 400);
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(insets);
		
	
		addLabels(grid);
		addTextFields(grid);

		return grid;
	}

}
