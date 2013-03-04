package pl.mariusz.georeminder;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddEvent extends FragmentActivity implements OnDateSetListener, OnTimeSetListener {
	
	private Button time;
	private Button date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
		time = (Button) findViewById(R.id.pick_time);
		time.setText(hour+":"+minute);
		date = (Button) findViewById(R.id.pick_date);
		date.setText(day+"/"+month+"/"+year);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId()) {
	    case R.id.main:
	    	this.startActivity(new Intent(AddEvent.this, MainActivity.class));
	    	break;
	    case R.id.location:
	    	this.startActivity(new Intent(AddEvent.this, LocationView.class));
	    	break;
	    case R.id.add:
	    	break;
	    case R.id.map:
	    	this.startActivity(new Intent(AddEvent.this, EventsMap.class));
	    	break;
	    default:	
	    }

	    return true;
	}
	
	private boolean createEvent() {
		TextView name = (TextView) findViewById(R.id.eventName);
		Button date = (Button) findViewById(R.id.pick_date);
		Button time = (Button) findViewById(R.id.pick_time);
		//TextView location = (TextView) findViewById(R.id.location);
		TextView description = (TextView) findViewById(R.id.description);
		Event event = new Event();		
		
		try {
			event.setName(name.getText().toString());
			
			// TO DO get location from google map
			//event.setLocation();
			
			event.setDescription(description.getText().toString());
		} catch (Exception e) {	
			Toast.makeText(getApplicationContext(), "coœ siê zjeba³o", Toast.LENGTH_LONG).show();
		}
			
			
		Date d = new Date();
		String dateStr = (String) date.getText();
		String[] strTab = dateStr.split("/");
		
		d.setDate(Integer.valueOf(strTab[0]));
		d.setMonth(Integer.valueOf(strTab[1]));
		d.setYear(Integer.valueOf(strTab[2]));
		
		String timeStr = (String) time.getText();
		strTab = timeStr.split(":");
		d.setHours(Integer.valueOf(strTab[0]));
		d.setMinutes(Integer.valueOf(strTab[1]));
		event.setDate(d);
		
		if(event.validate()) {
			MainActivity.addEvent(event);
			return true;
		} else {
			Resources res = getResources();
			Toast.makeText(getApplicationContext(), res.getString(R.string.addEventError), Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.pick_date:
			DialogFragment dateFragment = new DatePickerFragment();
			dateFragment.show(this.getSupportFragmentManager(), "datePicker");
			break;
		case R.id.pick_time:
			DialogFragment timeFragment = new TimePickerFragment();
			timeFragment.show(this.getSupportFragmentManager(), "timePicker");
		    break;
		case R.id.location:
			this.startActivity(new Intent(AddEvent.this, EventsMap.class));
			break;
		case R.id.mapButton:
			this.startActivity(new Intent(AddEvent.this, EventsMap.class));
			break;
		case R.id.cancelAddEvent:
			this.finish();
			break;
		case R.id.acceptAddEvent:
			if(this.createEvent()) {
				this.startActivity(new Intent(AddEvent.this, MainActivity.class));
			}
	    	break;
		default:
		}
	}
	//===============================================================================
	public static class TimePickerFragment extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), (OnTimeSetListener)getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
		}
	}

	public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
		time.setText(arg1+":"+arg2);
		
	}
	//===============================================================================
	public static class DatePickerFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), (OnDateSetListener)getActivity(), year, month, day);
		}

	}

	public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		date.setText(arg3+"/"+arg2+"/"+arg1);
		
	}
}
