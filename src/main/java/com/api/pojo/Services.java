package com.api.pojo;

public class Services {
	
	private String name;
	private String id;
	private String createdAt;
	private String updatedAt;
	
	public Services() {}
	
	public Services(String name) {
		this.name=name;
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
