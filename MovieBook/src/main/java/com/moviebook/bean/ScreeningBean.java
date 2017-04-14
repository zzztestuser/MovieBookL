package com.moviebook.bean;

import java.time.LocalDateTime;

public class ScreeningBean {

	private int id;
	private String theatreName;
	private String theatreLocation;
	private LocalDateTime screeningDateTime;

	public ScreeningBean() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public LocalDateTime getScreeningDateTime() {
		return screeningDateTime;
	}

	public void setScreeningDateTime(LocalDateTime screeningDateTime) {
		this.screeningDateTime = screeningDateTime;
	}

}
