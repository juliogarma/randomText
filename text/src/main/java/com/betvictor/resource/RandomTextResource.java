package com.betvictor.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RandomTextResource {

	@Value( "${com.betvictor.randomtext.end-point}" )
	private String endPoint;
	
	
	/**
	 * 
	 * @param amount
	 * @param number
	 * @param number_max
	 * @return
	 */
	public RandomTextResponse getResponse(int amount, int number, int number_max) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("user-agent", "");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		
		//parseURL
		String parsedURL = endPoint.replace("{type}", "giberish");
		parsedURL = parsedURL.replace("{format}", "p");
		parsedURL = parsedURL.replace("{amount}", Integer.toString(amount));
		parsedURL = parsedURL.replace("{number}", Integer.toString(number));
		parsedURL = parsedURL.replace("{number_max}", Integer.toString(number_max));
				
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<RandomTextResponse> responseEntity   = restTemplate.exchange(parsedURL, HttpMethod.GET, entity, RandomTextResponse.class);

		return responseEntity.getBody();
	}
	
}
