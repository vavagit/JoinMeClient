package vava.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.springframework.stereotype.Component;

@Component
public class PropertyManager {
	private static final String CONFIG_PATH = "src/main/resources/props";
	
	private Properties properties = new Properties();
	
	public PropertyManager() {
		properties = loadFromDir();
	}
	
	private Properties loadFromDir() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(CONFIG_PATH)));
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
