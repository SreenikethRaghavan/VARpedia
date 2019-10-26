package main.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
//import wikispeak.BashProcess;
import main.FXML.AppWindow;


/**
 * Controller for the view menu scene,
 * where the user is allowed to play and delete creations. 
 * 
 * @author Sreeniketh Raghavan
 * @author Hazel Williams
 * 
 */
public class ViewMenuController {


	@FXML
	private ListView<String> listView;

	@FXML
	private Button playButton;

	@FXML
	private Button pausePlayButton;

	@FXML
	private Button stopButton;

	@FXML
	private Button deleteButton;

	@FXML
	private MediaView mediaView;

	private MediaPlayer player;


	@FXML
	private void updateList() {
		List<String> creations = new ArrayList<String>();

		File[] files = new File("./creation_files/creations").listFiles();

		// remove creation extensions (.mp4)
		if (files.length != 0) {
			for (File file : files) {
				if (file.isFile()) {
					String name = file.getName();
					name = name.substring(0, name.lastIndexOf("."));
					creations.add(name);
				}
			}
		}
		else {
			// if no creations exist
			creations.add("No creations currently exist.");
			creations.add("Please create a creation to view it.");

			playButton.setDisable(true); 
			deleteButton.setDisable(true);


		}

		// sort alphabetically
		Collections.sort(creations);


		ObservableList<String> sorted = FXCollections.observableArrayList();

		int index = 1;

		String creationName = "";

		// number the creations, if there are creations
		for(String creation : creations) {
			if (files.length != 0) {
				//if there are files we want to number them
				creationName = index + ". " + creation;
			} else {
				//else we just want to add them as a message.
				creationName = creation;
			}
			sorted.add(creationName);
			index++;

		}


		listView.setItems(sorted);

		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);



	}




	@FXML
	private void initialize() {

		updateList();
		stopButton.setDisable(true);
		pausePlayButton.setDisable(true);

		//File fileUrl = new File("creation_files/temporary_files/video_files/output.mp4");
		File fileUrl = new File("src/main/images/defaultView.mp4");
		Media video = new Media(fileUrl.toURI().toString());
		player = new MediaPlayer(video);
		player.setAutoPlay(true);
		mediaView.setMediaPlayer(player);

	}


	@FXML
	private void togglePausePlay() {
		if (player.getStatus() == Status.PLAYING) {
			player.pause();
		} else {
			player.play();
		}
	}

	//resets the video to default view, ie stops the video.
	@FXML
	private void stopVideo() {
		File fileUrl = new File("src/main/images/defaultView.mp4");
		Media video = new Media(fileUrl.toURI().toString());
		player.pause();
		player = new MediaPlayer(video);
		player.setAutoPlay(true);
		mediaView.setMediaPlayer(player);
		stopButton.setDisable(true);
		pausePlayButton.setDisable(true);
	}

	@FXML
	private void deleteCreation() {

		String selected = listView.getSelectionModel().getSelectedItem();

		// if something is selected
		if (selected != null) {

			String selectedCreation = selected.substring(3);

			Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);

			deleteAlert.setTitle("Deletion Confirmation");
			deleteAlert.setHeaderText("Are you sure that you want to delete the creation '" + selectedCreation + "'?");
			deleteAlert.setContentText("This action CANNOT be undone!");

			deleteAlert.showAndWait().ifPresent(selection -> {

				if(selection == ButtonType.OK) {
					File file = new File("./creation_files/creations/"+ selectedCreation +".mp4");
					file.delete();
				}
			});

		}
		// update view
		updateList();
	}


	/*
	 * Changed 26/09/2019: No longer use ffmplay to play videos, instead embeds them in the gui
	 * Allows overriding, if you are already playing a creation you can click play on another creation and that will play instead.
	 */
	@FXML
	private void playCreation() {

		String selected = listView.getSelectionModel().getSelectedItem();

		if(selected == null) {
			playButton.setDisable(false);
		}

		else {
			stopButton.setDisable(false);
			pausePlayButton.setDisable(false);

			String selectedCreation = selected.substring(3);

			//sending to a different thread allows you to keep interacting with the GUI while the video is playing.
			Task<Void> task = new Task<Void>() {
				@Override protected Void call() throws Exception {
					//gets the selected file and plays it on the media view
					File fileUrl = new File("creation_files/creations/" + selectedCreation + ".mp4");
					Media video = new Media(fileUrl.toURI().toString());
					player = new MediaPlayer(video);
					player.setAutoPlay(true);
					mediaView.setMediaPlayer(player);
					player.setOnEndOfMedia(new Runnable() {
						@Override
						public void run() {
							//resets it back to default view once the video has finished playing
							File fileUrl = new File("src/main/images/defaultView.mp4");
							Media video = new Media(fileUrl.toURI().toString());
							player = new MediaPlayer(video);
							player.setAutoPlay(true);
							mediaView.setMediaPlayer(player);
							stopButton.setDisable(true);
							pausePlayButton.setDisable(true);
						}
					});



					return null;
				}
				@Override protected void done() {
					Platform.runLater(() -> {
						//doesnt need to run anything post play?

					});

				}
			};

			Thread thread = new Thread(task);

			thread.setDaemon(true);

			thread.start();

		}

	}


	@FXML
	private void returnToMainMenu(ActionEvent e) throws IOException {
		player.pause();
		AppWindow.valueOf("MainMenu").setScene(e);
		return;
	}
}