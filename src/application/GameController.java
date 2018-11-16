package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.PuzzlePiece;
import models.PuzzleShape;

public class GameController implements Initializable {
	
	@FXML
	private GridPane Board;
	
	@FXML
	private GridPane Dock;
	
	public void initialize( URL arg0, ResourceBundle arg1 ) {
//		System.out.println( Dock );
//		Dock.add(new PuzzlePiece(this), 2, 1);
//		Dock.add(new PuzzlePiece(this), 2, 2);
//		Dock.add(new PuzzlePiece(this), 2, 3);
		
		new PuzzleShape( "x....x....x....x.........", 1, Dock, this );
		new PuzzleShape( "x....xx..................", 2, Dock, this );
		new PuzzleShape( "xxx..xxx..xxx............", 3, Dock, this );
		
	}
	
	@FXML
	public void onDragDetected( MouseEvent event ) {
//		System.out.println("onDragDetected");

		PuzzlePiece item = (PuzzlePiece) event.getSource();
		
        Dragboard db = item.startDragAndDrop(TransferMode.MOVE);
        
        item.getShape().makeFullSize();
        
        db.setDragView(item.getShape().snapshot(), 17.5, 17.5);
        item.getShape().hide();

        ClipboardContent content = new ClipboardContent();
        content.putString(item.getId());
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
	    	PuzzlePiece item = (PuzzlePiece) ((Node) event.getTarget()).getScene().lookup( "#"+db.getString() );
	    	
	    	sp.getChildren().add(item);
	    	item.getShape().movePieces(event);
	    	item.getShape().makeFullSize();
	    	
//	    	System.out.println( sp );
	    	System.out.println( GridPane.getRowIndex(sp) +"-"+ GridPane.getColumnIndex(sp) );
	        success = true;
	    }

	    event.setDropCompleted(success);
	    event.consume();
	}
	
	@FXML
	public void onDragDone( DragEvent event ) {
		if ( ((Node) event.getTarget()).getParent().getParent() != Board ) {
			Dragboard db = event.getDragboard();
			PuzzlePiece item = (PuzzlePiece) ((Node) event.getTarget()).getScene().lookup( "#"+db.getString() );
			
			item.getShape().makeSmall();
		}
	}
	
}
