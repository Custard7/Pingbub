package com.jcurtis.pingbub;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Ping implements Comparable<Ping>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3886180235242347742L;
	Date date;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date d) {
		this.date = d;
	}
	
	public Ping() {
		
		Calendar calendar = Calendar.getInstance();
		date = calendar.getTime();
		
	}


	@Override
	public int compareTo(Ping another) {
		// TODO Auto-generated method stub
		return date.compareTo(another.getDate());
	}
	
	
	public boolean isYoungerThan(long days) {
		
		Calendar thatDay = Calendar.getInstance();
		thatDay.set(Calendar.DAY_OF_MONTH, date.getDay());
		thatDay.set(Calendar.MONTH, date.getMonth());
		thatDay.set(Calendar.YEAR, date.getYear());
		
		Calendar today = Calendar.getInstance();
		
		long difference = today.getTimeInMillis() - thatDay.getTimeInMillis();
		
		long daysDiff = difference / (24 * 60 * 60 * 1000);

		return daysDiff > difference ? false
				:true;
		
	}
	
}
