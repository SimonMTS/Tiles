package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
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
	
	// TODO split GameController into controller and model
	
	@FXML
	private GridPane Board;
	
	@FXML
	private GridPane Dock1;

	@FXML
	private GridPane Dock2;

	@FXML
	private GridPane Dock3;
	
	public void initialize( URL arg0, ResourceBundle arg1 ) {
		
		new PuzzleShape( Dock1, this );
		new PuzzleShape( Dock2, this );
		new PuzzleShape( Dock3, this );
		
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

	    if ( db.hasString() && event.getTarget() instanceof StackPane ) {
	    	StackPane sp = (StackPane) event.getTarget();
	    	PuzzlePiece item = (PuzzlePiece) ((Node) event.getTarget()).getScene().lookup( "#"+db.getString() );
	    	
	    	if ( item.getShape().canPlace(event) ) {
	    		
	    		sp.getChildren().add(item);
		    	item.getShape().movePieces(event);
		    	item.getShape().show();
		    	
		        success = true;
		        
		        item.getShape().disableDraggable();
	    	}
	    	
	    }

	    event.setDropCompleted(success);
	    event.consume();
	    
	    removeLines();
	    refillDock();
	    
	    if ( hasLost() ) {
	    	System.out.println("u lose");
	    }
	}
	
	@FXML
	public void onDragDone( DragEvent event ) {
		if ( ((Node) event.getTarget()).getParent() != null && ((Node) event.getTarget()).getParent().getParent() != Board ) {
			Dragboard db = event.getDragboard();
			PuzzlePiece item = (PuzzlePiece) ((Node) event.getTarget()).getScene().lookup( "#"+db.getString() );
			
			item.getShape().show();
		}
	}
	
	private void refillDock() {
		
		if ( Dock1.getChildren().isEmpty() && Dock2.getChildren().isEmpty() && Dock3.getChildren().isEmpty() ) {

			new PuzzleShape( Dock1, this );
			new PuzzleShape( Dock2, this );
			new PuzzleShape( Dock3, this );
			
		}
		
	}
	
	private boolean hasLost() {
		
		
		return false;
	}
	
	private void removeLines() {
		
		ObservableList<Node> childrens = Board.getChildren();
		boolean[] removeRow = new boolean[10];
		boolean[] removeColumn = new boolean[10];
		
		for (int i=0; i<10; i++) {
			removeRow[i] = true;
			for ( Node node : childrens ) {
	            if ( GridPane.getRowIndex(node) == i && ((StackPane)node).getChildren().isEmpty() ) {
	            	removeRow[i] = false;
	            	break;
	            }
			}
		}
		
		for (int i=0; i<10; i++) {
			removeColumn[i] = true;
			for ( Node node : childrens ) {
	            if ( GridPane.getColumnIndex(node) == i && ((StackPane)node).getChildren().isEmpty() ) {
	            	removeColumn[i] = false;
	            	break;
	            }
			}
		}
		
		for (int i=0; i<10; i++) {
			if (removeRow[i]) {
				for ( Node node : childrens ) {
		            if ( GridPane.getRowIndex(node) == i ) {
		            	((StackPane)node).getChildren().clear();
		            }
				}
			}
			if (removeColumn[i]) {
				for ( Node node : childrens ) {
		            if ( GridPane.getColumnIndex(node) == i ) {
		            	((StackPane)node).getChildren().clear();
		            }
				}
			}
		}
		
	}
	
}
