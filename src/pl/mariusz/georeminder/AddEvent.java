package pl.mariusz.georeminder;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class AddEvent extends FragmentActivity implements OnDateSetListener {
	
	private EditText name;
	private Button date;
	private EditText description;
	private EditText location;
	private EventDbAdapter eventDbAdapter;
	private Date d; 
	private Event event;
	private boolean editMode = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		initUiElements();
		fillElements();
	}

	private void initUiElements() {
		name = (EditText) findViewById(R.id.eventName);
		description = (EditText) findViewById(R.id.description);
		date = (Button) findViewById(R.id.pick_date);
		location = (EditText) findViewById(R.id.location);
		
		
		
		//if( name.getText().toString().length() == 0 )
		//    name.setError( getResources().getString(R.string.eventNameMissing) );
		//if( description.getText().toString().length() == 0 )
		//	description.setError( getResources().getString(R.id.eventDescriptionMissing) );
		//if( location.getText().toString().length() == 0 )
		//	location.setError( "First name is required!" );
	}
	
	private void fillElements() {
		Intent i = getIntent();
		int _id = i.getIntExtra("id", -1);
		if(_id >= 0) {
			editMode = true;
			String _name = i.getStringExtra("name");
			long _latitude = i.getLongExtra("latitude", 0);
			long _longitude = i.getLongExtra("longitude", 0);
			String _description = i.getStringExtra("description");
			long _date = i.getLongExtra("date", 0);
			int b = i.getIntExtra("completed", 0);
			boolean _completed = b !=0;
			event = new Event(_id, _name, _latitude, _longitude, _description, _date, _completed);
			d = new Date(event.getDate());
			name.setText(event.getName());
			description.setText(event.getDescription());
		} else {
			d = new Date();
			event = new Event();
		}
		int flags = 0;
		flags |= DateUtils.FORMAT_SHOW_DATE;
        flags |= DateUtils.FORMAT_SHOW_YEAR;
		String str = DateUtils.formatDateTime(this, d.getTime(), flags);
		date.setText(str);
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
		if(validate()) {
			event.setName(name.getText().toString());
			event.setLatitude(0.0);
			event.setLongitude(0.0);
			event.setDescription(description.getText().toString());
			event.setDate(d.getTime());
			
			eventDbAdapter = new EventDbAdapter(getApplicationContext());
			eventDbAdapter.open();
			if(editMode) {
				eventDbAdapter.updateEvent(event);
			} else {
				eventDbAdapter.insertEvent(event.getName(), event.getLatitude(), event.getLongitude(), event.getDescription(), event.getDate());
			}
			eventDbAdapter.close();
			return true;
		} else {
			return false;
		}
	}
	
	private boolean validate() {
		// TODO Auto-generated method stub
		return true;
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
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		d.setDate(day);
		d.setMonth(month);
		d.setYear(year);
		d = c.getTime();
		
		int flags = 0;
		flags |= DateUtils.FORMAT_SHOW_DATE;
        flags |= DateUtils.FORMAT_SHOW_YEAR;
		String str = DateUtils.formatDateTime(this, d.getTime(), flags);
		date.setText(str);
	}
}
