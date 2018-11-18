package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import application.Main;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import views.PuzzlePiece;
import views.PuzzleShape;

public class GameController implements Initializable {
	
	@FXML
	private GridPane Board;
	
	@FXML
	private GridPane Dock1;
	private PuzzleShape Dock1Shape;
	@FXML
	private GridPane Dock2;
	private PuzzleShape Dock2Shape;
	@FXML
	private GridPane Dock3;
	private PuzzleShape Dock3Shape;
	
	@FXML
	private Text Score;
	@FXML
	private Text HighScore;
	
	@FXML
	private Pane MenuPane;
	@FXML
	private Text ScoreText;
	@FXML
	private Text ScoreTypeText;
	@FXML
	private ImageView HighScoreImg;
	
	@FXML
	private Button ContinueBtn;
	
	
	public void initialize( URL arg0, ResourceBundle arg1 ) {
		
		if ( validSaveFile() ) {
			
			try (BufferedReader r = Files.newBufferedReader( Paths.get("./src/application/"+Main.getSaveFile()), Charset.defaultCharset() )) {
				Iterator<String> it = r.lines().iterator();
				
				String Placements[] = new String[19];
			    for (int i = 0; i < 13; i++) {
			        Placements[i] = it.next();
			    }
			    
			    String alphabet = "abcdefghijklmnopqrstuvwxyz";
			    String[] SavedRow;
			    for ( int i=0;i<10;i++ ) {
			    	SavedRow = Placements[i].split("");
			    	
			    	for ( int j=0;j<10;j++ ) {
			    		if ( !SavedRow[j].equals(".") ) {
			    			SavedRow[j] = SavedRow[j].toLowerCase();
			    			
			    			ObservableList<Node> childrens = Board.getChildren();
				    		for ( Node node : childrens ) {
				    			if ( GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j ) {
				    				
				    				((StackPane)node).getChildren().add( new PuzzlePiece( alphabet.indexOf(SavedRow[j].charAt(0)) ) );
				    				
				    			}
				    		}
				    		
			    		}
			    	}
			    }
			    
			    
			    String[] DockStorage = Placements[10].split(" ");
			    
			    if ( Integer.parseInt(DockStorage[0]) >= 0 ) {
			    	Dock1Shape = new PuzzleShape( Dock1, this, Integer.parseInt(DockStorage[0]) );
			    }
			    
			    if ( Integer.parseInt(DockStorage[1]) >= 0 ) {
			    	Dock2Shape = new PuzzleShape( Dock2, this, Integer.parseInt(DockStorage[1]) );
			    }
			    
			    if ( Integer.parseInt(DockStorage[2]) >= 0 ) {
			    	Dock3Shape = new PuzzleShape( Dock3, this, Integer.parseInt(DockStorage[2]) );
			    }
			    
			    Score.setText( Placements[11] );
			    HighScore.setText( Placements[12] );
				
			} catch (IOException e) {
				System.out.println("err");
			}
			
		} else {
			
			Dock1Shape = new PuzzleShape( Dock1, this );
			Dock2Shape = new PuzzleShape( Dock2, this );
			Dock3Shape = new PuzzleShape( Dock3, this );
			
		}
		
		saveGameInfo();
		
	}
	
