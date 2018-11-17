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
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Tiles");
        
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("./../views/Game.fxml"));
        AnchorPane rootLayout = (AnchorPane) loader.load();
        
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        GameController GameController = new GameController();
        
        primaryStage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Application Closed by click to Close Button(X)");
                        
                        BufferedWriter writer;
                        int[] info = GameController.getSaveGameInfo();
						try {
							
							writer = new BufferedWriter(new FileWriter("./src/application/savegame.txt"));
							writer.write("..........\r\n" + 
									"..........\r\n" + 
									"..........\r\n" + 
									"..........\r\n" + 
									"..........\r\n" + 
									"..........\r\n" + 
									"..........\r\n" + 
									"..........\r\n" + 
									"..........\r\n" + 
									"..........\r\n" + 
									"7 4 15 \r\n" + 
									info[0]+"\r\n" + 
									info[1]+"\r\n" + 
									"");
	                        writer.close();
							
						} catch (IOException e) {
							e.printStackTrace();
						}
                        
                        System.exit(0);
                    }
                });
            }
        });
        
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
