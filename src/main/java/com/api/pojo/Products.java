package com.api.pojo;

public record Products(String name,
                       String description,
                       String upc,
                       String type,
                       String model,
                       String id,
                       String createdAt,
                       String updatedAt)
{

    public Products(
        String name,
        String description,
        String upc,
        String type,
        String model
    ) {
        this(name, description, upc, type, model, "", "", "");
    }
}
