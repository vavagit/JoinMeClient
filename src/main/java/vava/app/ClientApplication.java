package vava.app;

import java.util.List;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.lynden.gmapsfx.service.geocoding.GeocodingResult;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import vava.app.components.GmComponent;

public class ClientApplication extends Application{
	@Override
	public void start(final Stage stage) throws Exception {
		GmComponent.getInstance();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/app/views/Login.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
        stage.setTitle("JoinMe");
        stage.setResizable(false);
        Image image = new Image(getClass().getResourceAsStream("/img/titleIco.jpg"));
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    
	}

	public static void main(String[] args) {
		System.out.println(Locale.getDefault().getLanguage());
		launch(args);
	}
}
