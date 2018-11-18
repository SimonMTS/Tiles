package application;
	
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import controllers.GameController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	private static String SaveInfo;
	private static String SaveFile = "savegame.txt";
//	private static String SaveFile = "cheatmode1.txt";
//	private static String SaveFile = "cheatmode2.txt";
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Tiles");
        
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("./../views/resources/Game.fxml"));
        AnchorPane rootLayout = (AnchorPane) loader.load();
        
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest((WindowEvent event1) -> {

			BufferedWriter writer;
			try {
				
				writer = new BufferedWriter(new FileWriter("./src/application/"+SaveFile));
				writer.write( SaveInfo );
                writer.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            System.exit(0);
        });
        
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void setSaveInfo( String info ) {
		SaveInfo = info;
	}
	
	public static String getSaveFile() {
		return SaveFile;
	}
}
