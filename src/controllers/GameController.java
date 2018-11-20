package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import application.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.scene.text.Text;
import javafx.stage.Screen;
import views.PuzzlePieceView;
import views.PuzzleShapeView;
import models.Game;

public class GameController {
	
	private Game Game = new Game();
	
	@FXML
	private GridPane Board;
	
	@FXML
	private GridPane Dock1;
	private PuzzleShapeView Dock1Shape;
	@FXML
	private GridPane Dock2;
	private PuzzleShapeView Dock2Shape;
	@FXML
	private GridPane Dock3;
	private PuzzleShapeView Dock3Shape;
	
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
	
	
	public void initialize() {
		
		if ( Game.validSaveFile() ) {
			
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
				    				
				    				((StackPane)node).getChildren().add( new PuzzlePieceView( alphabet.indexOf(SavedRow[j].charAt(0)) ) );
				    				
				    				Game.addPuzzlePiece(i, j, SavedRow[j].charAt(0));
				    				
				    			}
				    		}
				    		
			    		}
			    	}
			    }
			    
			    
			    String[] DockStorage = Placements[10].split(" ");
			    
			    if ( Integer.parseInt(DockStorage[0]) >= 0 ) {
			    	Dock1Shape = new PuzzleShapeView( Dock1, this, Integer.parseInt(DockStorage[0]) );
			    	Game.setShapeInDock( Dock1Shape.getShapeNumber(), 1 );
			    } else {
			    	Game.setShapeInDock( -1, 1 );
			    }
			    
			    if ( Integer.parseInt(DockStorage[1]) >= 0 ) {
			    	Dock2Shape = new PuzzleShapeView( Dock2, this, Integer.parseInt(DockStorage[1]) );
			    	Game.setShapeInDock( Dock2Shape.getShapeNumber(), 2 );
			    } else {
			    	Game.setShapeInDock( -1, 2 );
			    }
			    
			    if ( Integer.parseInt(DockStorage[2]) >= 0 ) {
			    	Dock3Shape = new PuzzleShapeView( Dock3, this, Integer.parseInt(DockStorage[2]) );
			    	Game.setShapeInDock( Dock3Shape.getShapeNumber(), 3 );
			    } else {
			    	Game.setShapeInDock( -1, 3 );
			    }
			    
			    Score.setText( Placements[11] );
			    Game.setScore( Integer.parseInt( Placements[11] ) );
			    
			    HighScore.setText( Placements[12] );
			    Game.setHighScore( Integer.parseInt( Placements[12] ) );
				
			} catch (IOException e) {
				System.out.println("err");
			}
			
		} else {
			
			Dock1Shape = new PuzzleShapeView( Dock1, this );
			Dock2Shape = new PuzzleShapeView( Dock2, this );
			Dock3Shape = new PuzzleShapeView( Dock3, this );
			
			Game.setShapeInDock( Dock1Shape.getShapeNumber(), 1 );
			Game.setShapeInDock( Dock2Shape.getShapeNumber(), 2 );
			Game.setShapeInDock( Dock3Shape.getShapeNumber(), 3 );
			
		}
		
		Game.saveInfo();
		
		new java.util.Timer().schedule( 
		    new java.util.TimerTask() {
		        @Override
		        public void run() {
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
		    }, 100
		);
		
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
	public void onCheatClick( ActionEvent event ) {

		Score.setText( "0" );
		Game.setScore(0);
		
		Dock1.getChildren().clear();
		Dock2.getChildren().clear();
		Dock3.getChildren().clear();
		
		refillDock();
		
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
		
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				Game.removePuzzlePiece(i, j);
			}
		}
		
		// clear dock
		Dock1.getChildren().clear();
		Dock2.getChildren().clear();
		Dock3.getChildren().clear();
		
		// refill dock
		refillDock();
		
		// set score to zero
		Score.setText( "0" );
		Game.setScore(0);
		
		Game.saveInfo();
		
		MenuPane.setTranslateY(700);
		
		ContinueBtn.setDisable(false);
		
	}
	
	@FXML
	public void onDragDetected( MouseEvent event ) {

		PuzzlePieceView item = (PuzzlePieceView) event.getSource();
		
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
	    	PuzzlePieceView item = (PuzzlePieceView) ((Node) event.getTarget()).getScene().lookup( "#"+db.getString() );
	    	
	    	if ( item.getShape().canPlace(event) ) {
	    		
	    		sp.getChildren().add(item);
	    		boolean[][] PlacedPieces = item.getShape().movePieces(event);
		    	item.getShape().show();
		    	
		    	Game.addPuzzlePieces(PlacedPieces, item.getColorChar().charAt(0));
		    	
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
			PuzzlePieceView item = (PuzzlePieceView) ((Node) event.getTarget()).getScene().lookup( "#"+db.getString() );
			
			item.getShape().show();
		}
	}
	
	private void afterShapePlaced( PuzzlePieceView PlacedPiece ) {
		
		int LinesRemoved = removeLines( PlacedPiece );
	    refillDock();
		
	    addScore( PlacedPiece.getShape(), LinesRemoved );
	    
	    Game.saveInfo();
	    
	}
	
	private void refillDock() {
		
		if ( Dock1.getChildren().isEmpty() && Dock2.getChildren().isEmpty() && Dock3.getChildren().isEmpty() ) {

			Dock1Shape = new PuzzleShapeView( Dock1, this );
			Dock2Shape = new PuzzleShapeView( Dock2, this );
			Dock3Shape = new PuzzleShapeView( Dock3, this );
			
			Game.setShapeInDock( Dock1Shape.getShapeNumber(), 1 );
			Game.setShapeInDock( Dock2Shape.getShapeNumber(), 2 );
			Game.setShapeInDock( Dock3Shape.getShapeNumber(), 3 );
			
		}
		
		if ( Dock1.getChildren().isEmpty() ) {
			Game.setShapeInDock( -1, 1 );
		}
		if ( Dock2.getChildren().isEmpty() ) {
			Game.setShapeInDock( -1, 2 );
		}
		if ( Dock3.getChildren().isEmpty() ) {
			Game.setShapeInDock( -1, 3 );
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
                    	if ( ((PuzzlePieceView)Part).getShape().canPlace( GridNode, ((PuzzlePieceView)Part).getId() ) ) {
                    		CanPlace = true;
                    	}
                    }
                }
                
             // for each PuzzlePiece in Dock2
                ObservableList<Node> AllPuzzlePiecesDock2 = Dock2.getChildren();
                if ( AllPuzzlePiecesDock2.size() > 0 ) {
                	for ( Node Part : AllPuzzlePiecesDock2 ) {
                    	if ( ((PuzzlePieceView)Part).getShape().canPlace( GridNode, ((PuzzlePieceView)Part).getId() ) ) {
                    		CanPlace = true;
                    	}
                    }
                }
                
             // for each PuzzlePiece in Dock3
                ObservableList<Node> AllPuzzlePiecesDock3 = Dock3.getChildren();
                if ( AllPuzzlePiecesDock3.size() > 0 ) {
                	for ( Node Part : AllPuzzlePiecesDock3 ) {
                    	if ( ((PuzzlePieceView)Part).getShape().canPlace( GridNode, ((PuzzlePieceView)Part).getId() ) ) {
                    		CanPlace = true;
                    	}
                    }
                }
                
    		}
    			
    	}
		
		return !CanPlace;
	}
	
	private int removeLines( PuzzlePieceView PlacedPiece ) {
		
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
		            	((PuzzlePieceView)((StackPane)node).getChildren().get(0)).remove( PlacedPiece, this, 'x' );
		            	PuzzlePiecesRemoved++;
		            }
				}
				for (int j=0; j<10; j++) {
					Game.removePuzzlePiece( i, j );
				}
			}
			if (removeColumn[i]) {
				for ( Node node : childrens ) {
		            if ( GridPane.getColumnIndex(node) == i ) {
		            	((PuzzlePieceView)((StackPane)node).getChildren().get(0)).remove( PlacedPiece, this, 'y' );
		            	PuzzlePiecesRemoved++;
		            }
				}
				for (int j=0; j<10; j++) {
					Game.removePuzzlePiece( j, i );
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

	public void addScore( PuzzleShapeView PlacedShape, int LinesRemoved ) {
		
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
		int TotalScore = Integer.parseInt( Score.getText() )+ScoreThisTurn;
		
		Score.setText( String.valueOf( TotalScore ) );
		Game.setScore( TotalScore );
		
		if ( Integer.parseInt( Score.getText() ) > Integer.parseInt( HighScore.getText() ) ) {
			HighScore.setText( String.valueOf( Integer.parseInt( Score.getText() ) ) );
			Game.setHighScore( Integer.parseInt( Score.getText() ) );
		}
		
	}
	
	public GridPane getBoard() {
		return Board;
	}
	
	public void SaveGameInfo() {
		Game.saveInfo();
	}

	
}
