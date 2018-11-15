package tiles;

import controllers.GameController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Tiles extends Application{

	public static void main(String[] args) {
		launch(args); 
	}
	
	@Override     
	public void start(Stage primaryStage) throws Exception { 
		
		primaryStage.setTitle("Tiles");
        
		GameController game = new GameController();
        
        StackPane root = new StackPane();
        root.getChildren().add( game.getBoard() );
        primaryStage.setScene(new Scene(root, 425, 700));
        primaryStage.show();
		
	}

}
