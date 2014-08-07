package com.jcurtis.pingbub;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

public class BubbleManager implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2176262654273084714L;
	
	
	List<Bubble> bubbles;
	
	public BubbleManager() {
		
		
		bubbles = new ArrayList<Bubble>();
		
	}
	
	
	public void addBubble(String name) {
		Bubble bubble = new Bubble(name);
		bubbles.add(bubble);
	}
	
	public transient MainActivity mainActivity;
	
	
	public List<Button> getRefreshedButtons(Context context, boolean weekly) {
		Collections.sort(bubbles);
		
		List<Button> buttons = new ArrayList<Button>();
		
		int totalPingCount = 1;
		
		
		for(Bubble b : bubbles) {
			if(weekly) {
				totalPingCount+=b.getRecentPingCount(1);
			} else {
				totalPingCount+=b.getPingCount();
			}
		}
		
		for(Bubble b : bubbles) {
			Log.d("Bubble Manager","Refreshing button: " + b.getName() );
		
			String bubbleName = b.getName();
			Button myButton = new Button(context);
			//Button myButton = (Button) LayoutInflater.from(context).inflate(R.drawable.styled_button, null);
			myButton.setBackgroundResource(R.drawable.red_button);
			myButton.setTextAppearance(context, R.style.button_text);
			
			/*
			Ping recentPing = b.getMostRecentPing();
			if(recentPing != null) {
				
				Date date = Calendar.getInstance().getTime();
				
				date.setDate(Calendar.getInstance().get(Calendar.DATE)-7);
				if(recentPing.getDate().after(date)) myButton.setBackgroundResource(R.drawable.red_button);
				
				date.setDate(Calendar.getInstance().get(Calendar.DATE)-5);
				if(recentPing.getDate().after(date)) myButton.setBackgroundResource(R.drawable.orange_button);
				
				date.setDate(Calendar.getInstance().get(Calendar.DATE)-3);
				if(recentPing.getDate().after(date)) myButton.setBackgroundResource(R.drawable.green_button);
				
				date.setDate(Calendar.getInstance().get(Calendar.DATE)-2);
				if(recentPing.getDate().after(date)) myButton.setBackgroundResource(R.drawable.blue_button);
				
				date.setDate(Calendar.getInstance().get(Calendar.DATE)-1);
				if(recentPing.getDate().after(date)) myButton.setBackgroundResource(R.drawable.purple_button); 			
				
			}*/
			
			float percent = ((float)b.getPingCount()) / ((float)totalPingCount);
			
			if(percent < .02f) {
				myButton.setBackgroundResource(R.drawable.red_button);

			} else if(percent < .06f) {
				myButton.setBackgroundResource(R.drawable.orange_button);
			} else if(percent < .12f) {
				myButton.setBackgroundResource(R.drawable.green_button);
			} else if (percent < .25f) {
				myButton.setBackgroundResource(R.drawable.blue_button);
			} else {
				myButton.setBackgroundResource(R.drawable.purple_button); 
			}
			
			
			myButton.setTag(bubbleName);
			//myButton.setTag(2, b);
			myButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					pingBubble((String)(v.getTag()));				
				}
				
			});
			
			myButton.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					removeBubbleDialog((String)v.getTag());
					return true;
				}
				
			});
			
			
			if(weekly) {
				myButton.setText(bubbleName + " : " + b.getRecentPingCount(1));
			} else {
				myButton.setText(bubbleName + " : " + b.getPingCount() + "PT");
			}
			
			buttons.add(myButton);
		}
		
		refreshed();
		
		return buttons;
	}
	
	public void pingBubble(String name) {
		for(Bubble b : bubbles) {
			
			if(b.getName().equalsIgnoreCase(name)) {
				b.ping();
				changed();
				return;
			}
			
		}
	}
	
	
	private boolean _hasChanged = false;
	
	public boolean hasChanged() {
		return _hasChanged;
	}
	private void changed() {
		_hasChanged = true;
		if(mainActivity != null)
			mainActivity.refreshLayout();
	}
	private void refreshed() {
		_hasChanged = false;
	}

	
	public void setMainActivity(MainActivity ac) {
		this.mainActivity = ac;
	}

	
	
	public int getVelocity() {
		
		int velocity = 0;
		
		for(Bubble b : bubbles) {
			for(Ping p : b.getPings()) {
				if(p.isYoungerThan(1)) {
					velocity += 2;
				} else if(p.isYoungerThan(2)) {
					velocity += 1;
				}
			}
 		}
		
		
		return velocity;
	}
	
	public int getTotalPings() {
		
		int total = 0;
		
		for(Bubble b : bubbles) {
			total += b.getPingCount();
 		}
		
		
		return total;
		
	}
	
	public void removeBubbleDialog(String name) {
		
		if(this.mainActivity != null) {
			this.mainActivity.removeBubble(name);
		}
		
	}
	
	public void removeBubble(String name) {
		
		Log.d("BUBBLE MANAGER","Trying to remove bubble: " + name);
		
		List<Bubble> toRemove = new ArrayList<Bubble>();
		for(Bubble b : bubbles) {
			if(b.getName().equalsIgnoreCase(name)) {
				toRemove.add(b);
				Log.d("BUBBLE MANAGER","Added to remove bubble: " + name);
			}
		}
		bubbles.removeAll(toRemove);
		
	}
	
	
	
}
