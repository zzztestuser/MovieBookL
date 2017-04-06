package com.moviebook.bean.user;

import java.time.LocalDateTime;

import com.google.gson.Gson;

public class User implements UserBeanFull, UserBeanLight {

	// Password is not retrieved

	private int id;
	private String email;
	private String name;
	private String profilePhotoPath;
	private LocalDateTime creationDateTime;
	private LocalDateTime modificationDateTime;

	public User() {

	}

	public User(int id, String email, String name, String profilePhotoPath, LocalDateTime creationDateTime, LocalDateTime modificationDateTime) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.profilePhotoPath = profilePhotoPath;
		this.creationDateTime = creationDateTime;
		this.modificationDateTime = modificationDateTime;
	}

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getProfilePhotoPath() {
		return profilePhotoPath;
	}

	public void setProfilePhotoPath(String profilePhotoPath) {
		this.profilePhotoPath = profilePhotoPath;
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

}
