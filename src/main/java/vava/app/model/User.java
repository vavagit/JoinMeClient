package vava.app.model;

import java.sql.Date;

public class User {
	private int id;
	private String userName;
	private String password;
	private Date registeredAt;
	private String name;
	private String lastName;
	private float rating;
	private String contact;
	private String address;
	private Location addressLocation;

	public User(String userName, String password, Date registered_at, String name, String lastName,
			float rating, String contact, String address, Location addressLocation) {
		this.userName = userName;
		this.password = password;
		this.registeredAt = registered_at;
		this.name = name;
		this.lastName = lastName;
		this.rating = rating;
		this.contact = contact;
		this.address = address;
		this.addressLocation = addressLocation;
	}
	
	public User(String username, String password) {
		this.userName = username;
		this.password = password;
	}
	
	public User() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(Date registered_at) {
		this.registeredAt = registered_at;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Location getAddressLocation() {
		return addressLocation;
	}

	public void setAddressLocation(Location addressLocation) {
		this.addressLocation = addressLocation;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", registered_at="
				+ registeredAt + ", name=" + name + ", lastName=" + lastName + ", rating=" + rating + ", contact="
				+ contact + ", address=" + address + ", addressLocation=" + addressLocation + "]";
	}

}
