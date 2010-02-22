package com.hansel;

import java.util.List;

public class UpdateEntry {
	public final List<ChatEntry> chatUpdates;
	public final List<UserEntry> activeUsers;
	
	public UpdateEntry(List<ChatEntry> chatUpdates, List<UserEntry> activeUsers){
		this.chatUpdates = chatUpdates;
		this.activeUsers = activeUsers;
	}
}
