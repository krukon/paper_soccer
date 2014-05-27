package ui;

/**
 * Class representing a network games list window.
 * 
 * @author ljk
 */

import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import network.RemoteGameController;
import network.ServerInquiry;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import controller.PaperSoccer;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GamesListWindow extends BorderPane{

	private Text windowTitle = new Text("Network game is waiting for you!");
	private Insets insets = new Insets(25, 25, 25, 25);
	private String guestName;

	public GamesListWindow(String guestName)
	{	
		setPrefSize(PaperSoccer.WIDTH, PaperSoccer.HEIGHT);
		setStyle("-fx-background-image: url('paper_soccer_background.jpg');");
		
		this.guestName = guestName;
		windowTitle.setFill(Color.WHITE);
		
		setPadding(insets);
		setTop(addWindowTitle());
		setCenter(addGridPane());
		setBottom(addBackButton());
		loadGamesList();
	}
	
	private void loadGamesList() {
		Thread listLoader = new Thread(new Runnable() {
			
			@Override
			public void run() {
				ServerInquiry server = PaperSoccer.server;
				try {
					BufferedReader reader = server.subscribeToGame();
					server.send("{\"type\":\"list_games\"}");
					String raw = reader.readLine();
					System.out.println("raw " + raw);
					JSONObject request = (JSONObject) JSONValue.parse(raw);
					final JSONArray data = (JSONArray) request.get("data");
					Platform.runLater(new Runnable() {
						
						@Override
						public void run() {
							renderGamesList(data);
						}

					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		listLoader.setDaemon(true);
		listLoader.start();
	}
	
	private void renderGamesList(JSONArray data) {
		System.out.println("Rendering list " + data.toString());
		recordList.clear();
		for (Object obj : data) {
			JSONObject game = (JSONObject) obj;
			recordList.add(new Record(
					Integer.parseInt(game.get("id").toString()),
					game.get("host_name").toString(),
					Integer.parseInt(game.get("width").toString()),
					Integer.parseInt(game.get("height").toString()),
					game.get("active").toString()));
		}
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
	
	
	public static class Record {
		 
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty boardWidth;
        private final SimpleIntegerProperty boardHeight;
        private final SimpleStringProperty active;
 
        public Record(int id, String name, int boardWidth, int boardHeight, String active) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.boardWidth = new SimpleIntegerProperty(boardWidth);
            this.boardHeight = new SimpleIntegerProperty(boardHeight);
            if(active == "false")
        		this.active = new SimpleStringProperty("Waiting");
        	else
        		this.active = new SimpleStringProperty("In Game");
        }
 
        public int getId() {
            return this.id.get();
        }
 
        public void setId(int id) {
            this.id.set(id);
        }
 
        public String getName() {
            return this.name.get();
        }

        public void setActive(String status) {
            this.active.set(status);
        }
        
        public String getActive() {
        	return this.active.get();
        }
 
        public void setName(String name) {
            this.name.set(name);
        }
 
        public int getBoardWidth() {
            return this.boardWidth.get();
        }
 
        public void setBoardWidth(int boardWidth) {
            this.boardWidth.set(boardWidth);
        }
 
        public int getBoardHeight() {
            return this.boardHeight.get();
        }
 
        public void setBoardHeight(int boardHeight) {
            this.boardHeight.set(boardHeight);
        }
    }
    
	private TableView<Record> tableView = new TableView<>();
    private final ObservableList<Record> recordList = FXCollections.observableArrayList();

    public void addList(GridPane grid) {
        tableView.setEditable(false);
 
        Callback<TableColumn, TableCell> integerCellFactory =
                new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                MyIntegerTableCell cell = new MyIntegerTableCell();
                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                return cell;
            }
        };
 
        Callback<TableColumn, TableCell> stringCellFactory =
                new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                MyStringTableCell cell = new MyStringTableCell();
                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                return cell;
            }
        };
        
        Callback<TableColumn, TableCell> activeCellFactory =
                new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                MyStringTableCell cell = new MyStringTableCell();
                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                return cell;
            }
        };
 
        TableColumn colId = new TableColumn("ID");
        colId.setCellValueFactory(
                new PropertyValueFactory<Record, String>("id"));
        colId.setCellFactory(integerCellFactory);
        colId.setPrefWidth(20);
        
        
        TableColumn colName = new TableColumn("Name");
        colName.setCellValueFactory(
                new PropertyValueFactory<Record, String>("name"));
        colName.setCellFactory(stringCellFactory);
        colName.setPrefWidth(60);
        
        
        TableColumn colBoardWidth = new TableColumn("Width");
        colBoardWidth.setCellValueFactory(
                new PropertyValueFactory<Record, String>("BoardWidth"));
        colBoardWidth.setCellFactory(integerCellFactory);
        colBoardWidth.setPrefWidth(60);
        
        TableColumn colBoardHeight = new TableColumn("Height");
        colBoardHeight.setCellValueFactory(
                new PropertyValueFactory<Record, String>("BoardHeight"));
        colBoardHeight.setCellFactory(integerCellFactory);
        colBoardHeight.setPrefWidth(60);
        
        TableColumn colActive = new TableColumn("Acitve");
        colActive.setCellValueFactory(new PropertyValueFactory<Record, String>("Active"));
        colActive.setCellFactory(activeCellFactory);
        colActive.setPrefWidth(50);
        
        tableView.setItems(recordList);
        tableView.getColumns().addAll(colId, colName, colBoardWidth, colBoardHeight, colActive);
         
        grid.add(tableView, 0, 0);
    }
 
    
    class MyIntegerTableCell extends TableCell<Record, Integer> {
 
        @Override
        public void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : getString());
            setGraphic(null);
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
 
    class MyStringTableCell extends TableCell<Record, String> {
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : getString());
            setGraphic(null);
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
 
    class MyEventHandler implements EventHandler<MouseEvent> {
 
        @Override
        public void handle(MouseEvent t) {
        	try {
            TableCell c = (TableCell) t.getSource();
            int index = c.getIndex();
            System.out.println("id = " + recordList.get(index).getId());
            System.out.println("name = " + recordList.get(index).getName());
            System.out.println("board width = " + recordList.get(index).getBoardWidth());
            System.out.println("board height = " + recordList.get(index).getBoardHeight());
            int id = recordList.get(index).getId();
            runControllerThread(id);
            
        	} catch (Exception e) {}
        }
    }
    
    

    
	/**
	 * Constructs base grid and its components for window.
	 * @return constructed grid with components
	 */
	private GridPane addGridPane() {
		GridPane grid = new GridPane();
		grid.setPrefSize(300, 400);
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(insets);
		//grid.setStyle("-fx-background-image: url('paper_soccer_background.jpg'); -fx-background-repeat: stretch;");
		
		addList(grid);
				
		return grid;
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
				System.out.println("Back to Network Menu");
				PaperSoccer.getMainWindow().showNetworkWindow();
			
			}
		});
		
		return exitButton;
	}
	
	/**
	 * Adds start and exit buttons to the window.
	 * @return HBox with a buttons
	 */
	private HBox addBackButton() {
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(getBackButton());
		
		return buttonBox;
	}
	
	private void runControllerThread(final int id) {
		Thread controllerThread = new Thread(new Runnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				ServerInquiry server = PaperSoccer.server;
				
				try {
					System.out.println("Joining");
					BufferedReader joinReader = server.subscribeToJoinGame();
					final BufferedReader gamePipe = server.subscribeToGame();
					
					JSONObject message = new JSONObject();
					message.put("type", "join_game");
					JSONObject data = new JSONObject();
					data.put("id", id);
					data.put("guest_name", guestName);
					message.put("data", data);
					
					server.send(message);
					
					String raw = joinReader.readLine();
					System.out.println("Joined " + raw);
					
					JSONObject response = (JSONObject) JSONValue.parse(raw);
					JSONObject startGameData = (JSONObject) response.get("data");
					
					String gameId = startGameData.get("id").toString();
					String hostName = startGameData.get("host_name").toString();
					
					final GameWindow guest = new GameWindow(guestName, Color.RED, Color.BLUE);
					guest.registerNames(hostName, guestName);
					guest.registerGameID(gameId);
					Platform.runLater(new Runnable() {
						
						@Override
						public void run() {
							PaperSoccer.getMainWindow().showSinglePlayerGameWindow(guest);
						}
					});
					
					RemoteGameController controller = new RemoteGameController(guest, gameId, gamePipe);
					
					controller.runGame();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        controllerThread.setDaemon(true);
        controllerThread.start();
	}

}
