package vava.app.controllers;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
//github.com/vavagit/JoinMeClient
import vava.app.model.SportCategory;

public class CreateEventsController implements Initializable {

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
	private Button createButton;
	@FXML
	private ChoiceBox<SportCategory> sportCategoryChB;
	
	private LatLong location;

	private static Logger logger = LogManager.getLogger(CreateEventsController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//pridanie mapy do okna
		GmComponent gm = GmComponent.getInstance();
		gmapsPane.getChildren().add(gm.mapComponent);
		
		init();
		gm.map.setCenter(new LatLong(Dataset.getInstance().getLoggedIn().getAddressLocation().getLatitude(),Dataset.getInstance().getLoggedIn().getAddressLocation().getLongitude()));
		gm.map.setZoom(11);
		logger.debug("initialize, nastavenie mapy na polohu uzivatela");
		CreateEventsController currentInstance = this;
		
		gmapsPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					logger.debug("handle2, Nastavujem lokaciu");
					GmComponent.getInstance().refillLatLong(currentInstance);
				}

			}

		});
		gm.map.setZoom(7);
		addressTF.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.ENTER) {
					logger.debug("handle2, zistujem suradnice: " + addressTF.getText());
					gm.geocodingAddress(addressTF.getText(), currentInstance);
				}
			}
		});

	}

	private void init() {
		List<SportCategory> list = null;
		
		//vytvorenie propertyManagera
		PropertyManager pm = new PropertyManager("");
		String language = pm.loadLanguageSet(getClass());
		
		logger.debug("init, Nastavenie jazyka: " + language);
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
			RestTemplate template = context.getBean(RestTemplate.class);
			((ConfigurableApplicationContext) context).close();
			
			//ziskanie konfiguracie
			String port = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("port");
			String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
			final String url = "http://" + ip + ":"+port+"/events/categories";
			
			//odoslanie poziadavky na server
			logger.debug("init, ziskanie sportovych kategorii: " + url);
			ResponseEntity<List<SportCategory>> returnedEntity = template.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<SportCategory>>() {
					});

			list = new ArrayList<>();
			for (SportCategory current : returnedEntity.getBody()) {
				list.add(current);
			}
			logger.debug("init, Kategorie pridane do listu");
		} catch (RestClientException e) {
			logger.catching(Level.ERROR, e);
			new Alert(AlertType.ERROR, pm.getProperty("connectionError")).showAndWait();
		}
		ObservableList<SportCategory> list2 = FXCollections.observableArrayList(list);
		//nastavenie convertera
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

		//nastavenie obsahu okna
		sportCategoryChB.setItems(list2);
		sportCategoryChB.getSelectionModel().selectFirst();
		
		logger.debug("init, nastavenie textu komponentom: multilanguage");
		
		titleLabel.setText(pm.getProperty("titleLabel"));
		titleDescriptionLabel.setText(pm.getProperty("titleDescriptionLabel"));
		createButton.setText(pm.getProperty("createButton"));
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
		//pridanie markera na mapu
		addMarker(this.location);
	}

	private void addMarker(LatLong l) {
		logger.debug("addMarker, nastavenie markera: (" + l.getLatitude() + ", " + l.getLongitude() + ")");
		GmComponent.getInstance().map.clearMarkers();
		GmComponent.getInstance().map.addMarker(new Marker(new MarkerOptions().position(l)));
		GmComponent.getInstance().map.setCenter(l);
	}

	@FXML
	private void createEventHandle(ActionEvent event) {
		String eventNameString =  eventNameTF.getText();
		String addressString =  addressTF.getText();
		String neccesaryAgeString = neccessaryAgeTF.getText();
		String maxUserString = maxUsersTF.getText();
		String descriptionString = descriptionTA.getText();
		LocalDate date = eventDateDP.getValue();
		SportCategory category = sportCategoryChB.getValue();
		
		PropertyManager propertyManager = new PropertyManager("");
		propertyManager.loadLanguageSet(getClass());
		
		//kontorla vyplnenia udajov
		if(eventNameString.isEmpty() || addressString.isEmpty() || neccesaryAgeString.isEmpty()
				|| maxUserString.isEmpty() || descriptionString.isEmpty() || date == null || category == null) {
			new Alert(AlertType.ERROR, propertyManager.getProperty("fillDataError")).showAndWait();
			return;
		}
		
		int maxUsers = 0;
		int neccesaryAge = 0;
		//parsovanie ciselnych hodnot
		try {
			maxUsers = Integer.parseInt(maxUserString);
			neccesaryAge = Integer.parseInt(neccesaryAgeString);
		} catch(NumberFormatException e) {
			new Alert(AlertType.ERROR, propertyManager.getProperty("dataFormatError")).showAndWait();
			logger.debug("Nespravne vyplnene ciselne udaje");
			return;
		}
		
		logger.info("createEventHandle, Poziadavka na vytvorenie eventu " + eventNameString );
		
		//vytvorenie noveho eventu
		Event created = new Event(0, maxUsers, eventNameString, descriptionString, Date.valueOf(date),
				neccesaryAge, Dataset.getInstance().getLoggedIn().getId(), category, addressString,
				new Location(location.getLatitude(), location.getLongitude()));
		
		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		RestTemplate template = context.getBean(RestTemplate.class);
		((ConfigurableApplicationContext) context).close();
		
		//ziskanie konfiguracie
		String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
		String port = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("port");
		final String url = "http://" + ip + ":"+port+"/events";
		
		logger.debug("createEventHandle, Konfiguracia ziskana: " + url);
		
		try {
			//odoslanie poziadavky
			template.postForEntity(url, created, Void.class);
			logger.info("createEventHandle, Poziadavka spracovana event vytvoreny");
		} catch (HttpStatusCodeException e) {
			new Alert(AlertType.ERROR, propertyManager.getProperty("eventNotCreated")).showAndWait();
			logger.info("createEventHandle, Poziadavka spracovana nastala chyba");
			return;
		} catch (RestClientException e) {
			new Alert(AlertType.ERROR, propertyManager.getProperty("connectionError")).showAndWait();
			logger.catching(Level.ERROR, e);
			return;
		}
		
		logger.debug("createEventHandle, zatvaram okno");
		//zatvorenie okna
		Stage currentStage = (Stage) createButton.getScene().getWindow();
		currentStage.close();
	}

}
