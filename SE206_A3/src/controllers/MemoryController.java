package controllers;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import FXML.AppWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MemoryController {

    @FXML
    private Text result;

    @FXML
    private ImageView imageView0;

    @FXML
    private Text text0;

    @FXML
    private ImageView imageView1;

    @FXML
    private Text text1;

    @FXML
    private ImageView imageView2;

    @FXML
    private Text text2;

    @FXML
    private ImageView imageView3;

    @FXML
    private Text text3;

    @FXML
    private ImageView imageView4;

    @FXML
    private Text text4;

    @FXML
    private ImageView imageView5;

    @FXML
    private Text text5;

    @FXML
    private ImageView imageView6;

    @FXML
    private Text text6;

    @FXML
    private ImageView imageView7;

    @FXML
    private Text text7;
    
    private ImageView[] imageViews;
    private Text[] texts;
    private int revealcount = 0;
    private String fileLocation = "./creation_files/memory_files";
    private ObservableList<String> mFiles;
    private String[] cards = {"X","X","X","X","X","X","X","X"};
    private Boolean[] isImage = {false,false,false,false,false,false,false,false};
    private Boolean[] revealed = {false,false,false,false,false,false,false,false};
    private Boolean[] won = {false,false,false,false,false,false,false,false};
    
    @FXML
	private void initialize() {
    	imageViews = new ImageView[] {imageView0,imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7};
    	texts = new Text[] {text0,text1,text2,text3,text4,text5,text6,text7};
    	
    	
    	//we need to get the files from the folder now.
    	mFiles = getURLList();
    	
    	ObservableList<Integer> availableIndexs = FXCollections.observableArrayList();
    	for (int i = 0; i<=7; i++) {
    		availableIndexs.add(i);
    	}
    	Collections.shuffle(availableIndexs);
    	
    	
    	
    	//with that list we want to distribute them across the board
    	//there are 8 card. which means we need 4 files.
    	//this leaves us with the following cases
    	//case 1: there are >=4 files, so pick 4.
    	//case 2: there are less than 4 files. so duplicate some.
    	if(mFiles.size() >=4) {
    		//pick the first 4
    		for(int i = 0; i < 4; i++) {
    			//String text = mFiles.get(i).substring(mFiles.get(i).lastIndexOf("/")+1, mFiles.get(i).lastIndexOf(".")).trim().replace('-', ' ');
    			//texts[availableIndexs.get(0)].setText(text);
    			//texts[availableIndexs.get(1)].setText(text);
    			cards[availableIndexs.get(0)]=mFiles.get(i);
    			cards[availableIndexs.get(1)]=mFiles.get(i);
    			isImage[availableIndexs.get(0)]=true;
    			isImage[availableIndexs.get(1)]=false;
    			availableIndexs.remove(0);
    			availableIndexs.remove(0);
    		}
    	} else {
    		for(int i = 0; i < mFiles.size(); i++) {
    			cards[availableIndexs.get(0)]=mFiles.get(i);
    			cards[availableIndexs.get(1)]=mFiles.get(i);
    			isImage[availableIndexs.get(0)]=true;
    			isImage[availableIndexs.get(1)]=false;
    			availableIndexs.remove(0);
    			availableIndexs.remove(0);
    		}
    		//get the extra ones
    		if(mFiles.size() == 0) {
    			System.out.println("No files");
    		} else if(mFiles.size() == 1) {
    			for(int i =0; i<3; i++) {
    				cards[availableIndexs.get(0)]=mFiles.get(0);
        			cards[availableIndexs.get(1)]=mFiles.get(0);
        			isImage[availableIndexs.get(0)]=true;
        			isImage[availableIndexs.get(1)]=false;
        			availableIndexs.remove(0);
        			availableIndexs.remove(0);
    			}
    		} else if(mFiles.size() == 2) {
				cards[availableIndexs.get(0)]=mFiles.get(0);
    			cards[availableIndexs.get(1)]=mFiles.get(0);
    			isImage[availableIndexs.get(0)]=true;
    			isImage[availableIndexs.get(1)]=false;
    			availableIndexs.remove(0);
    			availableIndexs.remove(0);
				cards[availableIndexs.get(0)]=mFiles.get(1);
    			cards[availableIndexs.get(1)]=mFiles.get(1);
    			isImage[availableIndexs.get(0)]=true;
    			isImage[availableIndexs.get(1)]=false;
    			availableIndexs.remove(0);
    			availableIndexs.remove(0);   			
    		} else if(mFiles.size() == 3) {
				cards[availableIndexs.get(0)]=mFiles.get(0);
    			cards[availableIndexs.get(1)]=mFiles.get(0);
    			isImage[availableIndexs.get(0)]=true;
    			isImage[availableIndexs.get(1)]=false;
    			availableIndexs.remove(0);
    			availableIndexs.remove(0); 			
    		}
    	}
    	
    	//now all the arrays are set up. we can start.	
    	
	}
    
	private ObservableList<String> getURLList() {
		List<String> creations = new ArrayList<String>();

		File[] files = new File(fileLocation).listFiles();

		if (files.length != 0) {
			for (File file : files) {
				if (file.isFile()) {
					String name = file.getName();
					name = name.substring(0, name.lastIndexOf("."));
					creations.add(name);
				}
			}
		}

		Collections.shuffle(creations);

		ObservableList<String> sorted = FXCollections.observableArrayList();

		// add the videos to the observable list
		for(String creation : creations) {
			sorted.add(fileLocation+"/"+creation+".jpg");
		}
		
		return sorted;


	}

    
    private void clearBoard() {
		File imageFile = new File("./src/images/blankcard.png");
		Image image = new Image("file:" + imageFile.getPath().substring(2));

    	for(int i = 0; i<8;i++) {
    		texts[i].setText("");
    		imageViews[i].setImage(image);
    		if(won[i]) {
    			texts[i].setText("Correct!");
    			texts[i].setFill(Color.GREEN);
    		} else {
    			revealed[i] = false;
    		}
    	}
    }
    
    
    
    private boolean isMatch() {
    	int tocheck1 = 34;
        int tocheck2 = 34;
    	
    	for(int i=0; i<8; i++) {
    		if(revealed[i] && !won[i]) {
    			//visable but not yet won
    			if(tocheck1 ==34) {
    				tocheck1 = i;
    			} else {
    				tocheck2 = i;
    			}
    		}
    	}
    	
    	//have the indexs we need to check now.
    	
    	//if they are both images or both text they arent a match
    	if(isImage[tocheck1] && isImage[tocheck2]) {
    		return false;
    	}
    	if(!isImage[tocheck1] && !isImage[tocheck2]) {
    		return false;
    	}
    	
    	//they are the same
    	if(cards[tocheck1].equals(cards[tocheck2])) {
    		won[tocheck1] = true;
    		won[tocheck2] = true;
    		return true;
    	}
    	
    	return false;
    }
    
    private void reveal(int num) {
    	if (revealcount == 2) {
    		//clear board
    		clearBoard();
    		revealcount = 0;
    	}
    	if(!revealed[num]) {
        	if(isImage[num]) {
        		//gotta work with the image
        		File imageFile = new File(cards[num]);
        		Image image = new Image("file:" + imageFile.getPath().substring(2));
        		imageViews[num].setImage(image);
        	} else {
        		texts[num].setText(cards[num].substring(cards[num].lastIndexOf("/")+1, cards[num].lastIndexOf(".")).trim().replace('-', ' '));
        	}
        	revealcount++;
        	revealed[num]=true;
    	}
    	if(revealcount == 2) {
    		if(isMatch()) {
    			result.setText("Thats correct! (+10XP)");
    			AssociationClass.getInstance().increaseXP(10);
    			result.setFill(Color.GREEN);
    		} else {
    			result.setText("Try Again");
    			result.setFill(Color.RED);
    		}
    	}
    	
    	//check if gameover
    	int correctCount =0;
    	for(int i =0; i<8; i++) {
    		if(won[i]) {
    			correctCount++;
    		}
    	}
    	
    	if(correctCount==8) {
    		Alert completedQuiz = new Alert(Alert.AlertType.INFORMATION);
			completedQuiz.setTitle("Memory Completed");
			completedQuiz.setHeaderText("Congratulations! You have successfully completed the game :D");
			completedQuiz.setContentText("Return to the learn menu to choose a new game.");
			completedQuiz.showAndWait();
    	}
    	//else its already visable

    }
    
    @FXML
    void reveal0(MouseEvent event) {
    	reveal(0);
    }

    @FXML
    void reveal1(MouseEvent event) {
    	reveal(1);
    }

    @FXML
    void reveal2(MouseEvent event) {
    	reveal(2);
    }

    @FXML
    void reveal3(MouseEvent event) {
    	reveal(3);
    }

    @FXML
    void reveal4(MouseEvent event) {
    	reveal(4);
    }

    @FXML
    void reveal5(MouseEvent event) {
    	reveal(5);
    }

    @FXML
    void reveal6(MouseEvent event) {
    	reveal(6);
    }

    @FXML
    void reveal7(MouseEvent event) {
    	reveal(7);
    }

    @FXML
	private void returnToMainMenu(javafx.event.ActionEvent e) throws IOException {

		AppWindow.valueOf("LearnMenu").setScene(e);
	}

}