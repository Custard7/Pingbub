package com.jcurtis.pingbub;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class RemoveBubbleDialogFragment extends DialogFragment{
	
	String bubbleToRemoveName;
	
	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        
	        // Get the layout inflater
	        LayoutInflater inflater = getActivity().getLayoutInflater();

	        // Inflate and set the layout for the dialog
	        // Pass null as the parent view because its going in the dialog layout
	        //final View dialogView=inflater.inflate(R.layout.remove_bubble_dialog, null);
	        
	        bubbleToRemoveName = getArguments().getString("NAME");
	        
	        builder.setMessage("Remove Pingbubble \""+bubbleToRemoveName+"\"")
	               .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // Add pingbubble
	                       mListener.onAcceptButtonRemove(RemoveBubbleDialogFragment.this, bubbleToRemoveName);
	                   }
	               })
	               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                       mListener.onCancelButtonRemove(RemoveBubbleDialogFragment.this);

	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	 
	 
	 /* The activity that creates an instance of this dialog fragment must
	     * implement this interface in order to receive event callbacks.
	     * Each method passes the DialogFragment in case the host needs to query it. */
	    public interface RemoveBubbleDialogListener {
	        public void onAcceptButtonRemove(DialogFragment dialog, String name);
	        public void onCancelButtonRemove(DialogFragment dialog);
	    }
	    
	    // Use this instance of the interface to deliver action events
	    RemoveBubbleDialogListener mListener;
	    
	    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
	    @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        // Verify that the host activity implements the callback interface
	        try {
	            // Instantiate the NoticeDialogListener so we can send events to the host
	            mListener = (RemoveBubbleDialogListener) activity;
	        } catch (ClassCastException e) {
	            // The activity doesn't implement the interface, throw exception
	            throw new ClassCastException(activity.toString()
	                    + " must implement NoticeDialogListener");
	        }
	    }
}
