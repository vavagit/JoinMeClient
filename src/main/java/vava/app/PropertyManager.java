package vava.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Level;

/**
 * Nastroj na pracu s properties subormi.
 * @author erikubuntu
 *
 */
@Component
public class PropertyManager {
	
	private Logger logger = LogManager.getLogger(PropertyManager.class);
	private Properties properties = new Properties();
	
	public PropertyManager(String path) {
		properties = loadFromFile(path);
	}
	
	private Properties loadFromFile(String path) {
		Properties properties = new Properties();
		logger.debug("loadFromFile, Subor: " + path);
		try {
			properties.load(new FileInputStream(path));
			logger.debug("loadFromFile, data uspesne nacitane");
		}
		catch(FileNotFoundException e) {
			logger.debug(Level.WARN, e);
		}
		catch (IOException e) {
			logger.debug(Level.WARN, e);
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
			logger.debug(Level.WARN, e);
			return "";
		}
	}
	
	/**
	 * Nacitanie properties suboru obsahujuci jazyk podla nastavenia pocitaca
	 * @param c Class triedy pre ktoru bude hladany jazykovy subor.
	 * @return kodove oznacenie pouziteho jazyka
	 */
	public String loadLanguageSet(Class<?> c) {
		String computerLanguage = Locale.getDefault().getLanguage();
		logger.debug("loadLanguageSet, default jazyk : " + computerLanguage);
		
		File file = new File("src/main/resources/language/" + c.getSimpleName().replaceAll("Controller", "") + "_" + computerLanguage);
		if(file.exists()) {
			logger.debug("loadLanguageSet, Subor s default jazykom najdeny " + c.getSimpleName().replaceAll("Controller", "") + "_" + computerLanguage);
			properties = loadFromFile(file.getPath());
			return computerLanguage;
		}
		else {
			logger.debug("loadLanguageSet, Subor s default jazykom neexistuje pouzivam en" + c.getSimpleName().replaceAll("Controller", "") + "_en");
			file = new File("src/main/resources/language/" + c.getSimpleName().replaceAll("Controller", "") + "_en");
			properties = loadFromFile(file.getPath());
			return "en";
		}
	}
	
	/**
	 * Nacitanie properties suboru obsahujuci jazyk podla nastavenia pocitaca
	 * @param fileName nazov triedy pre ktoru bude hladany jazykovy subor.
	 * @return kodove oznacenie pouziteho jazyka
	 */
	public String loadLanguageSet(String fileName) {
		String computerLanguage = Locale.getDefault().getLanguage();
		logger.debug("loadLanguageSet, default jazyk : " + computerLanguage);

		File file = new File("src/main/resources/language/" + fileName + "_" + computerLanguage);
		if(file.exists()) {
			logger.debug("loadLanguageSet, Subor s default jazykom najdeny " + file.getPath() + "_" + computerLanguage);
			properties = loadFromFile(file.getPath());
			return computerLanguage;
		}
		else {
			logger.debug("loadLanguageSet, Subor s default jazykom neexistuje pouzivam en" + file.getPath() + "_en");
			file = new File("src/main/resources/language/" + fileName + "_en");
			properties = loadFromFile(file.getPath());
			return "en";
		}
	}
}
