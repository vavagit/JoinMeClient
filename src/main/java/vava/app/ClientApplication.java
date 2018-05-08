package vava.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import vava.app.components.GmComponent;

public class ClientApplication extends Application{
	private static Logger logger = LogManager.getLogger(ClientApplication.class);
	
	@Override
	public void start(final Stage stage) throws Exception {
		GmComponent.getInstance();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/app/views/Login.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
        stage.setTitle("JoinMe");
        stage.setResizable(false);
        //nastavenie ikony
        Image image = new Image(getClass().getResourceAsStream("/img/titleIco.jpg"));
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    
	}

	public static void main(String[] args) {
		logger.info("Aplication start");
		launch(args);
	}
}
