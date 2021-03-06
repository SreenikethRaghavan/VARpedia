package varpedia.controllers.create.wikisearch;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import varpedia.scene.AppWindow;
import varpedia.controllers.AssociationClass;


/**
 * Controller for the search error scene,
 * where the user is allowed to enter a new 
 * term or return to the main menu.
 * 
 * @author Sreeniketh Raghavan
 * @author Hazel Williams
 * 
 */
public class SearchErrorController {
	
	@FXML
	private Text searchTxt;

	@FXML
	private void initialize() {
		searchTxt.setText("Sorry! Wikipedia didn't find anything for " + AssociationClass.getInstance().getSearchTerm());
		
	}
	
	@FXML
	private void search(ActionEvent event) throws IOException {

		AppWindow.valueOf("CreateMenu").setScene(event);
		return;
	}

	@FXML
	private void quit(ActionEvent event) throws IOException {
		AppWindow.valueOf("MainMenu").setScene(event);
		return;
	}

}
