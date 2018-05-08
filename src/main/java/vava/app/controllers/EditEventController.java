package vava.app.controllers;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import vava.app.Config;
import vava.app.PropertyManager;
import vava.app.components.GmComponent;
import vava.app.model.Dataset;
import vava.app.model.Event;
import vava.app.model.Location;
import vava.app.model.SportCategory;

public class EditEventController implements Initializable {
	@FXML
	private Pane gmapsPane;
	@FXML
	private Label titleDescriptionLabel;
	@FXML
	private Label titleLabel;
	@FXML
	private TextField eventNameTF;
	@FXML
	private TextField addressTF;
	@FXML
	private TextField neccessaryAgeTF;
	@FXML
	private TextField maxUsersTF;
	@FXML
	TextArea descriptionTA;
	@FXML
	private DatePicker eventDateDP;
	@FXML
	private Button updateButton;
	@FXML
	private ChoiceBox<SportCategory> sportCategoryChB;
	
	private LatLong location;
	private Event event;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		GmComponent gm = GmComponent.getInstance();
		gmapsPane.getChildren().add(gm.mapComponent);
		init();
		
		this.location = new LatLong(Dataset.getInstance().getLoggedIn().getAddressLocation().getLatitude(),Dataset.getInstance().getLoggedIn().getAddressLocation().getLongitude());
		gm.map.setCenter(this.location);
		gm.map.setZoom(11);
		EditEventController currentInstance = this;
		
		gmapsPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					GmComponent.getInstance().refillLatLong(currentInstance);
				}

			}

		});
		gm.map.setZoom(7);
		addressTF.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.ENTER) {
					gm.geocodingAddress(addressTF.getText(), currentInstance);
				}
			}
		});

	}
	public void fillEventObject(Event e) {
		this.event = e;
		eventNameTF.setText(e.getEventName());
		addressTF.setText(e.getAddress());
		descriptionTA.setText(e.getDescription());
		sportCategoryChB.setValue(e.getSportCategory());
		maxUsersTF.setText(e.getMaxUsersOnEvent() + "");
		neccessaryAgeTF.setText(e.getNecessaryAge() + "");
		eventDateDP.setValue(e.getDate().toLocalDate());
		location = new LatLong(e.getEventLocation().getLatitude(),e.getEventLocation().getLongitude());
		GmComponent.getInstance().map.setCenter(location);
		GmComponent.getInstance().map.setZoom(11);
		GmComponent.getInstance().map.clearMarkers();
		GmComponent.getInstance().map.addMarker(new Marker(new MarkerOptions().position(location)));
	}
	
	private void init() {
		List<SportCategory> list = null;
		
		//vytvorenie propertyManagera
		PropertyManager pm = new PropertyManager("");
		String language = pm.loadLanguageSet(getClass());
		
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
			RestTemplate template = context.getBean(RestTemplate.class);
			((ConfigurableApplicationContext) context).close();
			String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
			final String url = "http://" + ip + ":8009/events/categories";
			ResponseEntity<List<SportCategory>> returnedEntity = template.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<SportCategory>>() {
					});

			list = new ArrayList<>();
			for (SportCategory current : returnedEntity.getBody()) {
				list.add(current);
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObservableList<SportCategory> list2 = FXCollections.observableArrayList(list);
		sportCategoryChB.setConverter(new StringConverter<SportCategory>() {

			@Override
			public String toString(SportCategory object) {
				if ("sk".equals(language)) {
					return object.getSport_sk();
				}
				return object.getSport_en();
			}

			@Override
			public SportCategory fromString(String string) {
				return null;
			}
		});

		sportCategoryChB.setItems(list2);		
		titleLabel.setText(pm.getProperty("titleLabel"));
		titleDescriptionLabel.setText(pm.getProperty("titleDescriptionLabel"));
		updateButton.setText(pm.getProperty("updateButton"));
		eventNameTF.setPromptText(pm.getProperty("eventNameTF"));
		addressTF.setPromptText(pm.getProperty("addressTF"));
		eventDateDP.setPromptText(pm.getProperty("eventDateDP"));
		neccessaryAgeTF.setPromptText(pm.getProperty("neccessaryAgeTF"));
		maxUsersTF.setPromptText(pm.getProperty("maxUsersTF"));
		descriptionTA.setPromptText(pm.getProperty("descriptionTA"));
		Tooltip e = new Tooltip(pm.getProperty("sportCategoryChB"));
		sportCategoryChB.setTooltip(e);
	}

	public void fillLongLitude(LatLong l) {
		this.location = l;
		addMarker(this.location);
	}

	private void addMarker(LatLong l) {
		GmComponent.getInstance().map.clearMarkers();
		GmComponent.getInstance().map.addMarker(new Marker(new MarkerOptions().position(l)));
		GmComponent.getInstance().map.setCenter(l);
	}

	@FXML
	private void updateEventHandle(ActionEvent ev) {
		String eventNameString =  eventNameTF.getText();
		String addressString =  addressTF.getText();
		String neccesaryAgeString = neccessaryAgeTF.getText();
		String maxUserString = maxUsersTF.getText();
		String descriptionString = descriptionTA.getText();
		LocalDate date = eventDateDP.getValue();
		SportCategory category = sportCategoryChB.getValue();
		
		
		//kontorla vyplnenia udajov
		if(eventNameString.isEmpty() || addressString.isEmpty() || neccesaryAgeString.isEmpty()
				|| maxUserString.isEmpty() || descriptionString.isEmpty() || date == null || category == null) {
			new Alert(AlertType.ERROR, "Je nutne vyplnit vsetky udaje").showAndWait();
			return;
		}
		
		int maxUsers = 0;
		int neccesaryAge = 0;
		//parsovanie ciselnych hodnot
		try {
			maxUsers = Integer.parseInt(maxUserString);
			neccesaryAge = Integer.parseInt(neccesaryAgeString);
		} catch(NumberFormatException e) {
			new Alert(AlertType.ERROR, "Nespravne vyplnene udaje").showAndWait();
			return;
		}
		
		//vytvorenie noveho eventu
		event.setEventName(eventNameString);
		event.setAddress(addressString);
		event.setNecessaryAge(neccesaryAge);
		event.setMaxUsersOnEvent(maxUsers);
		event.setDescription(descriptionString);
		event.setSportCategory(category);
		event.setDate(Date.valueOf(date));
		event.setEventLocation(new Location(location.getLatitude(), location.getLongitude()));
		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		RestTemplate template = context.getBean(RestTemplate.class);
		((ConfigurableApplicationContext) context).close();
		
		String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
		final String url = "http://" + ip + ":8009/events/update";
		try {
			template.put(url, event, Void.class);
		} catch (HttpStatusCodeException e) {
			new Alert(AlertType.ERROR, "Event sa nepodarilo vytvorit").showAndWait();
			return;
		} catch (RestClientException e) {
			new Alert(AlertType.ERROR, "Chyba spojenia").showAndWait();
			return;
		}
		
		//zatvorenie okna
		Stage currentStage = (Stage) updateButton.getScene().getWindow();
		currentStage.close();
	}


}
