package org.springframework.integration.samples.cafe.xml;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class CafeDemoAppUtilities {

	/** spring profile for running locally */
	public static final String DEV = "dev";
	/** spring profile for running in cloud foundry */
	public static final String CLOUD = "cloud";


	/**
	 * 
	 * @param path path to the file
	 * @param targetClass the class who's classloader we will use to laod the context file
	 * @param profile a profile name
	 * @return the spring context
	 */
	public static AbstractApplicationContext loadProfileContext(String path, Class targetClass, String profile) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.getEnvironment().setActiveProfiles(profile);
		ctx.setClassLoader(targetClass.getClassLoader());
		ctx.load(path);
		ctx.refresh();
		return ctx;
	}
	



}
