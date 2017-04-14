package com.moviebook.bean;

import java.time.LocalDateTime;
import java.util.List;

public class EventBean {

	private int id;
	private int createdByID;
	private String createdByName;
	private String eventName;
	private String eventDescription;
	private LocalDateTime creationDateTime;
	private LocalDateTime modificationDateTime;
	private int movieID;
	private int screeningID;
	private LocalDateTime screeningDateTime;
	private String theatreName;
	private String theatreLocation;
	private List<AttendeeBean> attendees;

	public EventBean() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCreatedByID() {
		return createdByID;
	}

	public void setCreatedByID(int createdByID) {
		this.createdByID = createdByID;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public LocalDateTime getModificationDateTime() {
		return modificationDateTime;
	}

	public void setModificationDateTime(LocalDateTime modificationDateTime) {
		this.modificationDateTime = modificationDateTime;
	}

	public int getMovieID() {
		return movieID;
	}

	public void setMovieID(int movieID) {
		this.movieID = movieID;
	}

	public int getScreeningID() {
		return screeningID;
	}

	public void setScreeningID(int screeningID) {
		this.screeningID = screeningID;
	}

	public LocalDateTime getScreeningDateTime() {
		return screeningDateTime;
	}

	public void setScreeningDateTime(LocalDateTime screeningDateTime) {
		this.screeningDateTime = screeningDateTime;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public String getTheatreLocation() {
		return theatreLocation;
	}

	public void setTheatreLocation(String theatreLocation) {
		this.theatreLocation = theatreLocation;
	}

	public List<AttendeeBean> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<AttendeeBean> attendees) {
		this.attendees = attendees;
	}

}
