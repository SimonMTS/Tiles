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
	private GridPane Dock1;

	@FXML
	private GridPane Dock2;

	@FXML
	private GridPane Dock3;
	
	public void initialize( URL arg0, ResourceBundle arg1 ) {
		
		new PuzzleShape( "x....x....x....x.........", Dock1, this );
		new PuzzleShape( "x....xx..................", Dock2, this );
//		new PuzzleShape( "xxx..xxx..xxx............", Dock3, this );
		
//		new PuzzleShape( "xxxxxxxxxxxxxxxxxxxxxxxxxx", Dock1, this );
//		new PuzzleShape( "xxxxxxxxxxxxxxxxxxxxxxxxxx", Dock2, this );
		new PuzzleShape( "xxxxxxxxxxxxxxxxxxxxxxxxxx", Dock3, this );
		
	}
	
	@FXML
	public void onDragDetected( MouseEvent event ) {

		PuzzlePiece item = (PuzzlePiece) event.getSource();
		
        Dragboard db = item.startDragAndDrop(TransferMode.MOVE);
        
        db.setDragView(item.getShape().snapshot(), 17.5+(item.getXpos()*40), 17.5+(item.getYpos()*40));
        item.getShape().hide();

        ClipboardContent content = new ClipboardContent();
        content.putString(item.getId());
        db.setContent(content);

        event.consume();
	}
	
	@FXML
	private void onDragOver( DragEvent event ) {

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

	    Dragboard db = event.getDragboard();
	    boolean success = false;

	    if (db.hasString()) {
	    	StackPane sp = (StackPane) event.getTarget();
	    	PuzzlePiece item = (PuzzlePiece) ((Node) event.getTarget()).getScene().lookup( "#"+db.getString() );
	    	
	    	sp.getChildren().add(item);
	    	item.getShape().movePieces(event);
	    	item.getShape().show();
	    	
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
			
			item.getShape().show();
		}
	}
	
}
