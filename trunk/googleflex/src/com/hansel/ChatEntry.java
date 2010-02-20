package com.hansel;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ChatEntry {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	public String user;

	@Persistent
	public String chatText;

	public ChatEntry(String user, String chatText) {
		super();
		this.user = user;
		this.chatText = chatText;
	}
}
