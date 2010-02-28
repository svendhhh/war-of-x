package com.hansel;

import java.util.List;

import org.granite.messaging.amf.io.util.externalizer.annotation.ExternalizedBean;

@ExternalizedBean(type=org.granite.messaging.amf.io.util.externalizer.DefaultExternalizer.class)
public class UpdateEntry {
	public List<ChatEntry> chatUpdates;
	public List<UserEntry> activeUsers;
	
	public UpdateEntry(List<ChatEntry> chatUpdates, List<UserEntry> activeUsers){
		this.chatUpdates = chatUpdates;
		this.activeUsers = activeUsers;
	}	
}
