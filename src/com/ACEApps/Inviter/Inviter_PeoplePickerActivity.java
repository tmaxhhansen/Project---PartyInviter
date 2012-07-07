package com.ACEApps.Inviter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class Inviter_PeoplePickerActivity extends Activity{
	
	private Builder build_alertDialog;
	private int mSelected = -1;	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		build_alertDialog = new Builder(this);
		build_alertDialog.setTitle("Select people to invite");
		build_alertDialog.setCancelable(true);
		
	  ///TODO: Make a String arrayList to store names from conctacts
		
		final OnMultiChoiceClickListener onClick =
	      new OnMultiChoiceClickListener() {
	         public void onClick(final DialogInterface dialog,
	                                      final int which, final boolean isChecked){
	         Log.d("test", "which = " + which);

	          if (isChecked) {
	            if ((mSelected != -1) && (mSelected != which)) {
	              final int oldVal = mSelected;
	              final AlertDialog alert = (AlertDialog)dialog;
	              final ListView list = alert.getListView();
	              list.setItemChecked(oldVal, false);
	            }
	            Log.d("test", "which = " + which);
	          } else
	            mSelected = -1;
	        }
	      };
//	      build_alertDialog.setMultiChoiceItems(strings, null, onClick);
	      build_alertDialog.setPositiveButton("Done", new OnClickListener() {
	       public void onClick(final DialogInterface dialog,
	                                    final int which) {
	        String message = null;
	        if (mSelected == -1)
	          message = "You didn't select anything.";
	        else
//	          message = "You selected '" + strings[mSelected] + "'";
	        Toast.makeText(Inviter_PeoplePickerActivity.this, message, Toast.LENGTH_LONG).show();
	      }
	    });
	      build_alertDialog.show();
	  }
	}
