package com.hansel;

import java.util.Map;
import java.util.TreeMap;

public class ExpiryTreeMap<K,V extends ExpiryCacheable> extends TreeMap<K,V> {
	
	private static final long serialVersionUID = -5836364764100760411L;
	
	public final int maxAge;
	
	public ExpiryTreeMap(int maxAge) {
		this.maxAge = maxAge;
	}
	
	@Override
	public V put(K key, V value) {
		removeOld();
		return super.put(key, value);
	};
	
	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		removeOld();
		super.putAll(map);
	}
	
	public boolean removeOld(){
		boolean removedSome = false;
		long oldest = System.currentTimeMillis()-maxAge;
		while(!isEmpty() && lastEntry().getValue().getTimestamp() < oldest){
			removedSome = true;
			remove(lastEntry().getKey());
		}
		return removedSome;
	}
}
