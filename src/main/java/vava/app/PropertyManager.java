package vava.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
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
			properties.load(new FileInputStream(path));
		}
		catch(FileNotFoundException e) {
			//e.printStackTrace();
		}
		catch (IOException e) {
			//e.printStackTrace();
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
	
	public String loadLanguageSet(Class<?> c) {
		String computer_language = Locale.getDefault().getLanguage();
		
		File file = new File("src/main/resources/language/" + c.getSimpleName().replaceAll("Controller", "") + "_" + computer_language);
		if(file.exists()) {
			properties = loadFromFile(file.getPath());
			return computer_language;
		}
		else {
			file = new File("src/main/resources/language/" + c.getSimpleName().replaceAll("Controller", "") + "_en");
			properties = loadFromFile(file.getPath());
			return "en";
		}
	}
	
	public String loadLanguageSet(String fileName) {
		String computer_language = Locale.getDefault().getLanguage();
		
		File file = new File("src/main/resources/language/" + fileName + "_" + computer_language);
		if(file.exists()) {
			properties = loadFromFile(file.getPath());
			return computer_language;
		}
		else {
			file = new File("src/main/resources/language/" + fileName + "_en");
			properties = loadFromFile(file.getPath());
			return "en";
		}
	}
}
