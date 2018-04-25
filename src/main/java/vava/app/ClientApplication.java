package vava.app;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import vava.model.Event;
import vava.model.communication.RestTemplateFactory;

public class ClientApplication {
	
	public static void main(String[] args) {
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
			RestTemplateFactory template = context.getBean(RestTemplateFactory.class);
			System.out.println(template.getObject());
			template.getObject().exchange("http://localhost:8080/events",
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {
            });
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*RestTemplate restTemplate = new RestTemplate();
		restTemplate.setMessageConverters(getMessageConverters());
		ResponseEntity<List<Event>> rateResponse =
		        restTemplate.exchange("http://localhost:8080/events",
		                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {
		            });
		List<Event> rates = rateResponse.getBody();
		for(Event e : rates)
			System.out.println(e.getEventId());
			*/
	}
}
