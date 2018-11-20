package views;

import java.util.UUID;

import controllers.GameController;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class PuzzlePieceView extends Rectangle {

	private PuzzleShapeView PuzzleShape;
	private int SoloPieceColor = -10;
	
	private int PuzzleShapeX;
	private int PuzzleShapeY;
	
	public PuzzlePieceView( int ColorInt ) {
		super(35, 35);
		
		SoloPieceColor = ColorInt;
		
		Color Colr;
		switch ( ColorInt ) {
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
		
		this.setArcHeight( 10 );
		this.setArcWidth( 10 );
		
		this.setFill( Colr );
		
	}
	
	public PuzzlePieceView( GameController GameController, PuzzleShapeView ps, int Xpos, int Ypos, Color Color ) {
		super(35, 35);
		
		PuzzleShape = ps;
		PuzzleShapeX = Xpos;
		PuzzleShapeY = Ypos;
		
		this.setId( String.valueOf( UUID.randomUUID() ) );
		
		this.getStyleClass().add("draggable");
		
		this.setArcHeight( 10 );
		this.setArcWidth( 10 );
		
		this.setFill( Color );
		
		this.setOnDragDetected(new EventHandler <MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				GameController.onDragDetected( arg0 );
				
			}
		});
		
		this.setOnDragDone(new EventHandler <DragEvent>() {

			@Override
			public void handle(DragEvent arg0) {
				
				GameController.onDragDone( arg0 );
				
			}
		});
		
	}
	
	public PuzzleShapeView getShape() {
		return PuzzleShape; 
	}
	
	public int getXpos() {
		return PuzzleShapeX;
	}
	
	public int getYpos() {
		return PuzzleShapeY;
	}
	
	public int getGridX( GameController GameController ) {
		ObservableList<Node> childrens = GameController.getBoard().getChildren();
	    for ( int i=0;i<10;i++ ) {
	    	for ( int j=0;j<10;j++ ) {
	    		for ( Node node : childrens ) {
	    			if ( this.getParent() == node && GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j ) {
	    				
	    				return j;
	    				
	    			}
	    		}		
	    	}
	    }
	    
	    return 0;
	}
	
	public int getGridY( GameController GameController ) {
		ObservableList<Node> childrens = GameController.getBoard().getChildren();
	    for ( int i=0;i<10;i++ ) {
	    	for ( int j=0;j<10;j++ ) {
	    		for ( Node node : childrens ) {
	    			if ( this.getParent() == node && GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j ) {
	    				
	    				return i;
	    				
	    			}
	    		}		
	    	}
	    }
	    
	    return 0;
	}
	
	public String getColorChar() {
		
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		if ( SoloPieceColor >= 0 ) {
			
			return ""+alphabet.charAt( SoloPieceColor );
			
		} else {
			
			return ""+alphabet.charAt( PuzzleShape.getShapeNumber() );
			
		}
		
	}
	
	public void remove( PuzzlePieceView PlacedPiece, GameController GameController, char Direction ) {
		
		int PlacedPos;
		if ( Direction == 'y' ) {
			PlacedPos = PlacedPiece.getGridY( GameController );
		} else {
			PlacedPos = PlacedPiece.getGridX( GameController );
		}
		
		ObservableList<Node> childrens = GameController.getBoard().getChildren();
		int MyPos = 0;
	    for ( int i=0;i<10;i++ ) {
	    	for ( int j=0;j<10;j++ ) {
	    		for ( Node node : childrens ) {
	    			if ( this.getParent() == node && GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j ) {
	    				
	    				if ( Direction == 'y' ) {
	    					MyPos = i;
	    				} else {
	    					MyPos = j;
	    				}
	    				
	    			}
	    		}		
	    	}
	    }
	    
	    int Difference = 0;
        if ( MyPos <= PlacedPos ) {
        	Difference = (PlacedPos - MyPos);
        }
        if ( MyPos >= PlacedPos ) {
        	Difference = (MyPos - PlacedPos);
        }
		
		TranslateTransition Place = new TranslateTransition(Duration.millis(500), this);
		Place.setToX( this.getX() );
		Place.setToY( this.getY() );
		
		ScaleTransition Scale = new ScaleTransition(Duration.millis(500), this);
		Scale.setFromX(1);
		Scale.setFromY(1);
		Scale.setToX(0);
		Scale.setToY(0);
		
		Scale.setOnFinished(event -> {
        	
        	((StackPane)this.getParent()).getChildren().remove(this);
        	
        	GameController.SaveGameInfo();
        	
    	});
		
		ParallelTransition pt = new ParallelTransition(Scale, Place);
		

		SequentialTransition seq = new SequentialTransition( new PauseTransition(Duration.millis(Difference*50)), pt );

		seq.play();
		
	}
}
