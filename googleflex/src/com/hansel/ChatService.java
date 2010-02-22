package com.hansel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jdo.PersistenceManager;

import net.sf.jsr107cache.CacheException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.hansel.GlobalCaches.WorldChatCacheWrapper;

public class ChatService {
	
	public LoginEntry loginURL(){
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		String loginURL = userService.createLoginURL("/googleflex/myChat.html");
		String logoutURL = userService.createLogoutURL("/googleflex/myChat.html");

		return new LoginEntry(user!=null, loginURL, System.currentTimeMillis(), 
				user!=null?("Hello "+user.getNickname()+", welcome to the chat room"):"", logoutURL);
	}

	@SuppressWarnings("unchecked")
	public synchronized List<ChatEntry> getChat(long lastUpdateTime) throws CacheException {
		
		WorldChatCacheWrapper worldChatCache = GlobalCaches.getWorldChatCache();
							
		if(worldChatCache.treeSetIsNew){
			//Get all chat items that should be cached
			PersistenceManager pm = PMF.get().getPersistenceManager();
			String query = "SELECT FROM " + ChatEntry.class.getName() + " WHERE timestamp > " + (System.currentTimeMillis()-360000);
			List<ChatEntry> curentChat = (List<ChatEntry>)pm.newQuery(query).execute();
			//Add them to the treeset
			worldChatCache.chatEntries.addAll(curentChat);
			//Put the treeset in the cache
			worldChatCache.cache.put(GlobalCaches.WORLDCHAT, worldChatCache.chatEntries);
			
		}
		//Create a result list, max size is the cache size.
		ArrayList<ChatEntry> resultList = new ArrayList<ChatEntry>(worldChatCache.chatEntries.size());
		if(!worldChatCache.chatEntries.isEmpty()){
			//If the last entry is the same as the clients no updates are needed.
			if(worldChatCache.chatEntries.last().timestamp <= lastUpdateTime) return Collections.EMPTY_LIST;
			//Add all new entries to the results
			for(ChatEntry e: worldChatCache.chatEntries){
				if(e.timestamp <= lastUpdateTime) continue;
				resultList.add(e);
			}
		}
		//return the results
		return resultList;
	}
	
	public synchronized void addChat(String chatText) throws CacheException {
		//Get the user, add null check in future.
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		//Create a new chatEntry and save it
		ChatEntry newChat = new ChatEntry(user.getNickname(), chatText);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(newChat);
		pm.close();
		
		WorldChatCacheWrapper worldChatCache = GlobalCaches.getWorldChatCache();
		worldChatCache.chatEntries.add(newChat);
		worldChatCache.cache.put(GlobalCaches.WORLDCHAT, worldChatCache.chatEntries);
	}
}
