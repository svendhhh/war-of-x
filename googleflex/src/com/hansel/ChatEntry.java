package com.hansel;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.granite.messaging.amf.io.util.externalizer.annotation.ExternalizedBean;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class ChatEntry extends ExpiryCacheable implements Serializable{
	private static final long serialVersionUID = -3712988440023702414L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	public Key key;

	@Persistent
	public Key userID;
	
	@NotPersistent
	public UserEntry user;

	@Persistent
	public String chatText;

	@Persistent
	public Long timestamp;

	public ChatEntry(UserEntry user, String chatText) {
		super();
		this.user = user;
		this.userID = KeyFactory.createKey(UserEntry.class.getSimpleName(),user.userID);
		this.chatText = chatText;
		this.timestamp = System.currentTimeMillis();
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}
}
