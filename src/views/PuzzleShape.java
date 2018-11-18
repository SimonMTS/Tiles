package views;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;

import controllers.GameController;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.util.Duration;

public class PuzzleShape {

	private int ShapeNumber;
	
	private PuzzlePiece[][] PuzzlePieces = new PuzzlePiece[5][5];
	
	public PuzzleShape( GridPane Dock, GameController GameController ) {
		PuzzleShapeConst( Dock, GameController, new Random().nextInt(19) );
	}
	
	public PuzzleShape( GridPane Dock, GameController GameController, int sn ) {
		PuzzleShapeConst( Dock, GameController, sn );
	}
	
	public void PuzzleShapeConst( GridPane Dock, GameController GameController, int sn ) {
		
		ShapeNumber = sn;
		String Placement = "xxx..xxx..xxx............";
		
		try (BufferedReader r = Files.newBufferedReader( Paths.get("./src/application/config.txt"), Charset.defaultCharset() )) {
			Iterator<String> it = r.lines().iterator();
			
			String Placements[] = new String[19];
		    for (int i = 0; i < 19; i++) {
		        Placements[i] = it.next();
		    }
			
		    Placement = Placements[ ShapeNumber ];
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Color Colr;
		
		switch ( ShapeNumber ) {
			case 0:
				Colr = Color.rgb(122,130,207);
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
		
		for (int i=0; i<5; i++) {
	        for (int j=0; j<5; j++) {
	            if ( PuzzlePieces[i][j] instanceof PuzzlePiece ) {

	            	Translate TranslateDown = new Translate(0, 300);
		        	PuzzlePieces[i][j].getTransforms().addAll(TranslateDown);
		        	
		        	TranslateTransition tt = new TranslateTransition(Duration.millis(200), PuzzlePieces[i][j]);
	            	tt.setByY(-300);
	                tt.play();
		        	
	            }
	        }
	    }
		
	}
	
	public int getShapeNumber() {
		
		return ShapeNumber;
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
        	                break;
        	            }
        	        }
		        	
	            }
	        }
	    }
	}
	
	public void disableDraggable() {
		
		for (int i=0; i<5; i++) {
	        for (int j=0; j<5; j++) {
	            if ( PuzzlePieces[i][j] instanceof PuzzlePiece ) {
	            	
	            	PuzzlePieces[i][j].setOnDragDetected(new EventHandler <MouseEvent>() {

						@Override
						public void handle(MouseEvent arg0) { }});
	            	
	            }
	        }
		}
		
	}
	
	public boolean canPlace( StackPane StackPane, String PuzzleId ) {
		
		return canPlaceCalc( StackPane, PuzzleId );
		
	}
	
	public boolean canPlace( DragEvent event ) {
		
		StackPane sp = (StackPane) event.getTarget();
		String PuzzleId = event.getDragboard().getString();
		
		return canPlaceCalc( sp, PuzzleId );
		
	}
	
	private boolean canPlaceCalc( StackPane sp, String PuzzleId ) {
		
		GridPane Board = (GridPane) sp.getParent();
		
    	PuzzlePiece DraggedItem = (PuzzlePiece) ((Node) sp).getScene().lookup( "#"+PuzzleId );
    	int RowOfOriginallyDraggedItem = GridPane.getRowIndex((Node) sp);
    	int ColumnOfOriginallyDraggedItem = GridPane.getColumnIndex((Node) sp);

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
        	            	
        	            	if ( !((StackPane)node).getChildren().isEmpty() ) {
            	            	return false;
        	            	}
        	            	
        	            } else if (
        	            	XPosOfThisItemOnBoard > 9 || XPosOfThisItemOnBoard < 0 || 
	        				YPosOfThisItemOnBoard > 9 || YPosOfThisItemOnBoard < 0
        	            ) {
        	            	return false;
        	            }
        	            
        	        }
		        	
	            }
	        }
	    }
		
		return true;
	}
	
	public int getNumberOfPuzzlePieces() {
		
		int NumberOfPuzzlePieces = 0;
		
		for (int i=0; i<5; i++) {
	        for (int j=0; j<5; j++) {
	            if ( PuzzlePieces[i][j] instanceof PuzzlePiece ) {
	            	NumberOfPuzzlePieces++;
	            }
	        }
		}
		
		return NumberOfPuzzlePieces;
	}
	
	public WritableImage snapshot() {
		
		SnapshotParameters SnapShotParams = new SnapshotParameters();
        SnapShotParams.setFill(Color.TRANSPARENT);
        
        double windowScaling = Screen.getPrimary().getOutputScaleX();
        
        Scale Scale = new Scale( 2*windowScaling, 2*windowScaling );
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
