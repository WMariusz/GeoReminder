package pl.mariusz.georeminder;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static List<Event> events;
	private static EventsAdapter listAdapter;
	private static ListView lvEvents;
	private EventDbAdapter eventDbAdapter;
	private Cursor eventCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUiElements();
        initListView();
        //initButtonsOnClickListeners();
    }
	
	private void initUiElements() {
		lvEvents = (ListView) findViewById(R.id.eventsList);
	}
	
	private void initListView() {
	    fillListViewData();
	    initListViewOnItemClick();
	}

	private void fillListViewData() {
		eventDbAdapter = new EventDbAdapter(getApplicationContext());
		eventDbAdapter.open();
		getAllTasks();
		listAdapter = new EventsAdapter(this, R.layout.single_event, events);
		lvEvents.setAdapter(listAdapter);
	}

	private void getAllTasks() {
		events = new ArrayList<Event>();
		eventCursor = getAllEntriesFromDb();
		updateTaskList();
	}

	private Cursor getAllEntriesFromDb() {
		eventCursor = eventDbAdapter.getAllEvents();
		if(eventCursor != null) {
			startManagingCursor(eventCursor);
			eventCursor.moveToFirst();
		}
		return eventCursor;
	}

	private void updateTaskList() {
		if(eventCursor != null && eventCursor.moveToFirst()) {
			do {
				int id = eventCursor.getInt(EventDbAdapter.ID_COLUMN);
				String name = eventCursor.getString(EventDbAdapter.NAME_COLUMN);
				double latitude = eventCursor.getDouble(EventDbAdapter.LATITUDE_COLUMN);
				double longitude = eventCursor.getDouble(EventDbAdapter.LONGITUDE_COLUMN);
				String description = eventCursor.getString(EventDbAdapter.DESCRIPTION_COLUMN);
				long date = eventCursor.getLong(EventDbAdapter.DATE_COLUMN);
				events.add(new Event(id, name, latitude, longitude, description, date));
			} while(eventCursor.moveToNext());
		}
	}
	/*
	 * zachowanie listy po kliknieciu na jej element
	*/
	private void initListViewOnItemClick() {
		lvEvents.setOnItemClickListener(new OnItemClickListener() {
	        
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Event event = events.get(position);
	            //eventDbAdapter.updateEvent(event.getId(), event.getName(), event.getLatitude(), event.getLongitude(), event.getDescription());eventDbAdapter.updateEvent(event.getId(), event.getName(), event.getLatitude(), event.getLongitude(), event.getDescription());
	            eventDbAdapter.deleteEvent(event.getId());
	        	updateListViewData();
			}
			
	    });
		lvEvents.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long i) {
				/*
				 * TODO:
				 * 	- edycja eventu
				 */
				return false;
			}
			
		});
	}
	
	private void updateListViewData() {
		eventCursor.requery();
	    events.clear();
	    updateTaskList();
	    listAdapter.notifyDataSetChanged();
	}
	@Override
	protected void onDestroy() {
		if(eventDbAdapter != null)
			eventDbAdapter.close();
		super.onDestroy();
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
	    	break;
	    case R.id.location:
	    	this.startActivity(new Intent(MainActivity.this, LocationView.class));
	    	break;
	    case R.id.add:
	    	this.startActivity(new Intent(MainActivity.this, AddEvent.class));
	    	break;
	    case R.id.map:
	    	this.startActivity(new Intent(MainActivity.this, EventsMap.class));
	    	break;
	    default:	
	    }

	    return true;
	}	
}
