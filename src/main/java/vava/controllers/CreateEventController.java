package vava.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import vava.components.GmComponent;

public class CreateEventController implements Initializable{
	@FXML Pane gmapsPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gmapsPane.getChildren().add(new GmComponent(null).mapComponent);
		
	}
		
}
