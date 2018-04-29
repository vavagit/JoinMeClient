package vava.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.Animation;
import com.lynden.gmapsfx.javascript.object.DirectionsPane;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.LatLongBounds;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.service.directions.DirectionStatus;
import com.lynden.gmapsfx.service.directions.DirectionsRenderer;
import com.lynden.gmapsfx.service.directions.DirectionsResult;
import com.lynden.gmapsfx.service.directions.DirectionsServiceCallback;
import com.lynden.gmapsfx.service.elevation.ElevationResult;
import com.lynden.gmapsfx.service.elevation.ElevationServiceCallback;
import com.lynden.gmapsfx.service.elevation.ElevationStatus;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import com.lynden.gmapsfx.service.geocoding.GeocodingServiceCallback;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import vava.components.GmComponent;
import vava.controllers.LoginController;
import vava.model.Event;

/**
 * Example Application for creating and loading a GoogleMap into a JavaFX
 * application
 *
 * @author Rob Terpilowski
 */
public class ClientApplication extends Application{


	
	@Override
	public void start(final Stage stage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/views/Login.fxml"));
		AnchorPane root;
		root = loader.load();
		LoginController lg = loader.getController();
		//root.getChildrenUnmodifiable().add(new GmComponent(stage).mapComponent);
		Scene scene = new Scene(root);
		
        stage.setTitle("FXML Welcome");
        ;
        stage.setScene(scene);
        stage.show();
		
	
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
		launch(args);
		
		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		RestTemplate restTemplate = context.getBean(RestTemplate.class);
		ResponseEntity<List<Event>> rateResponse =
		        restTemplate.exchange("http://192.168.0.8:8080/events",
		                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {
		            });
		Boolean temp = restTemplate.postForObject("http://192.168.0.8:8080/events/",new Event(), Boolean.class);
		System.out.println(temp);
		List<Event> rates = rateResponse.getBody();
		for(Event e : rates)
			System.out.println(e.getEventId());
	}
	
	
	
	


}
