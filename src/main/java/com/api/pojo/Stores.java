package com.api.pojo;

public record Stores(String name,
                     String address,
                     String city,
                     String state,
                     String zip,
                     String id,
                     String createdAt,
                     String updatedAt)
{

    public Stores(String name, String address, String city, String state, String zip) {
        this(name, address, city, state, zip, "", "", "");
    }
}
