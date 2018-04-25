package vava.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import vava.model.communication.RestTemplateFactory;

@Configuration
public class Config {
	
	@Bean
	public RestTemplateFactory getCommunicationManager() {
		return new RestTemplateFactory();
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
		return getCommunicationManager().getObject();
	}
}
