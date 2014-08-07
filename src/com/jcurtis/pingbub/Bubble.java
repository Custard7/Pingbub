package com.jcurtis.pingbub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bubble implements Comparable<Bubble>, Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9077519765905771497L;

	private List<Ping> pings;
	
	String name;
	
	public String getName() {
		return name;
	}
	public void setName(String s) {
		name = s;
	}
	
	public Bubble(String name) {
		
		pings = new ArrayList<Ping>();
		setName(name);
	}
	
	
	
	public void ping() {
		Ping ping = new Ping();
		pings.add(ping);
	}
	
	public List<Ping> getPings() {
		return pings;
	}
	
	public int getPingCount() {
		return pings.size();
	}
	
	public int getRecentPingCount(int days) {
		Collections.sort(pings);
		
		int pingCount = 0;
		
		for(Ping p : pings) {
			
			if(p.isYoungerThan(days)) {
				pingCount++;
			} else {
				return pingCount;
			}
			
		}
		return pingCount;
		
	}
	
	public Ping getMostRecentPing() {
		
		if(getPingCount() <= 0)
			return null;
		
		Collections.sort(pings);
		
		return pings.get(0);
		
	}
	
	
	
	@Override
	public int compareTo(Bubble another) {
		return getPingCount() < another.getPingCount() ? 1
				: getPingCount() > another.getPingCount() ? -1
						: 0;
		
	}
	
	
	
	

}
