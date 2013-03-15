package pl.mariusz.georeminder;

public class Event {

	private int id;
	private String name;
	private long date;
	//private Location location;
	private double latitude;
	private double longitude;
	private String description;
	private boolean completed;
	
	public Event() {
	}
	
	public Event (String name, double latitude, double longitude, String description, long date) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.description = description;
		this.date = date;
	}
	
	public Event (int id, String name, double latitude, double longitude, String description, long date) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.description = description;
		this.date = date;
	}
	
	public Event (String name, double latitude, double longitude, String description, long date, boolean completed) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.description = description;
		this.date = date;
		this.completed = completed;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setDate(long date) {
		this.date = date;
	}
	
	public long getDate() {
		return this.date;
	}
	/*
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Location getLocation() {
		return this.location;
	}
	*/
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
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

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean validate() {
		//if(this.name == null) return false;
		//if(this.date == null) return false;
		//if(this.location == null) return false;
		//if(this.description == null) return false;
		
		return true;
	}
}
