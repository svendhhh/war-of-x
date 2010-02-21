package com.hansel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;

import sun.security.jca.GetInstance;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class ChatService {
	
	public LoginEntry loginURL(){
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		String loginURL = userService.createLoginURL("/googleflex/myChat.html");

		return new LoginEntry(user != null, loginURL, System.currentTimeMillis(), "Our warm personalised welcome!");
	}

	public synchronized List<ChatEntry> getChat(long lastUpdateTime) {
		
		Cache cache = CacheManager.getInstance().getCache("global");		
		if(cache!=null){
			BoundedTreeSetForChatEntries chatEntries = (BoundedTreeSetForChatEntries) cache.get(0);
			if(chatEntries != null && !chatEntries.isEmpty()){
				
				if(chatEntries.last().timestamp <= lastUpdateTime) return Collections.EMPTY_LIST;
				
				ArrayList<ChatEntry> resultList = new ArrayList<ChatEntry>(chatEntries.size());
				for(ChatEntry e: chatEntries){
					if(e.timestamp <= lastUpdateTime) continue;
					resultList.add(e);
				}
				return resultList;
			}
		}
		
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "SELECT FROM " + ChatEntry.class.getName() + " WHERE timestamp > " + lastUpdateTime + " ORDER BY timestamp ASC";
		List<ChatEntry> curentChat = (List<ChatEntry>)pm.newQuery(query).execute();

		return curentChat;
	}
	
	public synchronized void addChat(String chatText){		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		ChatEntry newChat = new ChatEntry(user.getNickname(), chatText);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(newChat);
		pm.close();
		
		try{
			Cache cache = CacheManager.getInstance().getCache("global");
			if(cache == null){
				cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
				CacheManager.getInstance().registerCache("global", cache);
			}
			BoundedTreeSetForChatEntries chatEntries = (BoundedTreeSetForChatEntries) cache.get(0);
			
			if(chatEntries == null){
				chatEntries = new BoundedTreeSetForChatEntries(360000);
			}
			chatEntries.add(newChat);
			cache.put(0, chatEntries);
		}catch (CacheException e) {
			e.printStackTrace();
		}
	}
}
