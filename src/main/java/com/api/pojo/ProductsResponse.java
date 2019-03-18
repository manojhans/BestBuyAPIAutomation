package com.api.pojo;

public class ProductsResponse {

	private String name;
	private String description;
	private String upc;
	private String type;
	private String model;
	private String id;
	private String createdAt;
	private String updatedAt;
	
	public ProductsResponse() {
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
