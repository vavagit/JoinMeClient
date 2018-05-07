package vava.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.springframework.stereotype.Component;

@Component
public class PropertyManager {
	
	private Properties properties = new Properties();
	
	public PropertyManager(String path) {
		properties = loadFromFile(path);
	}
	
	private Properties loadFromFile(String path) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(path)));
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	/**
	 * Vratenie hodnoty nahranej zo suboru
	 * @param key kluc podla ktoreho sa vyhladava
	 * @return emtpy string ak sa nenachadza inak hodnota
	 */
	public String getProperty(String key) {
		return properties.getProperty(key, "");
	}
}
