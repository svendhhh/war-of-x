package com.hansel;

import java.util.ArrayList;
import java.util.List;

import org.granite.messaging.amf.io.util.externalizer.annotation.ExternalizedBean;

@ExternalizedBean(type=org.granite.messaging.amf.io.util.externalizer.DefaultExternalizer.class)
public class LoginEntry {

	public boolean isLoggedIn;
	public String redirectURL;
	public long timestamp;
	public String greeting;
	public String logoutURL;
	
	protected List<String> testList = new ArrayList<String>();

	public LoginEntry(boolean isLoggedIn, String redirectURL, long timestamp, String greeting, String logoutURL) {
		super();
		this.isLoggedIn = isLoggedIn;
		this.redirectURL = redirectURL;
		this.timestamp = timestamp;
		this.greeting = greeting;
		this.logoutURL = logoutURL;
		testList.add("hello");
		testList.add("test");
	}
}
