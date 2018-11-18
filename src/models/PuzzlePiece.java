package models;

import java.util.UUID;

import controllers.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class PuzzlePiece extends Rectangle {

	private PuzzleShape PuzzleShape;
	private int SoloPieceColor = -10;
	
	private int PuzzleShapeX;
	private int PuzzleShapeY;
	
	public PuzzlePiece( int ColorInt ) {
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
	
	public PuzzlePiece( GameController GameController, PuzzleShape ps, int Xpos, int Ypos, Color Color ) {
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
	
	public PuzzleShape getShape() {
		return PuzzleShape; 
	}
	
	public int getXpos() {
		return PuzzleShapeX;
	}
	
	public int getYpos() {
		return PuzzleShapeY;
	}
	
	public String getColorChar() {
		
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		if ( SoloPieceColor >= 0 ) {
			
			return ""+alphabet.charAt( SoloPieceColor );
			
		} else {
			
			return ""+alphabet.charAt( PuzzleShape.getShapeNumber() );
			
		}
		
	}
	
	public void remove() {
		
		this.setHeight(40);
		this.setWidth(40);
		
		KeyValue HeightValue = new KeyValue(this.heightProperty(), 0);
        KeyFrame HeightFrame = new KeyFrame(Duration.millis(100), HeightValue);
        Timeline HeightTimeline = new Timeline(HeightFrame);
		
		KeyValue widthValue = new KeyValue(this.widthProperty(), 0);
        KeyFrame WidthFrame = new KeyFrame(Duration.millis(100), widthValue);
        Timeline WidthTimeline = new Timeline(WidthFrame);
        
        WidthTimeline.setOnFinished(event -> {

        	((StackPane)this.getParent()).getChildren().remove(this);
        	
    	});
        WidthTimeline.play();
        HeightTimeline.play();
		
	}
}
