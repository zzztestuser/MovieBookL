package com.moviebook.bean;

import java.time.LocalDateTime;

public class UserBean extends JsonBean {

	// Password is not retrieved

	private int id;
	private String email;
	private String name;
	private String profilePhotoPath;
	private LocalDateTime creationDateTime;
	private LocalDateTime modificationDateTime;

	public UserBean() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
