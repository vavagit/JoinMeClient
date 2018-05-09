package vava.app.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vava.app.Config;
import vava.app.PropertyManager;
import vava.app.controllers.EventDescriptionController;
import vava.app.controllers.MainViewController;
import vava.app.model.Dataset;
import vava.app.model.Event;
import vava.app.model.User;
import vava.app.model.communication.RestTemplateFactory;

public class EventPaneComponent extends HBox{
	private ImageView img = new ImageView();
    private Label title = new Label();
    private VBox vboxPicWithTitle = new VBox();
     //--------druhy stlpec
    private VBox info = new VBox();
    private HBox infoR1 = new HBox();
    private HBox infoR2 = new HBox();
    private HBox infoR3 = new HBox();
    private Label date = new Label();
    private Label dateValue = new Label();
    private Label address = new Label();
    private Label valueOfAddress = new Label();
    private Label numberOfUsers = new Label();
    private Label numberOfUsersValue = new Label();
    private Event event;
    private EventHandler<MouseEvent> join;
    private List<User> joinedUser;
    private EventHandler<MouseEvent> leave;
    private MainViewController mwc;
     //---------------------------------------
     
    public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	//-----------------treti stplec-----
    private Button buttonJoin = new Button();
    private Button detailBt = new Button();
    private VBox vboxBt = new VBox();
    private String buttonJoinedS = "";
    private String buttonJoinS = "";
    
    
     private void init() {
    	 title.setWrapText(true);
    	 buttonJoin.setMaxWidth(Double.MAX_VALUE);
 		//refillJoinedUser();
 		    	 
    	 detailBt.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent ev) {
				
				Stage s =(Stage) detailBt.getScene().getWindow();
				Stage newS = new Stage();
				newS.initOwner(s);
				//newS.setAlwaysOnTop(true);
				newS.setTitle("JoinMe - Event detail");
				newS.initModality(Modality.WINDOW_MODAL);
		        newS.setResizable(false);
		        //nastavenie ikony
		        Image image = new Image(getClass().getResourceAsStream("/img/titleIco.jpg"));
		        newS.getIcons().add(image);
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/app/views/EventDescription.fxml"));
					Parent root = loader.load();
					Scene scene = new Scene(root);
			        EventDescriptionController ec = loader.getController();
			        ec.fillEventObject(event);
					newS.setScene(scene);
			        newS.show();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		});
    	 join = new EventHandler<MouseEvent>() {
 			@Override
 			public void handle(MouseEvent event) {
 				//users/idUsera/event/ideventu
 				ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
 				RestTemplate template = context.getBean(RestTemplate.class);
 				((ConfigurableApplicationContext) context).close();
 				
 				String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
 				String port = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("port");
 				final String url = "http://" + ip + ":"+port+"/users/"+Dataset.getInstance().getLoggedIn().getId()+"/event/"+EventPaneComponent.this.event.getEventId();
 				try {
 					template.postForEntity(url, null, Void.class);
 					
 					buttonJoin.setText(buttonJoinedS);
 					buttonJoin.removeEventHandler(MouseEvent.MOUSE_CLICKED, join);
 					buttonJoin.addEventHandler(MouseEvent.MOUSE_CLICKED, leave);
 					mwc.loadEvents(Dataset.getInstance().getLoggedIn().getAddressLocation(), 1000);
 				} catch (HttpStatusCodeException e) {
 					new Alert(AlertType.ERROR, "Nepodarilo sa pripojic").showAndWait();
 					return;
 				} catch (RestClientException e) {
 					new Alert(AlertType.ERROR, "Chyba spojenia").showAndWait();
 					return;
 				}
 				
 			}
     		 
 		};
 		leave = new EventHandler<MouseEvent>() {
 			@Override
 			public void handle(MouseEvent event) {
 				//users/idUsera/event/ideventu
 				ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
 				RestTemplate template = context.getBean(RestTemplate.class);
 				((ConfigurableApplicationContext) context).close();
 				
 				String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
 				String port = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("port");
 				final String url = "http://" + ip + ":"+port+"/users/"+Dataset.getInstance().getLoggedIn().getId()+"/event/"+EventPaneComponent.this.event.getEventId();
 				try {
 					template.delete(url, null, Void.class);
 					
 					buttonJoin.setText(buttonJoinS);
 					buttonJoin.removeEventHandler(MouseEvent.MOUSE_CLICKED, leave);
 					buttonJoin.addEventHandler(MouseEvent.MOUSE_CLICKED, join);
 					mwc.loadEvents(Dataset.getInstance().getLoggedIn().getAddressLocation(), 1000);
 				} catch (HttpStatusCodeException e) {
 					new Alert(AlertType.ERROR, "Nepodarilo sa leavnut").showAndWait();
 					return;
 				} catch (RestClientException e) {
 					new Alert(AlertType.ERROR, "Chyba spojenia").showAndWait();
 					return;
 				}
 				
 			}
     		 
 		};
 		
 		
    	 
    	 
     }
     
    public EventPaneComponent(Event e,MainViewController mwc) {
		super();
		event = e;
		this.mwc = mwc;
		//init
		refillJoinedUser();
		title.getStyleClass().add("vbLabel");
		date.getStyleClass().add("vbLabel");
		address.getStyleClass().add("vbLabel");
		numberOfUsers.getStyleClass().add("vbLabel");
		dateValue.getStyleClass().addAll("vbLabel","vbValueLabel");
		valueOfAddress.getStyleClass().addAll("vbLabel","vbValueLabel");
		numberOfUsersValue.getStyleClass().addAll("vbLabel","vbValueLabel");
		// prvy vbox -------------------------
		Image im = new Image(getClass().getResourceAsStream("/img/categoryPictures/"+e.getSportCategory().getId()+".png")); // naplnim obrazok so sportom
		this.title.setText(e.getEventName()); // pridat mu este css style ak bude treba
		img.setImage(im);
		img.setFitWidth(50);
	    img.setFitHeight(50);
	    title.setTextAlignment(TextAlignment.LEFT);
	    title.setAlignment(Pos.CENTER_LEFT);
	    vboxPicWithTitle.getChildren().addAll(this.title,img);
	    vboxPicWithTitle.setAlignment(Pos.CENTER_LEFT);
	    vboxPicWithTitle.setMaxWidth(100);
	    vboxPicWithTitle.setMinWidth(100);
	    //------------------
	    //mozno by sa hodil prazdny
	    //-----druhy vbox---------
	    PropertyManager manager = new PropertyManager("");
	    manager.loadLanguageSet(getClass().getSimpleName());
	    buttonJoinedS = manager.getProperty("buttonJoined");
	    buttonJoinS = manager.getProperty("buttonJoin");
	    numberOfUsers.setText(manager.getProperty("numberOfUsers"));
	    
	    numberOfUsersValue.setText(joinedUser.size()+"/"+e.getMaxUsersOnEvent()); // dorob pocet uzivatelov na dany event
	    infoR1.getChildren().addAll(numberOfUsers,numberOfUsersValue);
	    date.setText(manager.getProperty("date"));
	    dateValue.setText(e.getDate().toString());
	    infoR2.getChildren().addAll(date,dateValue);
	    address.setText(manager.getProperty("address"));
	    valueOfAddress.setText(e.getAddress());
	    infoR3.getChildren().addAll(address,valueOfAddress);
	    
	    init();
	    
		if(joinedUser.contains(Dataset.getInstance().getLoggedIn())) {
 			buttonJoin.setText(manager.getProperty("buttonJoined"));
 			buttonJoin.addEventHandler(MouseEvent.MOUSE_CLICKED, leave);
 		}
 		else{
 			String buttontext = manager.getProperty("buttonJoin");
 			//System.out.println(buttontext+"---");
 			buttonJoin.addEventHandler(MouseEvent.MOUSE_CLICKED, join);
 			buttonJoin.setText(buttontext);
 		}
	    
	    
	    
	    info.getChildren().addAll(infoR1,infoR2,infoR3);
	    // treti vbox
		detailBt.getStyleClass().add("buttonDetail");
	    buttonJoin.getStyleClass().add("buttonJoin");
	    Pane p = new Pane();
	    p.setPrefHeight(5);
	    vboxBt.getChildren().addAll(buttonJoin,p,detailBt);
	    Pane space = new Pane();
	    space.setPrefWidth(30);
	    Pane space2 = new Pane();
	    space.setMaxWidth(Double.MAX_VALUE);
	    EventPaneComponent.setHgrow(space2, Priority.ALWAYS);
	    vboxBt.setAlignment(Pos.CENTER_RIGHT);
	    vboxBt.setMaxWidth(95);
	    vboxBt.setMinWidth(95);
	    //this.setAlignment(Pos.CENTER_RIGHT);
	    this.getChildren().addAll(vboxPicWithTitle,space,info,space2 ,vboxBt);
     }
    
	private void refillJoinedUser() {
		joinedUser = new ArrayList<>();
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
			RestTemplateFactory factory = context.getBean(RestTemplateFactory.class);
			RestTemplate template = factory.getObject();
			String ip = new PropertyManager("src/main/resources/connectionConfig").getProperty("host");
			String port = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("port");

			String url = "http://"+ip+":"+port+"/events/" + event.getEventId() + "/users";
			ResponseEntity<List<User>> returnedEntity = template.exchange(url, HttpMethod.GET, null,new ParameterizedTypeReference<List<User>>() {});
			for(User q: returnedEntity.getBody()) {
				joinedUser.add(q);
			}
			((ConfigurableApplicationContext)context).close();
		}
		catch(RestClientException p){
			p.printStackTrace();
			return;
		}
	}
}
