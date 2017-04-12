package com.moviebook.bean.user;

import java.util.List;

public class MovieBean {

	private int id;
	private String title;
	private String description;
	private String language;
	private int duration;
	private String posterSmallPath, posterMediumPath, posterLargePath;
	private List<String> genre;
	
	
	public MovieBean() {
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public String getPosterSmallPath() {
		return posterSmallPath;
	}


	public void setPosterSmallPath(String posterSmallPath) {
		this.posterSmallPath = posterSmallPath;
	}


	public String getPosterMediumPath() {
		return posterMediumPath;
	}


	public void setPosterMediumPath(String posterMediumPath) {
		this.posterMediumPath = posterMediumPath;
	}


	public String getPosterLargePath() {
		return posterLargePath;
	}


	public void setPosterLargePath(String posterLargePath) {
		this.posterLargePath = posterLargePath;
	}


	public List<String> getGenre() {
		return genre;
	}


	public void setGenre(List<String> genre) {
		this.genre = genre;
	}
	
}
