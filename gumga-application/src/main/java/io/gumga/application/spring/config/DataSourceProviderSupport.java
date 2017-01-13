package io.gumga.application.spring.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * Abstract class to provide auxiliary methods to the Data Source Provider implementations.
 * 
 * @author gyowanny
 * @since jul/2015
 */
public abstract class DataSourceProviderSupport {

	private static final StandardPBEStringEncryptor passwordEncryptor = new StandardPBEStringEncryptor();
	{
		passwordEncryptor.setPassword("_Gumga#@!_21231_");
	}
	
	/**
	 * Decrypts the given password if it starts with the 'ENC' keyword
	 * @param password The password to be verified
	 * @return The decrypted password if it starts with 'ENC' or the password itself
	 */
	public String decryptPassword(String password){
		String result = password;
		if (password != null && password.startsWith("ENC")){
			result = passwordEncryptor.decrypt(password);
		}
		return result;
	}
	
}
