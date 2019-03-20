package com.api.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class TestBase {

	protected Properties prop;
	
	public TestBase() throws IOException {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(this.getClass().getResource("/config.properties").getFile());
			prop.load(ip);
	}
	
	public String randomNumber() {
		Random random =new Random();
		String num=String.valueOf(random.nextInt(100000));
		return num;
	}
}
