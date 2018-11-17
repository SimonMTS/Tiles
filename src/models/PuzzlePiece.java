package models;

import java.util.UUID;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PuzzlePiece extends Rectangle {

	private PuzzleShape PuzzleShape;
	
	private int PuzzleShapeX;
	private int PuzzleShapeY;
	
	public PuzzlePiece( GameController GameController, PuzzleShape ps, int Xpos, int Ypos, Color Color ) {
		super(35, 35);
		
		PuzzleShape = ps;
		PuzzleShapeX = Xpos;
		PuzzleShapeY = Ypos;
		
		this.setId( String.valueOf( UUID.randomUUID() ) );
		
		this.setArcHeight( 5 );
		this.setArcWidth( 5 );
		
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
	
	public void animateDisappearance() {
		
//		this.setWidth(40);
//		this.setHeight(40);
		
	}
	
}
