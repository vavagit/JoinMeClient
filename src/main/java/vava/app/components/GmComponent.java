package vava.app.components;

import java.util.Locale;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import com.lynden.gmapsfx.service.geocoding.GeocodingServiceCallback;

import javafx.stage.Stage;
import netscape.javascript.JSObject;
import vava.app.controllers.CreateEventsController;
import vava.app.controllers.EditEventController;
import vava.app.controllers.MainViewController;
import vava.app.controllers.RegisterController;

public class GmComponent implements MapComponentInitializedListener,
GeocodingServiceCallback{

	public GoogleMapView mapComponent;
	public GoogleMap map;
	public GeocodingService gs;
	public LatLong fromGeocode;
	Object objectCTRL;
	private static GmComponent gm = null;
	
	//callback na geocodovanie nazvu podla suradnic
	@Override
	public void geocodedResultsReceived(GeocodingResult[] results, GeocoderStatus status) {
	//System.out.println(Thread.currentThread().getName()+"  "+Thread.currentThread().getId());
		LatLong temp = null;
		if (status.equals(GeocoderStatus.OK)) {
			for (GeocodingResult e : results) {
				temp = e.getGeometry().getLocation();
				//System.out.println("goeode result    "+e.getGeometry().getLocation());
				System.out.println("GEOCODE: " + e.getFormattedAddress() + "\n" + e.toString());
				fromGeocode = temp;
				break;
			}
			
			//System.out.println(fromGeocode);
		}
		
		if(objectCTRL instanceof CreateEventsController) {
			CreateEventsController q1 = (CreateEventsController)objectCTRL;
			q1.fillLongLitude(fromGeocode);
		}
		else if(objectCTRL instanceof MainViewController) {
			MainViewController q1 = (MainViewController)objectCTRL;
			q1.fillLongLitude(fromGeocode);
		}
		else if(objectCTRL instanceof RegisterController) {
			RegisterController q1 = (RegisterController)objectCTRL;
			q1.fillLongLitude(fromGeocode);
		}
		else if(objectCTRL instanceof EditEventController) {
			EditEventController q1 = (EditEventController)objectCTRL;
			q1.fillLongLitude(fromGeocode);
		}
	}
	public static GmComponent getInstance() {
		if(gm ==null) {
			gm = new GmComponent(null);
		}
		return gm;
	}

	private GmComponent(Stage s) {
		mapComponent = new GoogleMapView(Locale.getDefault().getLanguage(), null);
		//gs = new GeocodingService();
		mapComponent.addMapInitializedListener(this);
	}
		
	@Override
	public void mapInitialized() {


		MapOptions options = new MapOptions();
		options.mapMarker(true).zoom(9).overviewMapControl(false).keyboardShortcuts(true)
				.panControl(false).rotateControl(false).scaleControl(false).streetViewControl(false).zoomControl(true)
				.mapType(MapTypeIdEnum.ROADMAP).clickableIcons(false).disableDefaultUI(true)
				.disableDoubleClickZoom(true);

		map = mapComponent.createMap(options, false);
		//directions = mapComponent.getDirec();
		mapComponent.setPrefSize(300, 300);
		map.setHeading(123.2);
		
		map.addUIEventHandler(UIEventType.dblclick, (JSObject obj) -> { // liseneer ktory vrati suradnice po kliknuti
			LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
			fromGeocode = ll;
			map.clearMarkers();	
			if(objectCTRL instanceof CreateEventsController) {
				CreateEventsController q = (CreateEventsController)objectCTRL;
				q.fillLongLitude(fromGeocode);
			}
		});
		gs=new GeocodingService();
	}

	public void geocodingAddress(String place,Object e) {
		//GeocodingService gs = new GeocodingService();
		this.objectCTRL = e;
		gs.geocode(place, this);
		//System.out.println(Thread.currentThread().getName()+"  "+Thread.currentThread().getId());
		return;
	}
	public void refillLatLong(Object e) {
		objectCTRL = e;
	}

	public void reverseGeocoding(LatLong q) {
		
	}
}
