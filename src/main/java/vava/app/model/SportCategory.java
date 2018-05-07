package vava.app.model;

public class SportCategory {
	private int id;
	private String sport_sk;
	private String sport_en;
	
	public SportCategory(int id, String sport_sk,String sport_en) {
		this.id = id;
		this.sport_sk = sport_sk;
		this.sport_en = sport_en;
	}
	
	public SportCategory() {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getSport_sk() {
		return sport_sk;
	}

	public void setSport_sk(String sport_sk) {
		this.sport_sk = sport_sk;
	}

	public String getSport_en() {
		return sport_en;
	}

	public void setSport_en(String sport_en) {
		this.sport_en = sport_en;
	}

	@Override
	public String toString() {
		return "SportCategory [id=" + id + ", sport_sk=" + sport_sk + ", sport_en=" + sport_en + "]";
	}
	
	
	
}
