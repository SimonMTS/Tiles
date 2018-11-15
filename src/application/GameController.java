package application;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class GameController {
	
	private Scene scene;
	
	@FXML
	private Rectangle Rectangle1;
	
	@FXML
	private void Rectangle1Handle( MouseEvent event ) {
		System.out.println("onDragDetected");
		
        Dragboard db = Rectangle1.startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.putString(Rectangle1.getId());
        db.setContent(content);

        event.consume();
	}
	
	@FXML
	private void onDragOver( DragEvent event ) {
		System.out.println("onDragOver");

		if (
			event.getGestureSource() != null &&
			event.getDragboard().hasString()
	    ) {
	        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
	    }
		
	    event.consume();
	}
	
	@FXML
	private void onDragDropped( DragEvent event ) {
		System.out.println("onDragDropped");

		//Get the dragboard back
	    Dragboard db = event.getDragboard();
	    boolean success = false;
	    //Could have some more thorough checks of course.
	    if (db.hasString()) {
	        //Get the textarea and place it into flowPane2 instead
	    	System.out.println( "as" );
	    	
//	    	StackPane sp = (StackPane) scene.lookup("#StackPanel1");
	    	StackPane sp = (StackPane) event.getTarget();
	    	
	    	System.out.println( sp );
	    	sp.getChildren().add(Rectangle1);
	        success = true;
	    }
	    //Complete and consume the event.
	    event.setDropCompleted(success);
	    event.consume();
		
	    event.consume();
	}
	
	public void setScene( Scene scene ) {
		
		this.scene = scene;
		
	}
	
}
