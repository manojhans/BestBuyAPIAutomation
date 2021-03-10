package com.api.pojo;

public record Services(String name,
                       String id,
                       String createdAt,
                       String updatedAt)
{

    public Services(String name) {
        this(name, "", "", "");
    }
}
