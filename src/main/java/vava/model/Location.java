package vava.model;

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

	public Location(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}

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
}
