package vava.app.components;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
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
import vava.app.controllers.UserDetailsController;
import vava.app.model.User;

public class UserPaneComponent extends HBox{
	private User user;
	//prvy stplec fotka s labelom 
	ImageView img = new ImageView();
     Label title = new Label();
     VBox vboxPicWithTitle = new VBox();
     
     //druhy stlpec
     VBox infoName = new VBox();
     Label firstName = new Label();
     Label lastName = new Label();
     
     VBox infoR2 = new VBox();
     Label contact = new Label();
     Label address = new Label();
     //-----------------treti stplec-----
     Button detailUserBt = new Button();
    	  private void init() {
    	    	 detailUserBt.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    				@Override
    				public void handle(MouseEvent event) {
    					
    					Stage s =(Stage) detailUserBt.getScene().getWindow();
    					Stage newS = new Stage();
    					newS.initOwner(s);
    					//newS.setAlwaysOnTop(true);
    					newS.initModality(Modality.WINDOW_MODAL);
    					newS.setTitle("JoinMe - UserDetail");
    					Image image = new Image(getClass().getResourceAsStream("/img/titleIco.jpg"));
    			        newS.getIcons().add(image);
    					try {
    						FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/app/views/UserDetail.fxml"));
    						Parent root = loader.load();
    						Scene scene = new Scene(root);
    				        UserDetailsController ec = loader.getController();
    				        ec.fillUserObject(user);
    						newS.setScene(scene);
    				        newS.show();
    					} catch (IOException e) {
    						e.printStackTrace();
    						return;
    					}
    				}
    			});
    	     }
     
     
    public UserPaneComponent (User user) {
    	super();
    	this.user = user;
    	init();
    	
    	 Pane space2 = new Pane();
   	    space2.setMaxHeight(Double.MAX_VALUE);
    	//prvy stlpec
    	title.getStyleClass().add("vbLabel");
    	Image im = new Image(getClass().getResourceAsStream("/img/manIcon.png")); // naplnim obrazok so sportom
		this.title.setText(user.getUserName()); // pridat mu este css style ak bude treba
		img.setImage(im);
		img.setFitWidth(50);
	    img.setFitHeight(50);
	    vboxPicWithTitle.getChildren().addAll(this.title,img);
	    Pane space = new Pane();
	    space.setPrefWidth(10);
	    space.setMinWidth(10);
	    Pane space1 = new Pane();
	    space.setPrefWidth(10);
	    space1.setMinWidth(10);
	    //druhy stlpec
	    firstName.getStyleClass().add("vbLabel");
	    lastName.getStyleClass().add("vbLabel");
	    firstName.setText(user.getName());
	    lastName.setText(user.getLastName());
	    infoName.getChildren().addAll(firstName,lastName);
	    //treti stlpec
	    contact.getStyleClass().add("vbLabel");
	    address.getStyleClass().add("vbLabel");
	    contact.setText(user.getContact());
	    address.setText(user.getAddress());
	    infoR2.getChildren().addAll(contact,address);
	    detailUserBt.getStyleClass().add("buttonDetail");
	    this.setHgrow(space2, Priority.ALWAYS);
	    this.getChildren().addAll(vboxPicWithTitle,space,infoName,space1,infoR2,space2,detailUserBt);
	    
	    
	    
    }
	
}
