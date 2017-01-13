package io.gumga.application.spring.config;

import org.springframework.core.env.Environment;

/**
 * Wrapper to handle the <code>org.springframework.core.env.Environment</code>
 * properties. 
 * 
 * //TODO To add more converter getters like for BigDecimal, Double, Boolean, etc
 * 
 * @author gyowanny
 * @since jul/2015
 *
 */
public class EnvironmentHandler {

	private Environment env;
	
	public EnvironmentHandler(Environment _env){
		env = _env;
	}
	
	public String getString(String prop){
		return env.getProperty(prop);
	}
	
	public Integer getInteger(String prop, Integer defaultValue){
		String str = getString(prop);
		if (str == null || str.isEmpty()){
			return defaultValue;
		}else{
			return Integer.valueOf(str);
		}
	}
}
