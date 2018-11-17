package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
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
	
	public void initialize( URL arg0, ResourceBundle arg1 ) {
		
		if ( true ) {
			
			try (BufferedReader r = Files.newBufferedReader( Paths.get("./src/application/savegame.txt"), Charset.defaultCharset() )) {
				Iterator<String> it = r.lines().iterator();
				
				String Placements[] = new String[19];
			    for (int i = 0; i < 13; i++) {
			        Placements[i] = it.next();
			    }
			    
			    
			    new PuzzleShape( Dock1, this, Integer.parseInt( Placements[10].split(" ")[0] ) );
				new PuzzleShape( Dock2, this, Integer.parseInt( Placements[10].split(" ")[1] ) );
				new PuzzleShape( Dock3, this, Integer.parseInt( Placements[10].split(" ")[2] ) );
			    
			    Score.setText( Placements[11] );
			    HighScore.setText( Placements[12] );
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			
			new PuzzleShape( Dock1, this );
			new PuzzleShape( Dock2, this );
			new PuzzleShape( Dock3, this );
			
		}
		
	}
	
	public int[] getSaveGameInfo() {
		int[] info = new int[2];
//		System.out.println( Score.getText() );
//		info[0] = Integer.parseInt( Score.getText() );
//		info[1] = Integer.parseInt( HighScore.getText() );
		info[0] = 0;
		info[1] = 1589;
		
		return info;
	}
	
	@FXML
	public void onMenuClick( ActionEvent event ) {
		ScoreText.setText( String.valueOf( Integer.parseInt( Score.getText() ) ) );
		
		if ( true ) {
			ScoreTypeText.setText( "No moves left" );
		} else {
			ScoreTypeText.setText( "You surrendered" );
		}
		
		if ( true ) {
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
		        
		        afterShapePlaced( item.getShape() );
	    	}
	    	
	    }

	    event.setDropCompleted(success);
	    event.consume();
	    
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
	
	private void afterShapePlaced( PuzzleShape PlacedShape ) {
		
		int LinesRemoved = removeLines();
	    refillDock();
		
	    addScore( PlacedShape, LinesRemoved );
	    
	}
	
	private void refillDock() {
		
		if ( Dock1.getChildren().isEmpty() && Dock2.getChildren().isEmpty() && Dock3.getChildren().isEmpty() ) {

			new PuzzleShape( Dock1, this );
			new PuzzleShape( Dock2, this );
			new PuzzleShape( Dock3, this );
			
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
	
	private int removeLines() {
		
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
		            	((StackPane)node).getChildren().clear();
		            	PuzzlePiecesRemoved++;
		            }
				}
			}
			if (removeColumn[i]) {
				for ( Node node : childrens ) {
		            if ( GridPane.getColumnIndex(node) == i ) {
		            	((StackPane)node).getChildren().clear();
		            	PuzzlePiecesRemoved++;
		            }
				}
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
	
}
