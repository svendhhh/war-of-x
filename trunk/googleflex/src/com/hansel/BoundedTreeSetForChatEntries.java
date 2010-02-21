package com.hansel;

import java.util.Collection;
import java.util.TreeSet;

public class BoundedTreeSetForChatEntries extends TreeSet<ChatEntry> {
	
	private static final long serialVersionUID = -5836364764100760411L;
	
	public final int maxAge;
	
	public BoundedTreeSetForChatEntries(int maxAge) {
		this.maxAge = maxAge;
	}
	
	@Override
	public boolean add(ChatEntry e) {
		removeOld();
		return super.add(e);
	}
	
	@Override
	public boolean addAll(Collection<? extends ChatEntry> c) {
		removeOld();
		return super.addAll(c);
	}
	
	public void removeOld(){
		long oldest = System.currentTimeMillis()-maxAge;
		while(size()>1 && last().timestamp < oldest)remove(last());
	}
}
