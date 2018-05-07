package vava.app.model;

/**
 * Datova trieda opisujuca polohu pomocou atributov latitude a longitude.
 * Atributy su pouzivane na konvertovanie na adresu pomocou Google APIs.
 * 
 * @author erikubuntu
 *
 */
public class Location {
	private double latitude;
	private double longitude;
	private final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

	public Location(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}

	public Location() {}
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "(" + latitude + "," + longitude + ")";
	}

	public static int calculateDistanceInKilometers(Location point1, Location point2) {

		double latDistance = Math.toRadians(point1.getLatitude() - point2.getLatitude());
		double lngDistance = Math.toRadians(point1.getLongitude() - point2.getLongitude());

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(point1.getLatitude()))
				* Math.cos(Math.toRadians(point2.getLatitude())) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return (int)(AVERAGE_RADIUS_OF_EARTH_KM * c);
	}
}
