package com.api.pojo;

public class ServicesResponse {
	
	private String name;
	private String id;
	private String createdAt;
	private String updatedAt;
	
	public ServicesResponse() {
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt=createdAt;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}
	
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt=updatedAt;
	}
	
	public String getUpdatedAt() {
		return updatedAt;
	}
	
	public void setId(String id) {
		this.id=id;
	}

	public String getId() {
		return id;
	}
}
