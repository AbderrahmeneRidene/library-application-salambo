package com.sipacademy.stockmanager.exceptions;

public class ProviderNotFoundException extends RuntimeException{
	
	public ProviderNotFoundException() {
		super();
	}
	
	public ProviderNotFoundException(String msg) {
		super(msg);
	}
	
	public ProviderNotFoundException(Exception ex) {
		super(ex);
	}

}