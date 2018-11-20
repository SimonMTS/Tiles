package models;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import application.Main;

public class Game {

	private PuzzlePiece[][] Grid = new PuzzlePiece[10][10];
	
	private int[] ShapesInDock = new int[3];
	
	private int Score;
	private int HighScore;
	
	public void addPuzzlePiece( int x, int y, char ColorInt ) {
		Grid[x][y] = new PuzzlePiece( ColorInt );
	}
	
	public void addPuzzlePieces( boolean[][] PlacedPieces, char ColorInt ) {
		
		for ( int i=0;i<10;i++ ) {
			for ( int j=0;j<10;j++ ) {
				if ( PlacedPieces[i][j] ) {
					Grid[i][j] = new PuzzlePiece( ColorInt );
				}
			}
		}
		
	}
	
	public void removePuzzlePiece( int x, int y ) {
		Grid[x][y] = null;
	}
	
	public void setShapeInDock( int ShapeNumber, int DockNumber ) {
		ShapesInDock[DockNumber-1] = ShapeNumber;
	}
	
	public void setScore( int Score ) {
		this.Score = Score;
	}
	
	public void setHighScore( int HighScore ) {
		this.HighScore = HighScore;
	}
	
	public void saveInfo() {
		
		String SaveFile = "";
		
		for ( int i=0;i<10;i++ ) {
			for ( int j=0;j<10;j++ ) {
				if ( Grid[i][j] != null ) {
				
					SaveFile += ""+Grid[i][j].getColor();
					
				} else {
					
					SaveFile += ".";
					
				}
			}
			SaveFile += "\r\n";
		}
		
		SaveFile += ShapesInDock[0]+" "+ShapesInDock[1]+" "+ShapesInDock[2]+" \r\n";
		
		SaveFile += Score+"\r\n" + HighScore+"\r\n";
		
		
		Main.setSaveInfo( SaveFile );
		
	}
	
	public boolean validSaveFile() {
		
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get("./src/application/"+Main.getSaveFile()));
			String FileAsString = new String(encoded, Charset.defaultCharset());
			
//			((([a-s]|\\.){10})\r\n){10})\r\n){10}
//			(-?[1-9]([0-8]|)|0) (-?[1-9]([0-8]|)|0) (-?[1-9]([0-8]|)|0) \r\n
//			\\d+\r\n
//			\\d+\r\n
//			
			return FileAsString.matches("((([a-s]|\\.){10})\r\n){10}(-?[1-9]([0-8]|)|0) (-?[1-9]([0-8]|)|0) (-?[1-9]([0-8]|)|0) \r\n\\d+\r\n\\d+\r\n");
			
		} catch (IOException e) {
			
			return false;
			
		}
		
	}
	
}
