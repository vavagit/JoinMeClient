package vava.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import vava.app.model.communication.RestTemplateFactory;

@Configuration
public class Config {
	
	@Bean
	public RestTemplateFactory getCommunicationManager() {
		return new RestTemplateFactory();
	}
}
