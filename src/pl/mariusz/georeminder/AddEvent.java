package pl.mariusz.georeminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class AddEvent extends FragmentActivity implements OnDateSetListener {
	
	private TextView name;
	private Button date;
	private TextView description;
	private EventDbAdapter eventDbAdapter;
	private Date d; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		d = new Date();
        //int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        //int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        //int year = Calendar.getInstance().get(Calendar.YEAR);

		int flags = 0;
		flags |= DateUtils.FORMAT_SHOW_DATE;
        flags |= DateUtils.FORMAT_SHOW_YEAR;
		String str = DateUtils.formatDateTime(this, d.getTime(), flags);
		
		name = (TextView) findViewById(R.id.eventName);
		description = (TextView) findViewById(R.id.description);
		date = (Button) findViewById(R.id.pick_date);
		date.setText(str);
		description.setText(d.toString());
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
	
	@SuppressLint("SimpleDateFormat")
	private boolean createEvent() {
		Event event = new Event();		
		
		try {
			event.setName(name.getText().toString());
			event.setLatitude(0.0);
			event.setLongitude(0.0);
			event.setDescription(description.getText().toString());
			event.setDate(d.getTime());
		} catch (Exception e) {	
			Toast.makeText(getApplicationContext(), "coœ siê zjeba³o", Toast.LENGTH_LONG).show();
		}
		
		if(event.validate()) {
			eventDbAdapter = new EventDbAdapter(getApplicationContext());
			eventDbAdapter.open();
			try {
				eventDbAdapter.insertEvent(event.getName(), event.getLatitude(), event.getLongitude(), event.getDescription(), event.getDate());
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "coœ siê jeb³o", Toast.LENGTH_LONG).show();
			}
			eventDbAdapter.close();
			return true;
		} else {
			Resources res = getResources();
			Toast.makeText(getApplicationContext(), res.getString(R.string.addEventError), Toast.LENGTH_LONG).show();
			return false;
		}
	}
	/*
	private void clearCompletedTasks(){
	    if(todoCursor != null && todoCursor.moveToFirst()) {
	        do {
	            if(todoCursor.getInt(TodoDbAdapter.COMPLETED_COLUMN) == 1) {
	                long id = todoCursor.getLong(TodoDbAdapter.ID_COLUMN);
	                todoDbAdapter.deleteTodo(id);
	            }
	        } while (todoCursor.moveToNext());
	    }
	    updateListViewData();
	}
	*/
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.pick_date:
			DialogFragment dateFragment = new DatePickerFragment();
			dateFragment.show(this.getSupportFragmentManager(), "datePicker");
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

	public void onDateSet(DatePicker view, int year, int month, int day) {
		d.setDate(day);
		d.setMonth(month);
		d.setYear(year);
		int flags = 0;
		flags |= DateUtils.FORMAT_SHOW_DATE;
        flags |= DateUtils.FORMAT_SHOW_YEAR;
		String str = DateUtils.formatDateTime(this, d.getTime(), flags);
		description.setText(String.valueOf(d.getYear()));
		date.setText(str);
	}
}
