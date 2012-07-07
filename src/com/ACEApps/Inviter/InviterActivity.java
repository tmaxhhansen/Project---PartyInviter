package com.ACEApps.Inviter;

import java.util.ArrayList;
import java.util.Calendar;

import com.ACEApps.DB.SQLiteDAO;
import com.ACEApps.main.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InviterActivity extends Activity implements OnClickListener {

	private View view_background;

	private TextView text_date;
	private TextView text_description;

	private Button button_datePicker;
	private Button button_peoplePicker;
	private Button button_send;
	private Button button_cancel;

	private EditText eText_date;
	private EditText eText_description;

	/*** Date Picker ***/
	private static final int DATE_PICK_DIALOG_ID = 0;
	private static final int PEOPLE_PICK_DIALOG_ID = 1;
	private int mYear;
	private int mMonth;
	private int mDay;
	
	/*** People Picker ***/
	private int peoplePicker_mselected = -1;
	private Builder build_alertDialog;
	private ArrayList<String> aList_name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inviter_main);
		
		text_date = (TextView) findViewById(R.id.inviter_text_date);
		text_description = (TextView) findViewById(R.id.inviter_text_description);
		
		view_background = (View) findViewById(R.id.inviter_background);
		view_background.setOnClickListener(this);
		
		eText_date = (EditText) findViewById(R.id.inviter_edit_showdate);
		eText_description = (EditText) findViewById(R.id.inviter_edit_description);
		
		button_send = (Button) findViewById(R.id.inviter_button_next);
		button_send.setOnClickListener(this);
		button_cancel= (Button) findViewById(R.id.inviter_button_cancel);
		button_cancel.setOnClickListener(this);

		button_peoplePicker = (Button) findViewById(R.id.inviter_button_peoplepicker);
		button_peoplePicker.setOnClickListener(this);
		
		// button for datePicker connected to datepicker dialog 
		button_datePicker = (Button) findViewById(R.id.inviter_button_datepicker);
		button_datePicker.setOnClickListener(this);

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		displayDate();
	}

	public void onClick(View _view) {
		switch(_view.getId()){
		case R.id.inviter_button_next:
			
			boolean stored = true;
			try{
			String date = eText_date.getText().toString();
			String description = eText_description.getText().toString();
			
			SQLiteDAO entry = new SQLiteDAO(getApplicationContext());
			
			entry.open();
			entry.create(date, description);
			entry.close();
			}catch (Exception e){
				stored = false;
				
			}finally{
				if(stored){
					Intent s = new Intent(this, Inviter_SenderActivity.class);
					startActivity(s);
				}else{
					Dialog popup = new Dialog(this);
					popup.setTitle("Storing failed");
					TextView tv = new TextView(this);
					tv.setText("Please try again");
					popup.setContentView(tv);
					popup.show();
				}
			}
			break;
			
		case R.id.inviter_button_datepicker:
			DatePickerDialog showDatePicker = new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);	
			showDatePicker.show();
			break;
			
		case R.id.inviter_button_peoplepicker:
			peoplePickerHelper();
			break;
		}
	}

	/*** protected methods ***/

	
	/**
	 * DatePicker Dialog setup
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			displayDate();
		}
	};


	/*** Private methods ***/
	
	private void hideKeyboard() {

	}
	
	/**
	 * Name: peoplePickerHelper()
	 * Function: Helper for peoplePicker dialog
	 */
	private void peoplePickerHelper(){
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
	            if ((peoplePicker_mselected != -1) && (peoplePicker_mselected != which)) {
	              final int oldVal = peoplePicker_mselected;
	              final AlertDialog alert = (AlertDialog)dialog;
	              final ListView list = alert.getListView();
	              list.setItemChecked(oldVal, false);
	            }
	            Log.d("test", "which = " + which);
	          } else
	          	peoplePicker_mselected = -1;
	        }
	      };
//	      build_alertDialog.setMultiChoiceItems(strings, null, onClick);
	      build_alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
	       public void onClick(final DialogInterface dialog,
	                                    final int which) {
	        String message = null;
	        if (peoplePicker_mselected == -1)
	          message = "You didn't select anything.";
	        else
//	          message = "You selected '" + strings[mSelected] + "'";
	        Toast.makeText(InviterActivity.this, message, Toast.LENGTH_LONG).show();
	      }
	    });
	      build_alertDialog.show();
	}
	
	
	/**
	 * Name: displayDate()
	 * Function: display chosen date on the edit box 
	 *           working with the datePicker button
	 */
	private void displayDate() {
		eText_date.setText(new StringBuilder().append(mMonth + 1).append("/")
				.append(mDay).append("/").append(mYear).append(" "));
		// Month starts from 0. Need to add 1.
	}
	
	
	/**
	 * Name: getNameContact
	 * Function: retrieve names from the contact list 
	 */
	@SuppressLint("ParserError")
	private String[] getNameContact(){
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		String tempName;
		String tempId;
		if(cur.getCount() > 0){
			int cnt = 1; 
			while(cur.moveToNext()){
				
			}
		}
		
		return null;
	}
          

}
