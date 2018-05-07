package vava.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
	
	/**
	 * Vratenie hodnoty nahranej zo suboru
	 * @param key kluc podla ktoreho sa vyhladava
	 * @return emtpy string ak sa nenachadza inak hodnota
	 */
	public String getProperty(String key) {
		String value = properties.getProperty(key, "");
		try {
			byte[] transformed = value.getBytes("UTF16");
			return new String(transformed, "UTF16");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
}
