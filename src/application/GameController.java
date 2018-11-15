package application;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class GameController {
	
	@FXML
	private GridPane Board;
	
	@FXML
	private Rectangle Rectangle1;
	
	@FXML
	private void onDragDetected( MouseEvent event ) {
//		System.out.println("onDragDetected");
		
        Dragboard db = Rectangle1.startDragAndDrop(TransferMode.MOVE);
        
        Rectangle1.setWidth(35);
		Rectangle1.setHeight(35);
        
        db.setDragView(Rectangle1.snapshot(null, null));
        Rectangle1.setWidth(0);
		Rectangle1.setHeight(0);

        ClipboardContent content = new ClipboardContent();
        content.putString(Rectangle1.getId());
        db.setContent(content);

        event.consume();
	}
	
	@FXML
	private void onDragOver( DragEvent event ) {
//		System.out.println("onDragOver");

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
//		System.out.println("onDragDropped");

	    Dragboard db = event.getDragboard();
	    boolean success = false;

	    if (db.hasString()) {
	    	StackPane sp = (StackPane) event.getTarget();
	    	sp.getChildren().add(Rectangle1);
	    	Rectangle1.setWidth(35);
			Rectangle1.setHeight(35);
	    	
//	    	System.out.println( sp );
	    	System.out.println( GridPane.getRowIndex(sp) +"-"+ GridPane.getColumnIndex(sp) );
	        success = true;
	    }

	    event.setDropCompleted(success);
	    event.consume();
	}
	
	@FXML
	private void onDragDone( DragEvent event ) {
		if ( ((Node) event.getTarget()).getParent().getParent() != Board ) {
			Rectangle1.setWidth(20);
			Rectangle1.setHeight(20);
		}
	}
	
}
