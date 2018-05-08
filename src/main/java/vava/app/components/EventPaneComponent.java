package vava.app.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import vava.app.model.Event;

public class EventPaneComponent extends HBox{
	String labelNumberOfUsers = "Pocet uzivatelov: ";
	String labelDate = "Kedy ? : ";
	String labelAddress = "Kde ? : ";
     ImageView img = new ImageView();
     Label title = new Label();
     VBox vboxPicWithTitle = new VBox();
     //--------druhy stlpec
     VBox info = new VBox();
     HBox infoR1 = new HBox();
     HBox infoR2 = new HBox();
     HBox infoR3 = new HBox();
     Label date = new Label();
     Label dateValue = new Label();
     Label address = new Label();
     Label valueOfAddress = new Label();
     Label numberOfUsers = new Label();
     Label numberOfUsersValue = new Label();
     
     //---------------------------------------
     
     //-----------------treti stplec-----
     Button buttonJoin = new Button();
     Button detailBt = new Button();
     VBox vboxBt = new VBox();
    
    public EventPaneComponent(Event e) {
		super();
		//init
		title.getStyleClass().add("vbLabel");
		date.getStyleClass().add("vbLabel");
		address.getStyleClass().add("vbLabel");
		numberOfUsers.getStyleClass().add("vbLabel");
		dateValue.getStyleClass().addAll("vbLabel","vbValueLabel");
		valueOfAddress.getStyleClass().addAll("vbLabel","vbValueLabel");
		numberOfUsersValue.getStyleClass().addAll("vbLabel","vbValueLabel");
		// prvy vbox -------------------------
	//	System.out.println("/img/categoryPictures/"+e.getEventId()+".png");
		Image im = new Image(getClass().getResourceAsStream("/img/categoryPictures/"+e.getSportCategory().getId()+".png")); // naplnim obrazok so sportom
		this.title.setText(e.getEventName()); // pridat mu este css style ak bude treba
		img.setImage(im);
		img.setFitWidth(50);
	    img.setFitHeight(50);
	    vboxPicWithTitle.getChildren().addAll(this.title,img);
	    //------------------
	    //mozno by sa hodil prazdny
	    //-----druhy vbox---------
	    numberOfUsers.setText(labelNumberOfUsers);
	    numberOfUsersValue.setText("0/"+e.getMaxUsersOnEvent()); // dorob pocet uzivatelov na dany event
	    infoR1.getChildren().addAll(numberOfUsers,numberOfUsersValue);
	    date.setText(labelDate);
	    dateValue.setText(e.getDate().toString());
	    infoR2.getChildren().addAll(date,dateValue);
	    address.setText(labelAddress);
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
	    this.setHgrow(space2, Priority.ALWAYS);
	    vboxBt.setAlignment(Pos.CENTER_RIGHT);
	    this.setAlignment(Pos.CENTER_RIGHT);
	    this.getChildren().addAll(vboxPicWithTitle,space,info,space2 ,vboxBt);
     }
}
