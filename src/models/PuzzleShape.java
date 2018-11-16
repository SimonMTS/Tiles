package models;

import java.util.Random;

import application.GameController;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

public class PuzzleShape {

	private PuzzlePiece[][] PuzzlePieces = new PuzzlePiece[5][5];
	
	public PuzzleShape( String Placement, GridPane Dock, GameController GameController ) {
		
		Color Colr;
		
		// TODO: relate to shape
		int randomNumber = new Random().nextInt(19);
		
		switch ( randomNumber ) {
			case 0:
				Colr = Color.rgb(122,130,207);
//				Colr = Color.RED;
				break;
			case 1:
			case 2:
				Colr = Color.rgb(244,199,62);
				break;
			case 3:
			case 4:
				Colr = Color.rgb(219,140,69);
				break;
			case 5:
			case 6:
			case 7:
			case 8:
				Colr = Color.rgb(105,219,126);
				break;
			case 9:
			case 10:
				Colr = Color.rgb(211,82,122);
				break;
			case 11:
				Colr = Color.rgb(157,235,81);
				break;
			case 12:
			case 13:
				Colr = Color.rgb(199,82,79);
				break;
			case 14:
			case 15:
			case 16:
			case 17:
				Colr = Color.rgb(104,193,224);
				break;
			case 18:
				Colr = Color.rgb(97,229,172);
				break;
			default:
				Colr = Color.BLACK;
				break;
		}
		
		int Cplacement = 0;
		
		for (int i = 0; i<5; i++) {
			for (int j = 0; j<5; j++) {
			    char c = Placement.charAt(Cplacement);
			    
			    if ( c == 'x' ) {
			    	PuzzlePieces[i][j] = new PuzzlePiece(GameController, this, j, i, Colr);
			    	
			    	Dock.add(PuzzlePieces[i][j], j, i);
			    }
			    
			    Cplacement++;
			}
		}
		
	}
	
	public void hide() {
		for (int i=0; i<5; i++) {
	        for (int j=0; j<5; j++) {
	            if ( PuzzlePieces[i][j] instanceof PuzzlePiece ) {

		        	PuzzlePieces[i][j].setWidth(0);
		        	PuzzlePieces[i][j].setHeight(0);
		        	
	            }
	        }
	    }
	}
	
	public void show() {
		for (int i=0; i<5; i++) {
	        for (int j=0; j<5; j++) {
	            if ( PuzzlePieces[i][j] instanceof PuzzlePiece ) {

		        	PuzzlePieces[i][j].setWidth(35);
		        	PuzzlePieces[i][j].setHeight(35);
		        	
	            }
	        }
	    }
	}
	
	public void movePieces( DragEvent event ) {
		GridPane Board = (GridPane) ((StackPane) event.getTarget()).getParent();
		
    	PuzzlePiece DraggedItem = (PuzzlePiece) ((Node) event.getTarget()).getScene().lookup( "#"+event.getDragboard().getString() );
    	int RowOfOriginallyDraggedItem = GridPane.getRowIndex(DraggedItem.getParent());
    	int ColumnOfOriginallyDraggedItem = GridPane.getColumnIndex(DraggedItem.getParent());

		for (int i=0; i<5; i++) {
	        for (int j=0; j<5; j++) {
	            if ( PuzzlePieces[i][j] instanceof PuzzlePiece ) {
	            	
	            	int XPosOfThisItemOnBoard = RowOfOriginallyDraggedItem + (i - DraggedItem.getYpos());
	            	int YPosOfThisItemOnBoard = ColumnOfOriginallyDraggedItem + (j - DraggedItem.getXpos());


	            	ObservableList<Node> childrens = Board.getChildren();
	            	
        	        for ( Node node : childrens ) {
        	            if (
        	            	( 
        	            		GridPane.getRowIndex(node) == XPosOfThisItemOnBoard && 
        	            		GridPane.getColumnIndex(node) == YPosOfThisItemOnBoard 
        	            	) &&
        	            	( 
        	            		GridPane.getRowIndex(node) != RowOfOriginallyDraggedItem || 
        	            		GridPane.getColumnIndex(node) != ColumnOfOriginallyDraggedItem 
        	            	)
        	            ) {
        	            	((StackPane)node).getChildren().add( PuzzlePieces[i][j] );
        	            	System.out.println( "PlacedNode: "+ XPosOfThisItemOnBoard +"-"+ YPosOfThisItemOnBoard );
        	                break;
        	            }
        	        }
		        	
	            }
	        }
	    }
	}
	
	public WritableImage snapshot() {
		
		SnapshotParameters SnapShotParams = new SnapshotParameters();
        SnapShotParams.setFill(Color.TRANSPARENT);
        Scale Scale = new Scale( 2, 2 );
        SnapShotParams.setTransform(Scale);
		
        WritableImage SnapShot = null;
        
        for (int i=0; i<5; i++) {
	        for (int j=0; j<5; j++) {
	            if ( PuzzlePieces[i][j] instanceof PuzzlePiece ) {
	            	
	            	SnapShot = PuzzlePieces[i][j].getParent().snapshot(SnapShotParams, null);break;
	            	
	            }
	        }
		}
        
		return SnapShot;
		
	}
	
}
