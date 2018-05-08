package vava.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vava.app.model.communication.RestTemplateFactory;

/**
 * Konfiguracna trieda spring
 * @author erikubuntu
 *
 */
@Configuration
public class Config {
	
	@Bean
	public RestTemplateFactory getCommunicationManager() {
		return new RestTemplateFactory();
	}
}
