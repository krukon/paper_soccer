package ui;

/**
 * Class representing a network games list window.
 * 
 * @author ljk
 */

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.concurrent.atomic.AtomicBoolean;

import helpers.Player;
import controller.PaperSoccer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import controller.PaperSoccerController;
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
import model.Board;

public class GamesListWindow extends BorderPane{

	private Text windowTitle = new Text("Network game is waiting for you!");
	private Insets insets = new Insets(25, 25, 25, 25);

	public GamesListWindow(String player)
	{		
		windowTitle.setFill(Color.WHITE);
		
		//setStyle("-fx-background-image: url('paper_soccer_background.jpg'); -fx-background-repeat: stretch;");
		setPadding(insets);
		setTop(addWindowTitle());
		setCenter(addGridPane());
		setBottom(addBackButton());
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
 
        private Record(int id, String name, int boardWidth, int boardHeight) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.boardWidth = new SimpleIntegerProperty(boardWidth);
            this.boardHeight = new SimpleIntegerProperty(boardHeight);
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
 
    private void prepareRecordList() {
        recordList.add(new Record(12, "LJK", 10, 10));
        recordList.add(new Record(15, "Krukon", 12, 8));
        recordList.add(new Record(1, "Kuba", 14, 6));
    }
 
    
    public void addList(GridPane grid) { 
        prepareRecordList();
 
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
 
        TableColumn colId = new TableColumn("ID");
        colId.setCellValueFactory(
                new PropertyValueFactory<Record, String>("id"));
        colId.setCellFactory(integerCellFactory);
        
        
        TableColumn colName = new TableColumn("Name");
        colName.setCellValueFactory(
                new PropertyValueFactory<Record, String>("name"));
        colName.setCellFactory(stringCellFactory);
        colName.setMinWidth(70);
        
        
        TableColumn colBoardWidth = new TableColumn("Width");
        colBoardWidth.setCellValueFactory(
                new PropertyValueFactory<Record, String>("BoardWidth"));
        colBoardWidth.setCellFactory(integerCellFactory);
        colBoardWidth.setMinWidth(60);
        
        TableColumn colBoardHeight = new TableColumn("Height");
        colBoardHeight.setCellValueFactory(
                new PropertyValueFactory<Record, String>("BoardHeight"));
        colBoardHeight.setCellFactory(integerCellFactory);
        colBoardHeight.setMinWidth(60);
        
        tableView.setItems(recordList);
        tableView.getColumns().addAll(colId, colName, colBoardWidth, colBoardHeight);
         
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
            TableCell c = (TableCell) t.getSource();
            int index = c.getIndex();
            System.out.println("id = " + recordList.get(index).getId());
            System.out.println("name = " + recordList.get(index).getName());
            System.out.println("board width = " + recordList.get(index).getBoardWidth());
            System.out.println("board height = " + recordList.get(index).getBoardHeight());
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

}
