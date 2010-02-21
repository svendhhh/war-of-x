package com.hansel;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ChatEntry implements Comparable<ChatEntry>, Serializable{
	@SuppressWarnings("unused")
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	public String user;

	@Persistent
	public String chatText;

	@Persistent
	public Long timestamp;

	public ChatEntry(String user, String chatText) {
		super();
		this.user = user;
		this.chatText = chatText;
		this.timestamp = System.currentTimeMillis();
	}

	@Override
	public int compareTo(ChatEntry o) {
		return (int) (timestamp-o.timestamp);
	}
}
