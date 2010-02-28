package com.hansel;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.granite.messaging.amf.io.util.externalizer.annotation.ExternalizedBean;

import com.google.appengine.api.users.User;

@PersistenceCapable
public class UserEntry extends ExpiryCacheable implements Serializable{

	private static final long serialVersionUID = -662514534343716078L;

	@PrimaryKey
	@Persistent
	public String userID;

	@Persistent
	public String nickName;

	@Persistent
	public long lastSeen;
	
	public UserEntry(User user) {
		super();
		this.userID = user.getUserId();
		nickName = user.getNickname();
		int atSymb = nickName.indexOf('@');
		if(atSymb != -1)nickName = nickName.substring(0, atSymb);
		this.lastSeen = System.currentTimeMillis();
	}

	@Override
	public long getTimestamp() {
		return lastSeen;
	}
}
