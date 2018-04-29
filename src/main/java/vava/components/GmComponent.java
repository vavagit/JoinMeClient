package vava.components;

import java.util.Locale;

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
import com.lynden.gmapsfx.service.directions.DirectionsResult;
import com.lynden.gmapsfx.service.directions.DirectionsServiceCallback;
import com.lynden.gmapsfx.service.elevation.ElevationResult;
import com.lynden.gmapsfx.service.elevation.ElevationServiceCallback;
import com.lynden.gmapsfx.service.elevation.ElevationStatus;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import com.lynden.gmapsfx.service.geocoding.GeocodingServiceCallback;

import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class GmComponent implements MapComponentInitializedListener, ElevationServiceCallback,
GeocodingServiceCallback, DirectionsServiceCallback {

	public GoogleMapView mapComponent;
	public GoogleMap map;
	protected DirectionsPane directions;
	private MarkerOptions markerOptions2;
	private Marker myMarker2;

	public void elevationsReceived(ElevationResult[] results, ElevationStatus status) {
		if (status.equals(ElevationStatus.OK)) {
			for (ElevationResult e : results) {
				System.out.println(" Elevation on " + e.getLocation().toString() + " is " + e.getElevation());
			}
		}
	}

	
	public void geocodedResultsReceived(GeocodingResult[] results, GeocoderStatus status) {
		if (status.equals(GeocoderStatus.OK)) {
			for (GeocodingResult e : results) {
				System.out.println(e.getVariableName());
				System.out.println("GEOCODE: " + e.getFormattedAddress() + "\n" + e.toString());
			}

		}

	}

	public GmComponent(Stage s) {
		mapComponent = new GoogleMapView(Locale.getDefault().getLanguage(), null);
		
		mapComponent.addMapInitializedListener(this);
	}
	
	public void directionsReceived(DirectionsResult results, DirectionStatus status) {
		if (status.equals(DirectionStatus.OK)) {
			mapComponent.getMap().showDirectionsPane();
			System.out.println("OK");

			DirectionsResult e = results;
			GeocodingService gs = new GeocodingService();

			System.out.println("SIZE ROUTES: " + e.getRoutes().size() + "\n" + "ORIGIN: "
					+ e.getRoutes().get(0).getLegs().get(0).getStartLocation());
			// gs.reverseGeocode(e.getRoutes().get(0).getLegs().get(0).getStartLocation().getLatitude(),
			// e.getRoutes().get(0).getLegs().get(0).getStartLocation().getLongitude(),
			// this);
			System.out.println("LEGS SIZE: " + e.getRoutes().get(0).getLegs().size());
			System.out.println("WAYPOINTS " + e.getGeocodedWaypoints().size());
			/*
			 * double d = 0; for(DirectionsLeg g : e.getRoutes().get(0).getLegs()){ d +=
			 * g.getDistance().getValue(); System.out.println("DISTANCE " +
			 * g.getDistance().getValue()); }
			 */
			try {
				System.out
						.println("Distancia total = " + e.getRoutes().get(0).getLegs().get(0).getDistance().getText());
			} catch (Exception ex) {
				System.out.println("ERRO: " + ex.getMessage());
			}
			System.out.println("LEG(0)");
			System.out.println(e.getRoutes().get(0).getLegs().get(0).getSteps().size());
			/*
			 * for(DirectionsSteps ds : e.getRoutes().get(0).getLegs().get(0).getSteps()){
			 * System.out.println(ds.getStartLocation().toString() + " x " +
			 * ds.getEndLocation().toString()); MarkerOptions markerOptions = new
			 * MarkerOptions(); markerOptions.position(ds.getStartLocation())
			 * .title(ds.getInstructions()) .animation(Animation.DROP) .visible(true);
			 * Marker myMarker = new Marker(markerOptions); map.addMarker(myMarker); }
			 */
		}
	}
	
	@Override
	public void mapInitialized() {

		// System.out.println("MainApp.mapInitialised....");

		// Once the map has been loaded by the Webview, initialize the map details.
		LatLong center = new LatLong(47.606189, -122.335842);
		mapComponent.addMapReadyListener(() -> {
			// This call will fail unless the map is completely ready.
			checkCenter(center);
		});

		MapOptions options = new MapOptions();
		options.center(center).mapMarker(true).zoom(9).overviewMapControl(false).keyboardShortcuts(true)
				.panControl(false).rotateControl(false).scaleControl(false).streetViewControl(false).zoomControl(true)
				.mapType(MapTypeIdEnum.ROADMAP).clickableIcons(false).disableDefaultUI(true)
				.disableDoubleClickZoom(true);

		map = mapComponent.createMap(options, false);
		directions = mapComponent.getDirec();
		mapComponent.setPrefSize(300, 300);
		map.setHeading(123.2);
		
//	        System.out.println("Heading is: " + map.getHeading() );

		MarkerOptions markerOptions = new MarkerOptions();
		LatLong markerLatLong = new LatLong(47.606189, -122.335842);
		markerOptions.position(markerLatLong).title("My new Marker").icon("mymarker.png").animation(Animation.DROP)
				.visible(true);

		final Marker myMarker = new Marker(markerOptions);

		map.fitBounds(new LatLongBounds(new LatLong(40, 120), center)); // vytiahne taku mapu aby sa vosla do okna
//	        System.out.println("Bounds : " + map.getBounds());

		// liseneer ktory vrati suradnice po kliknuti
		map.addUIEventHandler(UIEventType.click, (JSObject obj) -> { // liseneer ktory vrati suradnice po kliknuti
			LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
			System.out.println("LatLong: lat: " + ll.getLatitude() + " lng: " + ll.getLongitude());
			// lblClick.setText(ll.toString());
		});

		map.addUIEventHandler(UIEventType.dblclick, (JSObject obj) -> { // liseneer ktory vrati suradnice po kliknuti
			LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
			if (myMarker2 == null) {
				markerOptions2 = new MarkerOptions();
				markerOptions2.position(ll).title("My new Marker").visible(true).draggable(true);
				myMarker2 = new Marker(markerOptions2);
				map.addMarker(myMarker2);
			} else {
				myMarker2.setPosition(ll);
			}

		});
	}

	private void checkCenter(LatLong center) {
//	        System.out.println("Testing fromLatLngToPoint using: " + center);
//	        Point2D p = map.fromLatLngToPoint(center);
//	        System.out.println("Testing fromLatLngToPoint result: " + p);
//	        System.out.println("Testing fromLatLngToPoint expected: " + mapComponent.getWidth()/2 + ", " + mapComponent.getHeight()/2);
	}


}
