package com.hansel;

import java.util.Collections;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

public enum GlobalCaches {
	WORLDCHAT,
	;
	
	public static WorldChatCacheWrapper getWorldChatCache() throws CacheException{
		//Get global cache
		Cache cache = CacheManager.getInstance().getCache("global");
		if(cache == null){
			//If missing create the cache
			cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
			CacheManager.getInstance().registerCache("global", cache);
		}
		//Get the world chat treeset from the cache
		BoundedTreeSetForChatEntries chatEntries = (BoundedTreeSetForChatEntries) cache.get(WORLDCHAT);
		
		boolean treeSetIsNew = false;
		if(chatEntries == null){
			//If missing create the treeset
			chatEntries = new BoundedTreeSetForChatEntries(360000);
			treeSetIsNew = true;
		}
		//Return the cache and treeset
		return new WorldChatCacheWrapper(treeSetIsNew,cache,chatEntries);
	}
	
	public static class WorldChatCacheWrapper{
		public final boolean treeSetIsNew;
		public final Cache cache;
		public final BoundedTreeSetForChatEntries chatEntries;
		
		public WorldChatCacheWrapper(boolean treeSetIsNew, Cache cache, BoundedTreeSetForChatEntries chatEntries) {
			this.treeSetIsNew = treeSetIsNew;
			this.cache = cache;
			this.chatEntries = chatEntries;
		}
	}
}
