package com.api.pojo;

public record Categories(String id,
                         String name,
                         String createdAt,
                         String updatedAt)
{

    public Categories(String id, String name) {
        this(id, name, "", "");
    }
}
