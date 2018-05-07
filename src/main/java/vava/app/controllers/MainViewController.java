package vava.app.controllers;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import vava.app.components.EventPaneComponent;
import vava.app.model.Event;
import vava.app.model.Location;
import vava.app.model.SportCategory;


public class MainViewController implements Initializable {
	String leftTitle = "Hi!";
	String nameUser = "Damian Majercak";
	String locationLabelS = "Lokacia: ";
	@FXML ListView<EventPaneComponent> eventListView;
	@FXML Label titleLeftLabel;
	@FXML Label nameOfUserLabel;
	@FXML Label locationLabel;
	@FXML TextField locationTextField;
	@FXML Label rangeLabel;
	@FXML TextField rangeTextField;
	@FXML Button filterButton;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		titleLeftLabel.setText(leftTitle);
		nameOfUserLabel.setText(nameUser);
		locationLabel.setText(locationLabelS);
		rangeLabel.setText("Range: ");
		List<EventPaneComponent> list = new ArrayList<>();
        list.add(new EventPaneComponent(new Event(1, 12, "Fc pod hrat", "skuska", Date.valueOf(LocalDate.now()), 30, 15, new SportCategory(4,"hockey"), "Krizova Ves", new Location())));
        list.add(new EventPaneComponent(new Event(1, 12, "Fc pod hrat", "skuska", Date.valueOf(LocalDate.now()), 30, 5, new SportCategory(2,"hockey"), "Krizova Ves", new Location())));

        ObservableList<EventPaneComponent> myObservableList = FXCollections.observableList(list);
         eventListView.setItems(myObservableList);
	
         
	
	}
	@FXML
	private void mouseEnteredFilterBt(MouseEvent event) {
		filterButton.setStyle("-fx-background-color: #6dcde9");
		//filterButton.getStyleClass().add("buttonChange");
	}
	
	@FXML
	private void mouseExitedFilterBt(MouseEvent event) {
		filterButton.setStyle("-fx-background-color: #19b9e7");
		//filterButton.getStyleClass().remove("buttonChange");
	}
}
