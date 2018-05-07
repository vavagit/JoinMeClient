package vava.app.model;

public class Dataset {
	private static Dataset set = new Dataset();
	
	private User loggedIn;
	
	private Dataset() { 
		setLoggedIn(null);
	}
	
	public static Dataset getInstance() {
		return set;
	}

	public User getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(User loggedIn) {
		this.loggedIn = loggedIn;
	}
}
