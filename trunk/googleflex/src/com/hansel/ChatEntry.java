package com.hansel;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ChatEntry extends ExpiryCacheable implements Serializable{
	private static final long serialVersionUID = -3712988440023702414L;

	@SuppressWarnings("unused")
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	public UserEntry user;

	@Persistent
	public String chatText;

	@Persistent
	public Long timestamp;

	public ChatEntry(UserEntry user, String chatText) {
		super();
		this.user = user;
		this.chatText = chatText;
		this.timestamp = System.currentTimeMillis();
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}
}