	public void saveGameInfo() {
		String[] info = new String[105];
		
		int i = 5;
		ObservableList<Node> childrens = Board.getChildren();
		for ( Node node : childrens ) {
            
        	if ( ((StackPane)node).getChildren().isEmpty() ) {
        		info[ i ] = ".";
        	} else {
        		info[ i ] = ((PuzzlePiece) ((StackPane)node).getChildren().get(0)).getColorChar();
        	}
        	
        	i++;
    	}

		if ( !Dock3.getChildren().isEmpty() ) {
			info[4] = String.valueOf(Dock3Shape.getShapeNumber());
		} else {
			info[4] = String.valueOf(-1);
		}
		
		if ( !Dock2.getChildren().isEmpty() ) {
			info[3] = String.valueOf(Dock2Shape.getShapeNumber());
		} else {
			info[3] = String.valueOf(-1);
		}
		
		if ( !Dock1.getChildren().isEmpty() ) {
			info[2] = String.valueOf(Dock1Shape.getShapeNumber());
		} else {
			info[2] = String.valueOf(-1);
		}
		
		info[1] = Score.getText();
		info[0] = HighScore.getText();

		Main.setSaveInfo( info[5]+ info[6]+ info[7]+ info[8]+ info[9]+ info[10]+info[11]+info[12]+info[13]+info[14]+"\r\n" + 
				info[15]+info[16]+info[17]+info[18]+info[19]+info[20]+info[21]+info[22]+info[23]+info[24]+"\r\n" + 
				info[25]+info[26]+info[27]+info[28]+info[29]+info[30]+info[31]+info[32]+info[33]+info[34]+"\r\n" + 
				info[35]+info[36]+info[37]+info[38]+info[39]+info[40]+info[41]+info[42]+info[43]+info[44]+"\r\n" + 
				info[45]+info[46]+info[47]+info[48]+info[49]+info[50]+info[51]+info[52]+info[53]+info[54]+"\r\n" + 
				info[55]+info[56]+info[57]+info[58]+info[59]+info[60]+info[61]+info[62]+info[63]+info[64]+"\r\n" + 
				info[65]+info[66]+info[67]+info[68]+info[69]+info[70]+info[71]+info[72]+info[73]+info[74]+"\r\n" + 
				info[75]+info[76]+info[77]+info[78]+info[79]+info[80]+info[81]+info[82]+info[83]+info[84]+"\r\n" + 
				info[85]+info[86]+info[87]+info[88]+info[89]+info[90]+info[91]+info[92]+info[93]+info[94]+"\r\n" + 
				info[95]+info[96]+info[97]+info[98]+info[99]+info[100]+info[101]+info[102]+info[103]+info[104]+"\r\n" + 
				info[2]+" "+info[3]+" "+info[4]+" \r\n" + 
				info[1]+"\r\n" + 
				info[0]+"\r\n" + 
				"" );
		
	}
	
	@FXML
	public void onMenuClick( ActionEvent event ) {
		ScoreText.setText( String.valueOf( Integer.parseInt( Score.getText() ) ) );
		
		ScoreTypeText.setText( "You surrendered" );
		
		if ( Integer.parseInt( Score.getText() ) == Integer.parseInt( HighScore.getText() )  ) {
			HighScoreImg.setOpacity(1);
		} else {
			HighScoreImg.setOpacity(0);
		}
		
		MenuPane.setTranslateY(0);
	}
	
	@FXML
	public void onContinueClick( ActionEvent event ) {
		MenuPane.setTranslateY(700);
	}
	
	@FXML
	public void onRestartClick( ActionEvent event ) {
		
		// clear board
		ObservableList<Node> childrens = Board.getChildren();
		for ( Node node : childrens ) {
			((StackPane)node).getChildren().clear();
		}
		
		// clear dock
		Dock1.getChildren().clear();
		Dock2.getChildren().clear();
		Dock3.getChildren().clear();
		
		// refill dock
		refillDock();
		
		// set score to zero
		Score.setText( "0" );
		
		saveGameInfo();
		
		MenuPane.setTranslateY(700);
		
		ContinueBtn.setDisable(false);
		
	}
	
