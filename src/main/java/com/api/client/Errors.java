package com.api.client;

public interface Errors {

    String[] categories = {
        "should have required property 'name'",
        "should have required property 'id'"
    };
    String[] products = {
        "should have required property 'name'",
        "should have required property 'type'",
        "should have required property 'upc'",
        "should have required property 'description'",
        "should have required property 'model'"
    };
    String[] stores = {
        "should have required property 'name'",
        "should have required property 'address'",
        "should have required property 'city'",
        "should have required property 'state'",
        "should have required property 'zip'"
    };
    String[] services = {"should have required property 'name'"};
}
