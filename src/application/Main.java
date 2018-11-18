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
	private static String[] SaveInfo;
	
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
        
        primaryStage.setOnCloseRequest((WindowEvent event1) -> {

        				BufferedWriter writer;
						try {
							
							writer = new BufferedWriter(new FileWriter("./src/application/savegame.txt"));
							writer.write(
									SaveInfo[5]+ SaveInfo[6]+ SaveInfo[7]+ SaveInfo[8]+ SaveInfo[9]+ SaveInfo[10]+SaveInfo[11]+SaveInfo[12]+SaveInfo[13]+SaveInfo[14]+"\r\n" + 
									SaveInfo[15]+SaveInfo[16]+SaveInfo[17]+SaveInfo[18]+SaveInfo[19]+SaveInfo[20]+SaveInfo[21]+SaveInfo[22]+SaveInfo[23]+SaveInfo[24]+"\r\n" + 
									SaveInfo[25]+SaveInfo[26]+SaveInfo[27]+SaveInfo[28]+SaveInfo[29]+SaveInfo[30]+SaveInfo[31]+SaveInfo[32]+SaveInfo[33]+SaveInfo[34]+"\r\n" + 
									SaveInfo[35]+SaveInfo[36]+SaveInfo[37]+SaveInfo[38]+SaveInfo[39]+SaveInfo[40]+SaveInfo[41]+SaveInfo[42]+SaveInfo[43]+SaveInfo[44]+"\r\n" + 
									SaveInfo[45]+SaveInfo[46]+SaveInfo[47]+SaveInfo[48]+SaveInfo[49]+SaveInfo[50]+SaveInfo[51]+SaveInfo[52]+SaveInfo[53]+SaveInfo[54]+"\r\n" + 
									SaveInfo[55]+SaveInfo[56]+SaveInfo[57]+SaveInfo[58]+SaveInfo[59]+SaveInfo[60]+SaveInfo[61]+SaveInfo[62]+SaveInfo[63]+SaveInfo[64]+"\r\n" + 
									SaveInfo[65]+SaveInfo[66]+SaveInfo[67]+SaveInfo[68]+SaveInfo[69]+SaveInfo[70]+SaveInfo[71]+SaveInfo[72]+SaveInfo[73]+SaveInfo[74]+"\r\n" + 
									SaveInfo[75]+SaveInfo[76]+SaveInfo[77]+SaveInfo[78]+SaveInfo[79]+SaveInfo[80]+SaveInfo[81]+SaveInfo[82]+SaveInfo[83]+SaveInfo[84]+"\r\n" + 
									SaveInfo[85]+SaveInfo[86]+SaveInfo[87]+SaveInfo[88]+SaveInfo[89]+SaveInfo[90]+SaveInfo[91]+SaveInfo[92]+SaveInfo[93]+SaveInfo[94]+"\r\n" + 
									SaveInfo[95]+SaveInfo[96]+SaveInfo[97]+SaveInfo[98]+SaveInfo[99]+SaveInfo[100]+SaveInfo[101]+SaveInfo[102]+SaveInfo[103]+SaveInfo[104]+"\r\n" + 
									SaveInfo[2]+" "+SaveInfo[3]+" "+SaveInfo[4]+" \r\n" + 
									SaveInfo[1]+"\r\n" + 
									SaveInfo[0]+"\r\n" + 
									"");
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
	
	public static void setSaveInfo( String[] info ) {

		SaveInfo = info;
		
	}
}
