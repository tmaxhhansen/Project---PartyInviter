package com.ACEApps.main;

import com.ACEApps.Calendar.CalendarActivity;
import com.ACEApps.Inviter.InviterActivity;
import com.ACEApps.main.R;
import com.ACEApps.main.R.id;
import com.ACEApps.main.R.layout;
import com.ACEApps.List.ListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PartyInvitorActivity extends Activity implements OnClickListener {
	private static final String tag = "PartyInvitorAcitivy";
	
	private Button button_invitor;
	private Button button_listshow;
	private Button button_calendar; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        // Button for invitor
        button_invitor = (Button) findViewById(R.id.button_inviter);
        button_invitor.setOnClickListener(this);
        
        // Button for listshow
        button_listshow = (Button) findViewById(R.id.button_listshow);
        button_listshow.setOnClickListener(this);
        
        // Button for calendar
        button_calendar = (Button) findViewById(R.id.button_calendar);
        button_calendar.setOnClickListener(this);
    }

	public void onClick(View view) {
		switch(view.getId()){
		
		case R.id.button_inviter:
			Intent i = new Intent(this, InviterActivity.class);
			startActivity(i);
			break;
		case R.id.button_calendar:
			Intent c = new Intent(this, CalendarActivity.class);
			startActivity(c);			
			break;
			
		case R.id.button_listshow:
			Intent l = new Intent(this, ListActivity.class);
			startActivity(l);
			break;
		}
	}
}