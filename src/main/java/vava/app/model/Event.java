package vava.app.model;

import java.sql.Date;

public class Event {
	private int eventId;
	private int maxUsersOnEvent;
	private String eventName;
	private String description;
	private Date date;
	private int necessaryAge;
	private int creatorId;
	private SportCategory sportCategory;
	private String address;
	private Location eventLocation;
	
	public Event(int eventId, int maxUsersOnEvent, String eventName, String description, Date date, int necessaryAge,
			int creatorId, SportCategory sportCategory, String address, Location eventLocation) {
		super();
		this.eventId = eventId;
		this.maxUsersOnEvent = maxUsersOnEvent;
		this.eventName = eventName;
		this.description = description;
		this.date = date;
		this.necessaryAge = necessaryAge;
		this.creatorId = creatorId;
		this.sportCategory = sportCategory;
		this.address = address;
		this.eventLocation = eventLocation;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getMaxUsersOnEvent() {
		return maxUsersOnEvent;
	}

	public void setMaxUsersOnEvent(int maxUsersOnEvent) {
		this.maxUsersOnEvent = maxUsersOnEvent;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getNecessaryAge() {
		return necessaryAge;
	}

	public void setNecessaryAge(int necessaryAge) {
		this.necessaryAge = necessaryAge;
	}

	public int getCreatorId() {
		return creatorId;
	}

	public void setCreator(int creatorId) {
		this.creatorId = creatorId;
	}

	public SportCategory getSportCategory() {
		return sportCategory;
	}

	public void setSportCategory(SportCategory sportCategory) {
		this.sportCategory = sportCategory;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Location getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(Location eventLocation) {
		this.eventLocation = eventLocation;
	}

	@Override
	public String toString() {
		return "Event [eventId=" + eventId + ", maxUsersOnEvent=" + maxUsersOnEvent + ", eventName=" + eventName
				+ ", description=" + description + ", date=" + date + ", necessaryAge=" + necessaryAge + ", creatorId="
				+ creatorId + ", sportCategory=" + sportCategory + ", address=" + address + ", eventLocation="
				+ eventLocation + "]";
	}
	
	
}
