package vava.app.components;

import java.util.List;
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
	public GeocodingService gs = new GeocodingService();
	protected DirectionsPane directions;
	private MarkerOptions markerOptions2;
	private Marker myMarker2;
	public LatLong fromGeocode;
	
	@Override
	public void elevationsReceived(ElevationResult[] results, ElevationStatus status) {
		if (status.equals(ElevationStatus.OK)) {
			for (ElevationResult e : results) {
				System.out.println(" Elevation on " + e.getLocation().toString() + " is " + e.getElevation());
			}
		}
	}

	@Override
	public void geocodedResultsReceived(GeocodingResult[] results, GeocoderStatus status) {
		LatLong temp = null;
		if (status.equals(GeocoderStatus.OK)) {
			for (GeocodingResult e : results) {
				temp = e.getGeometry().getLocation();
				//System.out.println(e.getGeometry().getLocation());
				//System.out.println("GEOCODE: " + e.getFormattedAddress() + "\n" + e.toString());
			}
			fromGeocode = temp;
		}
		else {
			fromGeocode = null;
		}
		/*MarkerOptions mo = new MarkerOptions();
		mo.position(fromGeocode).visible(true);
		Marker newMarker = new Marker(mo);
		map.clearMarkers();
		map.addMarker(newMarker);
		map.setCenter(fromGeocode);
		map.setZoom(13);*/
		

	}

	public GmComponent(Stage s) {
		mapComponent = new GoogleMapView(Locale.getDefault().getLanguage(), null);
		//gs = new GeocodingService();
		mapComponent.addMapInitializedListener(this);
	}
	
	public void directionsReceived(DirectionsResult results, DirectionStatus status) {
		if (status.equals(DirectionStatus.OK)) {
			mapComponent.getMap().showDirectionsPane();
			System.out.println("OK");

			DirectionsResult e = results;
			

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
	//	gs = new GeocodingService();
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
			map.clearMarkers();
				markerOptions2 = new MarkerOptions();
				markerOptions2.position(ll).title("My new Marker").visible(true).draggable(true);
				myMarker2 = new Marker(markerOptions2);
				map.addMarker(myMarker2);
			

		});
		System.out.println("skus");
		//gs=new GeocodingService();
	}

	private void checkCenter(LatLong center) {
//	        System.out.println("Testing fromLatLngToPoint using: " + center);
//	        Point2D p = map.fromLatLngToPoint(center);
//	        System.out.println("Testing fromLatLngToPoint result: " + p);
//	        System.out.println("Testing fromLatLngToPoint expected: " + mapComponent.getWidth()/2 + ", " + mapComponent.getHeight()/2);
	}
	public LatLong geocodingAddress(String place) {
		//GeocodingService gs = new GeocodingService();
		fromGeocode = null;
		gs.geocode(place, this);
		return fromGeocode;
		
		
	}


}
