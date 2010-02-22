package com.hansel;

public abstract class ExpiryCacheable implements Comparable<ExpiryCacheable> {
	public abstract long getTimestamp();
	
	@Override
	public int compareTo(ExpiryCacheable o) {
		return (int) (getTimestamp()-o.getTimestamp());
	}
}
