package vava.controllers;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import vava.app.model.Event;
import vava.app.model.Location;
import vava.app.model.SportCategory;
import vava.components.EventPaneComponent;


public class MainViewController implements Initializable {
	String leftTitle = "Hi!";
	String nameUser = "Damian Majercak";
	@FXML ListView<EventPaneComponent> eventListView;
	@FXML Label titleLeftLabel;
	@FXML Label nameOfUserLabel;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		titleLeftLabel.setText(leftTitle);
		nameOfUserLabel.setText(nameUser);
		 List<EventPaneComponent> list = new ArrayList<>();
        list.add(new EventPaneComponent(new Event(1, 12, "Fc pod hrat", "skuska", Date.valueOf(LocalDate.now()), 30, 15, new SportCategory(4,"hockey"), "Krizova Ves", new Location())));
        list.add(new EventPaneComponent(new Event(1, 12, "Fc pod hrat", "skuska", Date.valueOf(LocalDate.now()), 30, 5, new SportCategory(2,"hockey"), "Krizova Ves", new Location())));

        ObservableList<EventPaneComponent> myObservableList = FXCollections.observableList(list);
         eventListView.setItems(myObservableList);
	}

}
