package com.api.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import static java.lang.String.valueOf;

public class TestBase {

    protected Properties prop;

    public TestBase() {
        try {
            prop = new Properties();
            FileInputStream inputStream =
                new FileInputStream(this.getClass().getResource("/config.properties").getFile());
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String randomNumber() {
        return valueOf(new Random().nextInt(100000));
    }
}
