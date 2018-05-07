package vava.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.lynden.gmapsfx.service.directions.DirectionsRenderer;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import vava.app.components.GmComponent;
import vava.app.controllers.LoginController;


/**
 * Example Application for creating and loading a GoogleMap into a JavaFX
 * application
 *
 * @author Rob Terpilowski
 */
public class ClientApplication extends Application{


	
	@Override
	public void start(final Stage stage) throws Exception {
		GmComponent g = GmComponent.getInstance();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/views/Login.fxml"));
		AnchorPane root;
		root = loader.load();
		LoginController lg = loader.getController();
		//root.getChildrenUnmodifiable().add(new GmComponent(stage).mapComponent);
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("/vava/views/Log.css").toExternalForm());
        stage.setTitle("JoinMe");
        stage.setResizable(false);
        Image image = new Image(getClass().getResourceAsStream("/img/titleIco.jpg"));
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
        //GeocodingService gs = new GeocodingService();
	
        /*System.out.println("Java version: " + System.getProperty("java.home"));
		

		BorderPane bp = new BorderPane();
		bp.setCenter(mapComponent);

		Scene scene = new Scene(bp);
		stage.setScene(scene);
		stage.show();	*/
	}
	
	
	
	
	
	DirectionsRenderer renderer;

	private static List<HttpMessageConverter<?>> getMessageConverters() {
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new MappingJackson2HttpMessageConverter());
		return converters;
	}

	


	public static void main(String[] args) {
		//System.setProperty("java.net.useSystemProxies", "true");
		System.out.println("hello");
		
		System.out.println(Locale.getDefault().getCountry());
		ScriptEngineManager factory = new ScriptEngineManager();
		//System.out.println(g.geocodingAddress("Poprad"));
	    ScriptEngine engine = factory.getEngineByName("JavaScript");
	    try {
			engine.eval("print('Hello, World')");
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		launch(args);
		
		
	}
	
	
	
	


}
