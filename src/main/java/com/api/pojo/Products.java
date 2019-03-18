package com.api.pojo;

public class Products {

	private String name;
	private String description;
	private String upc;
	private String type;
	private String model;
	
	public Products(String name, String description, String upc, String type, String model) {
		this.name=name;
		this.description=description;
		this.upc=upc;
		this.type=type;
		this.model=model;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDescription(String description) {
		this.description=description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setUpc(String upc) {
		this.upc=upc;
	}
	
	public String getUpc() {
		return upc;
	}
	
	public void setType(String type) {
		this.type=type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setModel(String model) {
		this.model=model;
	}
	
	public String getModel() {
		return model;
	}
}
