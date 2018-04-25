package vava.model;

import java.util.ArrayList;
import java.util.List;

public class Event {
	private int eventId;
	private int maxUsersOnEvent;
	private String eventName;
	private User creator;
	private SportCategory sportCategory;
	private Location eventLocation;
	private List<User> acceptedUsers;
	private List<User> applyingUsers;
	
	public Event() {
		acceptedUsers = new ArrayList<User>();
		applyingUsers = new ArrayList<User>();
	}
	
	public Event(int id, int maxUsers, String name, User creator, SportCategory category, Location location) {
		this();
		setId(id);
		setMaxUsersOnEvent(maxUsers);
		setEventName(name);
		setCreator(creator);
		setSportCategory(category);
		setEventLocation(location);
	}
	
	public boolean isFull() {
		return maxUsersOnEvent == acceptedUsers.size();
	}
	
	public int getId() {
		return eventId;
	}
	
	public void setId(int id) {
		this.eventId = id;
	}
	
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public SportCategory getSportCategory() {
		return sportCategory;
	}

	public void setSportCategory(SportCategory sportCategory) {
		this.sportCategory = sportCategory;
	}

	public Location getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(Location eventLocation) {
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

	public List<User> getAcceptedUsers() {
		return acceptedUsers;
	}

	public List<User> getApplyingUsers() {
		return applyingUsers;
	}
	
	public void AddAcceptedUser(User user) {
		acceptedUsers.add(user);
	}
	
	public void AddApplyingUser(User user) {
		applyingUsers.add(user);
	}
}
