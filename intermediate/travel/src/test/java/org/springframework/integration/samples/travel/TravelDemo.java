package org.springframework.integration.samples.travel;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.xml.transform.StringSource;

public class TravelDemo {
	private static Logger logger = Logger.getLogger(TravelDemo.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		ApplicationContext ac = new FileSystemXmlApplicationContext("src/main/resources/META-INF/spring/*.xml");
		TravelGateway travelGateway = ac.getBean("travelGateway", TravelGateway.class);
		String weatherReply = travelGateway.getWeatherByZip("10035");
		logger.info("### Weather: \n" + formatResult(weatherReply));
		
		String trafficReply = travelGateway.getTrafficByZip("10035");
		logger.info("### Traffic: \n" + formatResult(trafficReply));
	}


	private static String formatResult(String result) throws Exception{
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(2));
		StreamResult streamResult = new StreamResult(new StringWriter());
		Source source = new StringSource(result);
		transformer.transform(source, streamResult);
		String xmlString = streamResult.getWriter().toString();
		return xmlString;
	}

}
