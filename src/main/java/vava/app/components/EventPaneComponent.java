package vava.app.components;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vava.app.PropertyManager;
import vava.app.controllers.EventDescriptionController;
import vava.app.model.Event;

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
    
     private void init() {
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
     }
     
    public EventPaneComponent(Event e) {
		super();
		init();
		//init
		event = e;
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
	    vboxPicWithTitle.getChildren().addAll(this.title,img);
	    //------------------
	    //mozno by sa hodil prazdny
	    //-----druhy vbox---------
	    PropertyManager manager = new PropertyManager("");
	    manager.loadLanguageSet(getClass().getSimpleName());
	    numberOfUsers.setText(manager.getProperty("numberOfUsers"));
	    numberOfUsersValue.setText("0/"+e.getMaxUsersOnEvent()); // dorob pocet uzivatelov na dany event
	    infoR1.getChildren().addAll(numberOfUsers,numberOfUsersValue);
	    date.setText(manager.getProperty("date"));
	    dateValue.setText(e.getDate().toString());
	    infoR2.getChildren().addAll(date,dateValue);
	    address.setText(manager.getProperty("address"));
	    valueOfAddress.setText(e.getAddress());
	    infoR3.getChildren().addAll(address,valueOfAddress);
	    
	    info.getChildren().addAll(infoR1,infoR2,infoR3);
	    
	    // treti vbox
		detailBt.getStyleClass().add("buttonDetail");
	    buttonJoin.getStyleClass().add("buttonJoin");
	    Pane p = new Pane();
	    p.setPrefHeight(5);
	    buttonJoin.setText("Join");
	    vboxBt.getChildren().addAll(buttonJoin,p,detailBt);
	    Pane space = new Pane();
	    space.setPrefWidth(30);
	    Pane space2 = new Pane();
	    space.setMaxWidth(Double.MAX_VALUE);
	    EventPaneComponent.setHgrow(space2, Priority.ALWAYS);
	    vboxBt.setAlignment(Pos.CENTER_RIGHT);
	    this.setAlignment(Pos.CENTER_RIGHT);
	    this.getChildren().addAll(vboxPicWithTitle,space,info,space2 ,vboxBt);
     }
}
