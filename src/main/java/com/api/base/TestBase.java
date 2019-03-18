package com.api.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class TestBase {
	
	protected int RESPONSE_STATUS_CODE_200 = 200;
	protected int RESPONSE_STATUS_CODE_500 = 500;
	protected int RESPONSE_STATUS_CODE_400 = 400;
	protected int RESPONSE_STATUS_CODE_404 = 404;
	protected int RESPONSE_STATUS_CODE_401 = 401;
	protected int RESPONSE_STATUS_CODE_201 = 201;

	protected Properties prop;
	
	public TestBase(){
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String randomNumber() {
		Random random =new Random();
		String num=String.valueOf(random.nextInt(100000));
		return num;
	}
}
