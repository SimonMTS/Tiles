package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Tiles");
        
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Game.fxml"));
        AnchorPane rootLayout = (AnchorPane) loader.load();
        
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

        GameController GameController = new GameController();
        GameController.setScene( scene );
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
