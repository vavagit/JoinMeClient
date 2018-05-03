package vava.app.model;

public class SportCategory {
	private int id;
	private String sport;
	
	public SportCategory(int id, String sport) {
		this.id = id;
		this.sport = sport;
	}
	
	public SportCategory() {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	
	
}
