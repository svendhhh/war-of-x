package com.hansel;

import java.util.Collections;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

public enum GlobalCaches {
	WORLDCHAT(360000),
	USERS(20000),
	;
	
	public final int cacheTime;
	
	private GlobalCaches(int cacheTime) {
		this.cacheTime = cacheTime;
	}
	
	public static <K,V extends ExpiryCacheable> CacheWrapper<K,V> getCache(GlobalCaches whichCache) throws CacheException{
		//Get global cache
		Cache cache = CacheManager.getInstance().getCache("global");
		if(cache == null){
			//If missing create the cache
			cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
			CacheManager.getInstance().registerCache("global", cache);
		}
		//Get the world chat TreeMap from the cache
		ExpiryTreeMap<K,V> chatEntries = (ExpiryTreeMap<K,V>) cache.get(whichCache);
		
		boolean treeSetIsNew = false;
		if(chatEntries == null){
			//If missing create the TreeMap
			chatEntries = new ExpiryTreeMap<K,V>(whichCache.cacheTime);
			treeSetIsNew = true;
		}
		//Return the cache and TreeMap
		return new CacheWrapper<K,V>(treeSetIsNew,cache,chatEntries);
	}
	
	public static class CacheWrapper<K,V extends ExpiryCacheable>{
		public final boolean treeMapIsNew;
		public final Cache cache;
		public final ExpiryTreeMap<K,V> entries;
		
		public CacheWrapper(boolean treeSetIsNew, Cache cache, ExpiryTreeMap<K,V> entries) {
			this.treeMapIsNew = treeSetIsNew;
			this.cache = cache;
			this.entries = entries;
		}
	}
}
