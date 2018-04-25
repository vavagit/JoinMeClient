package vava.model.communication;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import vava.model.communication.authentification.ClientBasicAuthHttpRequestFactory;


public class RestTemplateFactory implements InitializingBean, FactoryBean<RestTemplate> {
	
	private RestTemplate restTemplate;

	/**
	 * Vytvorenie zoznamu konverterov prichadzajucej spravy
	 */
	private static List<HttpMessageConverter<?>> getMessageConverters() {
	    List<HttpMessageConverter<?>> converters = 
	      new ArrayList<HttpMessageConverter<?>>();
	    converters.add(new MappingJackson2HttpMessageConverter());
	    return converters;
	}
	
	 
    public RestTemplate getObject() {
        return restTemplate;
    }
    public Class<RestTemplate> getObjectType() {
        return RestTemplate.class;
    }
    public boolean isSingleton() {
        return true;
    }
 
    public void afterPropertiesSet() {
        HttpHost host = new HttpHost("localhost", 8080, "http");
        restTemplate = new RestTemplate(
          new ClientBasicAuthHttpRequestFactory(host));
        restTemplate.setMessageConverters(getMessageConverters());
        setAuthorization("user", "password");
    }

    public void setAuthorization(String username, String password) {
    	 restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(username, password));
    }
    
    public void clearAuthorization() {
    	restTemplate.getInterceptors().clear();
    }
}
