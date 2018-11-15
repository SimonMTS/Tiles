package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class GameController {
	
	private Button board;
	
	public Button getBoard() {
		return board;
	}
	
	public GameController() {
		
		board = new Button();
		board.setText("Say 'Hello World'");
		board.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
		
	}
	
}