	@FXML
	public void onDragDetected( MouseEvent event ) {

		PuzzlePiece item = (PuzzlePiece) event.getSource();
		
        Dragboard db = item.startDragAndDrop(TransferMode.MOVE);
        
        double windowScaling = Screen.getPrimary().getOutputScaleX();
        db.setDragView(item.getShape().snapshot(), (17.5+(item.getXpos()*40))*windowScaling, (17.5+(item.getYpos()*40))*windowScaling);
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
		        
		        afterShapePlaced( item );
	    	}
	    	
	    }

	    event.setDropCompleted(success);
	    event.consume();
	    
	    if ( hasLost() ) {

	    	ScoreText.setText( String.valueOf( Integer.parseInt( Score.getText() ) ) );
			
			ScoreTypeText.setText( "No moves left" );
			
			ContinueBtn.setDisable(true);
			
			if ( Integer.parseInt( Score.getText() ) == Integer.parseInt( HighScore.getText() )  ) {
				HighScoreImg.setOpacity(1);
			} else {
				HighScoreImg.setOpacity(0);
			}
			
			MenuPane.setTranslateY(0);
	    	
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
	
	private void afterShapePlaced( PuzzlePiece PlacedPiece ) {
		
		int LinesRemoved = removeLines( PlacedPiece );
	    refillDock();
		
	    addScore( PlacedPiece.getShape(), LinesRemoved );
	    
	    saveGameInfo();
	    
	}
	
	private void refillDock() {
		
		if ( Dock1.getChildren().isEmpty() && Dock2.getChildren().isEmpty() && Dock3.getChildren().isEmpty() ) {

			Dock1Shape = new PuzzleShape( Dock1, this );
			Dock2Shape = new PuzzleShape( Dock2, this );
			Dock3Shape = new PuzzleShape( Dock3, this );
			
		}
		
	}
	
	private boolean hasLost() {
		boolean CanPlace = false;
		
		// for each GridPane position
		ObservableList<Node> childrens = Board.getChildren();
    	for ( Node node : childrens ) {
    		StackPane GridNode = ((StackPane)node);
            
    		if ( GridNode.getChildren().isEmpty() ) {
    			
    			// for each PuzzlePiece in Dock1
                ObservableList<Node> AllPuzzlePiecesDock1 = Dock1.getChildren();
                if ( AllPuzzlePiecesDock1.size() > 0 ) {
                	for ( Node Part : AllPuzzlePiecesDock1 ) {
                    	if ( ((PuzzlePiece)Part).getShape().canPlace( GridNode, ((PuzzlePiece)Part).getId() ) ) {
                    		CanPlace = true;
                    	}
                    }
                }
                
             // for each PuzzlePiece in Dock2
                ObservableList<Node> AllPuzzlePiecesDock2 = Dock2.getChildren();
                if ( AllPuzzlePiecesDock2.size() > 0 ) {
                	for ( Node Part : AllPuzzlePiecesDock2 ) {
                    	if ( ((PuzzlePiece)Part).getShape().canPlace( GridNode, ((PuzzlePiece)Part).getId() ) ) {
                    		CanPlace = true;
                    	}
                    }
                }
                
             // for each PuzzlePiece in Dock3
                ObservableList<Node> AllPuzzlePiecesDock3 = Dock3.getChildren();
                if ( AllPuzzlePiecesDock3.size() > 0 ) {
                	for ( Node Part : AllPuzzlePiecesDock3 ) {
                    	if ( ((PuzzlePiece)Part).getShape().canPlace( GridNode, ((PuzzlePiece)Part).getId() ) ) {
                    		CanPlace = true;
                    	}
                    }
                }
                
    		}
    			
    	}
		
		return !CanPlace;
	}
	
	private int removeLines( PuzzlePiece PlacedPiece ) {
		
		ObservableList<Node> childrens = Board.getChildren();
		int PuzzlePiecesRemoved = 0;
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
		            	((PuzzlePiece)((StackPane)node).getChildren().get(0)).remove( PlacedPiece, this, 'x' );
		            	PuzzlePiecesRemoved++;
		            }
				}
			}
			if (removeColumn[i]) {
				for ( Node node : childrens ) {
		            if ( GridPane.getColumnIndex(node) == i ) {
		            	((PuzzlePiece)((StackPane)node).getChildren().get(0)).remove( PlacedPiece, this, 'y' );
		            	PuzzlePiecesRemoved++;
		            }
				}
			}
		}
		
		if ( PuzzlePiecesRemoved > 0 ) {
			try {
				Clip clip = AudioSystem.getClip();
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(
				Main.class.getResourceAsStream("./../views/resources/splat.wav"));
				clip.open(inputStream);
				clip.start(); 
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		
		return PuzzlePiecesRemoved/10;
		
	}

	public void addScore( PuzzleShape PlacedShape, int LinesRemoved ) {
		
		int ShapeSize = PlacedShape.getNumberOfPuzzlePieces(),
			Bonus=0;
		
		switch ( LinesRemoved ) {
			case 0:
			case 1:
				Bonus = 0;
				break;
			case 2:
				Bonus = 10;
				break;
			case 3:
				Bonus = 30;
				break;
			case 4:
				Bonus = 60;
				break;
			case 5:
				Bonus = 100;
				break;
			case 6:
			default:
				Bonus = 150;
				break;
		}
		
		int ScoreThisTurn = ShapeSize + (10*LinesRemoved) + Bonus;
		
		Score.setText( String.valueOf( Integer.parseInt( Score.getText() )+ScoreThisTurn ) );
		
		if ( Integer.parseInt( Score.getText() ) > Integer.parseInt( HighScore.getText() ) ) {
			HighScore.setText( String.valueOf( Integer.parseInt( Score.getText() ) ) );
		}
		
	}
	
	public GridPane getBoard() {
		return Board;
	}
	
	private boolean validSaveFile() {
		
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get("./src/application/"+Main.getSaveFile()));
			String FileAsString = new String(encoded, Charset.defaultCharset());
			
//			((([a-s]|\.){10})\n){10}
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
