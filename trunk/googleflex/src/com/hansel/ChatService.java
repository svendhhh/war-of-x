package com.hansel;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import net.sf.jsr107cache.CacheException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.hansel.GlobalCaches.CacheWrapper;

public class ChatService {
	
	public LoginEntry loginURL(){
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		String loginURL = userService.createLoginURL("/googleflex/myChat.html");
		String logoutURL = userService.createLogoutURL("/googleflex/myChat.html");

		return new LoginEntry(user!=null, loginURL, System.currentTimeMillis(), 
				user!=null?("Hello "+user.getNickname()+", welcome to the chat room"):"", logoutURL);
	}

	public synchronized UpdateEntry getUpdate(long lastUpdateTime) throws CacheException {
		
		//Get the user, add null check in future.
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		ArrayList<ChatEntry> chatUpdates = new ArrayList<ChatEntry>();
		ArrayList<UserEntry> activeUsers = new ArrayList<UserEntry>();
		
		
		CacheWrapper<ChatEntry,ChatEntry> worldChatCache = GlobalCaches.getCache(GlobalCaches.WORLDCHAT);
							
		if(worldChatCache.treeMapIsNew){
			//Get all chat items that should be cached
			String query = "SELECT FROM " + ChatEntry.class.getName() + " WHERE timestamp > " + (System.currentTimeMillis()-GlobalCaches.WORLDCHAT.cacheTime);
			List<ChatEntry> curentChat = (List<ChatEntry>)pm.newQuery(query).execute();
			//Add them to the TreeMap
			for(ChatEntry e: curentChat){
				worldChatCache.entries.put(e, e);
			}
			//Put the TreeMap in the cache
			worldChatCache.cache.put(GlobalCaches.WORLDCHAT, worldChatCache.entries);
			
		}
		//Remove old and save if needed.
		if(worldChatCache.entries.removeOld())worldChatCache.cache.put(GlobalCaches.WORLDCHAT, worldChatCache.entries);

		out:
		if(!worldChatCache.entries.isEmpty()){
			//If the last entry is the same as the clients no updates are needed.
			if(worldChatCache.entries.lastEntry().getValue().timestamp <= lastUpdateTime) break out;
			//Add all new entries to the results
			for(ChatEntry e: worldChatCache.entries.values()){
				if(e.timestamp <= lastUpdateTime) continue;
				chatUpdates.add(e);
			}
		}
		
		
		CacheWrapper<String,UserEntry> userCache = GlobalCaches.getCache(GlobalCaches.USERS);
		
		if(userCache.treeMapIsNew){
			//Get all chat items that should be cached
			String query = "SELECT FROM " + UserEntry.class.getName() + " WHERE lastSeen > " + (System.currentTimeMillis()-GlobalCaches.USERS.cacheTime);
			List<UserEntry> curentUsers = (List<UserEntry>)pm.newQuery(query).execute();
			//Add them to the TreeMap
			for(UserEntry e: curentUsers){
				userCache.entries.put(e.userID, e);
			}
			//Put the TreeMap in the cache
			userCache.cache.put(GlobalCaches.USERS, userCache.entries);
			
		}
		seenUser(user, userCache, pm, true);
		
		//Remove old and save if needed.
		if(userCache.entries.removeOld())userCache.cache.put(GlobalCaches.USERS, userCache.entries);
		activeUsers.addAll(userCache.entries.values());
	
		pm.close();
		//return the results
		return new UpdateEntry(chatUpdates, activeUsers);
	}
	
	public synchronized void addChat(String chatText) throws CacheException {
		//Get the user, add null check in future.
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		CacheWrapper<String,UserEntry> userCache = GlobalCaches.getCache(GlobalCaches.USERS);
		UserEntry userEntry = seenUser(user, userCache, pm, false);
		
		//Create a new chatEntry and save it
		ChatEntry newChat = new ChatEntry(userEntry, chatText);
		pm.makePersistent(newChat);
		pm.close();
		
		CacheWrapper<ChatEntry,ChatEntry> worldChatCache = GlobalCaches.getCache(GlobalCaches.WORLDCHAT);
		worldChatCache.entries.put(newChat,newChat);
		worldChatCache.cache.put(GlobalCaches.WORLDCHAT, worldChatCache.entries);
	}
	
	private UserEntry seenUser(User user, CacheWrapper<String,UserEntry> userCache, PersistenceManager pm, boolean save){
		UserEntry userEntry = userCache.entries.get(user.getUserId());
		if(userEntry==null){
			userEntry = new UserEntry(user);
		}
		userEntry.lastSeen=System.currentTimeMillis();
		userCache.entries.put(user.getUserId(), userEntry);
		userCache.cache.put(GlobalCaches.USERS, userCache.entries);
		
		if(save)pm.makePersistent(userEntry);
		
		return userEntry;
	}
}
