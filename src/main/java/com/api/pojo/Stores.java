package com.api.pojo;

public class Stores {

	private String name;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String id;
	private String createdAt;
	private String updatedAt;
	
	public Stores() {}
	
	public Stores(String name, String address, String city, String state, String zip) {
		this.name=name;
		this.address=address;
		this.city=city;
		this.state=state;
		this.zip=zip;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setAddress(String address) {
		this.address=address;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setCity(String city) {
		this.city=city;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setState(String state) {
		this.state=state;
	}
	
	public String getState() {
		return state;
	}
	
	public void setZip(String zip) {
		this.zip=zip;
	}
	
	public String getZip() {
		return zip;
	}
	
	public void setId(String id) {
		this.id=id;
	}
	
	public String getId() {
		return id;
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
}
