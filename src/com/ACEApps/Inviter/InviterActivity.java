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

@SuppressLint("ParserError")
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
	private String[] StringNames;
	private ArrayList<String> aList_mobileNumber;

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
		button_cancel = (Button) findViewById(R.id.inviter_button_cancel);
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
		switch (_view.getId()) {
		case R.id.inviter_button_next:

			boolean stored = true;
			try {
				String date = eText_date.getText().toString();
				String description = eText_description.getText().toString();

				SQLiteDAO entry = new SQLiteDAO(getApplicationContext());

				entry.open();
				entry.create(date, description);
				entry.close();
			} catch (Exception e) {
				stored = false;

			} finally {
				if (stored) {
					Intent s = new Intent(this, Inviter_SenderActivity.class);
					startActivity(s);
				} else {
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
			DatePickerDialog showDatePicker = new DatePickerDialog(this,
					mDateSetListener, mYear, mMonth, mDay);
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
	 * Name: peoplePickerHelper() Function: Helper for peoplePicker dialog
	 */
	private void peoplePickerHelper() {
		
		String [] array_nameString = new String[getNameContact().size()];
		array_nameString = (String[]) getNameContact().toArray(array_nameString);
		
		Log.d("tag", "getNameContact.size()" + getNameContact().size());
		
		for(int i = 0; i < getNameContact().size(); i++){
			Log.d("tag", "in getNameContact = " + array_nameString[i]);
		}
		
		if (array_nameString.length == 0) {
			String message = null;
			message = "You have no contact";
			Toast.makeText(InviterActivity.this, message, Toast.LENGTH_LONG).show();
		} else {

			build_alertDialog = new Builder(this);
			build_alertDialog.setTitle("Select people to invite");
			build_alertDialog.setCancelable(true);


			final OnMultiChoiceClickListener onClick = new OnMultiChoiceClickListener() {
				public void onClick(final DialogInterface dialog, final int which,
						final boolean isChecked) {
					Log.d("test", "which = " + which);

					Log.d("tag", "get at least here 10");
					if (isChecked) {
						if ((peoplePicker_mselected != -1)
								&& (peoplePicker_mselected != which)) {
							final int oldVal = peoplePicker_mselected;
							final AlertDialog alert = (AlertDialog) dialog;
							final ListView list = alert.getListView();
							list.setItemChecked(oldVal, false);
						}
						Log.d("test", "which = " + which);
					} else
						peoplePicker_mselected = -1;
				}
			};
			
			Log.d("tag", "get at least here 11");
			build_alertDialog.setMultiChoiceItems(array_nameString, null, onClick);
			
			build_alertDialog.setPositiveButton("Done",
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog, final int which) {
							String message = null;
							if (peoplePicker_mselected == -1)
								message = "You didn't select anything.";
							else
								// message = "You selected '" + strings[which] + "'";
								Toast
										.makeText(InviterActivity.this, message, Toast.LENGTH_LONG)
										.show();
						}
					});
		

			build_alertDialog.show();

			
		}
	}

	/**
	 * Name: displayDate() Function: display chosen date on the edit box working
	 * with the datePicker button
	 */
	private void displayDate() {
		eText_date.setText(new StringBuilder().append(mMonth + 1).append("/")
				.append(mDay).append("/").append(mYear).append(" "));
		// Month starts from 0. Need to add 1.
	}

	/**
	 * Name: getNameContact Function: retrieve names from the contact list
	 */

	private ArrayList<String> getNameContact() {
		ContentResolver cr = getContentResolver();
		Log.d("tag", "get at least here 1");
		Cursor cur_findName = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		Log.d("tag", "get at least here 2");
		String tempId;
		String mDecider = Integer
				.toString(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
		ArrayList<String> aList_name = new ArrayList<String>();

		if (cur_findName.getCount() > 0) {
			// int cnt = 1;
			
			while (cur_findName.moveToNext()) {
				tempId = cur_findName.getString(cur_findName
						.getColumnIndex(ContactsContract.Contacts._ID));
				if (Integer.parseInt(cur_findName.getString(cur_findName
						.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) != 0) {
					
					aList_name.add(cur_findName.getString(cur_findName
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
					
					Log.d("tag", "DISPLAY_NAME : " + cur_findName.getString(cur_findName
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
					
		//			Log.d("tag", "Test" +)

					Cursor cur_findMobile = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
							new String[] { tempId }, null);

					while (cur_findMobile.moveToNext()) {
						String phonetype = cur_findMobile.getString(cur_findMobile
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
						if (phonetype.equals(mDecider)) {
							aList_mobileNumber
									.add(cur_findMobile.getString(cur_findMobile
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
							break;
						}
					}
					cur_findMobile.close();
				}
			}

		}
		return aList_name;
	}

}