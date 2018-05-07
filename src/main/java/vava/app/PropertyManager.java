package vava.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.stereotype.Component;

@Component
public class PropertyManager {
	
	private Properties properties = new Properties();
	
	public PropertyManager(InputStream path) {
		properties = loadFromFile(path);
	}
	
	private Properties loadFromFile(InputStream path) {
		Properties properties = new Properties();
		try {
			properties.load(path);
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key, "");
	}
}
