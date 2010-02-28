package com.hansel;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

@SuppressWarnings("serial")
public class GoogletestServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		UserService userService = UserServiceFactory.getUserService();

		if(userService.isUserLoggedIn() && userService.isUserAdmin()){
		
			try{
				Cache cache = CacheManager.getInstance().getCache("global");
				if(cache == null){
					//If missing create the cache
					cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
					CacheManager.getInstance().registerCache("global", cache);
				}
				cache.clear();
				resp.getWriter().println("Cache Cleared");
			}catch(CacheException e){
				e.printStackTrace(resp.getWriter());
			}
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			String query = "SELECT FROM " + UserEntry.class.getName();
			List<UserEntry> curentUsers = (List<UserEntry>)pm.newQuery(query).execute();
			//Add them to the TreeMap
			for(UserEntry e: curentUsers){
				pm.deletePersistent(e);
			}
			
			query = "SELECT FROM " + ChatEntry.class.getName();
			List<ChatEntry> allchat = (List<ChatEntry>)pm.newQuery(query).execute();
			//Add them to the TreeMap
			for(ChatEntry e: allchat){
				pm.deletePersistent(e);
			}
			pm.close();
			resp.getWriter().println("Data Cleared");
		}
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
