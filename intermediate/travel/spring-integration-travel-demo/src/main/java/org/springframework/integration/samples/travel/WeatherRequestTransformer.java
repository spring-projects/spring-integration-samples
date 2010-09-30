package org.springframework.integration.samples.travel;

public class WeatherRequestTransformer {

	public String transform(String zipCode){
		return "<weat:GetCityWeatherByZIP xmlns:weat=\"http://ws.cdyne.com/WeatherWS/\">" +
				"	<weat:ZIP>" + zipCode + "</weat:ZIP>" +
				"</weat:GetCityWeatherByZIP>";
	}
}
