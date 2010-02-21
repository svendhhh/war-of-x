package com.hansel;

public class LoginEntry {

	public final boolean isLoggedIn;
	public final String redirectURL;
	public final long timestamp;
	public final String greeting;

	public LoginEntry(boolean isLoggedIn, String redirectURL, long timestamp, String greeting) {
		super();
		this.isLoggedIn = isLoggedIn;
		this.redirectURL = redirectURL;
		this.timestamp = timestamp;
		this.greeting = greeting;
	}

}
