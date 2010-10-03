package org.springframework.integration.samples.travel;

public interface TravelGateway {

	public String getWeatherByZip(String zip);
	
	public String getTrafficByZip(String zip);

}
