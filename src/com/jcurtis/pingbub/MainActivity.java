package com.jcurtis.pingbub;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.List;

import com.jcurtis.pingbub.AddBubbleDialogFragment.AddBubbleDialogListener;
import com.jcurtis.pingbub.RemoveBubbleDialogFragment.RemoveBubbleDialogListener;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


public class MainActivity extends ActionBarActivity  implements AddBubbleDialogListener, RemoveBubbleDialogListener {

	LinearLayout layout;
	Button addButton;
	Button velocityButton;
	
	
	BubbleManager bubbleManager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        if(savedInstanceState != null) {
            //Recreating Activity
        
        } else {
        	//New Activity
        	bubbleManager = new BubbleManager();
    		bubbleManager.setMainActivity(this);

        }
        
        
        layout = (LinearLayout) findViewById(R.id.linear_layout);
        addButton = (Button) findViewById(R.id.addButton);
        velocityButton = (Button)findViewById(R.id.velocityButton);
        
        
        
        
    }
    
    
    public void addBubble(View view) {
    	
    	Log.d("BUBBLE","Add Bubble!");
    	DialogFragment newFragment = new AddBubbleDialogFragment();
    	//newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.JCDark);
        newFragment.show(getSupportFragmentManager(), "Add Bubble");
    }
     
    public void removeBubble(String name) {
    	DialogFragment newFragment = new RemoveBubbleDialogFragment();
    	//newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.JCDark);

    	Bundle bundle = new Bundle();
    	bundle.putString("NAME", name);
    	newFragment.setArguments(bundle);
    	
        newFragment.show(getSupportFragmentManager(), "Remove Bubble");
    }
    
    boolean weekly = true;
    
    public void toggleView(View view) {
    	weekly = !weekly;
    	
    	refreshLayout();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String name) {
		// TODO Add a bubble
		Log.d("BUBBLE","SUCCESS! Adding bubble");

		/*String bubbleName = name;
		Button myButton = new Button(this);
		myButton.setText(bubbleName);

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout.addView(myButton, lp);
		*/
		
		bubbleManager.addBubble(name);
		refreshLayout();
		
	}


	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Do not add ping bubble
    	Log.d("BUBBLE","NEGATIVE GHOSTRIDER. Not adding bubble");

	}
	
	public final String SAVE_FILE = "bubble_manager_save.bin";
	
	@Override
	public void onPause() {
		super.onPause();
		
		saveToMemory();
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		BubbleManager bMan = getBubbleManagerFromMemory();
		if(bMan == null) {
			Log.d("PINGBUBG","NEW BUBBLE MANAGER");
			bubbleManager = new BubbleManager();
		} else {
			bubbleManager = bMan;
		}
		
		refreshLayout();
	}
	
	public BubbleManager getBubbleManagerFromMemory() {
		FileInputStream fin = null;
		try {
			fin = openFileInput(SAVE_FILE);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(fin);
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object o = null;
		try {
			if(ois != null)
				o = ois.readObject();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BubbleManager bMan = null;
		if(o != null) {
			bMan = (BubbleManager)o;
			//bMan.setMainActivity(this);
		}

		return bMan;
		
	}
	
	public void saveToMemory() {
		try {
			FileOutputStream fos = openFileOutput(SAVE_FILE, Context.MODE_PRIVATE);
			
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(bubbleManager);
			oos.flush();
			oos.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public final String BUBBLE_MANAGER_SAVE = "BubbleManager";
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		bubbleManager = (BubbleManager)savedInstanceState.getSerializable(BUBBLE_MANAGER_SAVE);
		//bubbleManager.setMainActivity(this);
		
		refreshLayout();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putSerializable(BUBBLE_MANAGER_SAVE, bubbleManager);
		
		
		super.onSaveInstanceState(savedInstanceState);
	}
	
	
	
	public void refreshLayout() {
		bubbleManager.setMainActivity(this);
		List<Button> buttons = bubbleManager.getRefreshedButtons(this, weekly);
		layout.removeAllViews();
		//layout.invalidate();
		
		for(Button b : buttons) {
			
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			layout.addView(b, lp);
		}
		
		
		int velocity = bubbleManager.getVelocity();
		velocityButton.setText(""+velocity);

		
		if(!weekly) {
			int total = bubbleManager.getTotalPings();
			velocityButton.setText(""+((float)velocity/(float)total) * 100 + " PV");

		}
		
		
		
		
		
		if(velocity < 6) {
			velocityButton.setBackgroundResource(R.drawable.red_button);

		} else if(velocity < 10) {
			velocityButton.setBackgroundResource(R.drawable.orange_button);
		} else if(velocity < 26) {
			velocityButton.setBackgroundResource(R.drawable.green_button);
		} else if (velocity < 35) {
			velocityButton.setBackgroundResource(R.drawable.blue_button);
		} else {
			velocityButton.setBackgroundResource(R.drawable.purple_button); 
		}
		
		if(!weekly) {
			velocityButton.setBackgroundResource(R.drawable.purple_button); 

		}
		
	}


	@Override
	public void onAcceptButtonRemove(DialogFragment dialog, String name) {
		// TODO Auto-generated method stub
		bubbleManager.removeBubble(name);
		refreshLayout();
	}


	@Override
	public void onCancelButtonRemove(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
	
	
	



}
