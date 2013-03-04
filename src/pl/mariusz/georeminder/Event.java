package pl.mariusz.georeminder;

import java.util.Date;

import android.location.Location;

public class Event {

	private int id;
	private String name;
	private Date date;
	private Location location;
	private String description;
	
	public Event() {
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
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}

	public boolean validate() {
		if(this.name == null) return false;
		if(this.date == null) return false;
		//if(this.location == null) return false;
		if(this.description == null) return false;
		
		return true;
	}
}
