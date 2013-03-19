package pl.mariusz.georeminder;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {

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
	
	private void updateListViewData() {
		eventCursor.requery();
	    events.clear();
	    updateTaskList();
	    listAdapter.notifyDataSetChanged();
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
				boolean completed = eventCursor.getInt(EventDbAdapter.COMPLETED_COLUMN) != 0;
				events.add(new Event(id, name, latitude, longitude, description, date, completed));
			} while(eventCursor.moveToNext());
		}
	}
	/**
	 * zachowanie listy po kliknieciu na jej element
	*/
	private void initListViewOnItemClick() {
		lvEvents.setOnItemClickListener(new OnItemClickListener() {
	        
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Event event = events.get(position);
	        	//eventDbAdapter.deleteEvent(event.getId());
	        	if(event.isCompleted()) {
	        		eventDbAdapter.updateEvent(event.getId(), event.getName(), event.getLatitude(), event.getLongitude(), 
	        				event.getDescription(), event.getDate(), false);
	        	} else {
	        		eventDbAdapter.updateEvent(event.getId(), event.getName(), event.getLatitude(), event.getLongitude(), 
	        				event.getDescription(), event.getDate(), true);
	        	}
	            updateListViewData();
			}
			
	    });
		lvEvents.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long i) {
				/*
				 * TODO:
				 * 	- edycja eventu
				 * 	- usuwanie
				 */
				DialogFragment df = new EventMenu();
				Bundle args = new Bundle();
		        args.putInt("pos", events.get(position).getId());
		        df.setArguments(args);
				df.show(getSupportFragmentManager(), "eventMenu");
				return false;
			}
			
		});
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
	    	break;
	    }

	    return true;
	}
	@SuppressLint("ValidFragment")
	public class EventMenu extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final int id = getArguments().getInt("pos");
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    builder.setItems(R.array.eventMenu, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int which) {
		            	   switch(which) {
		            	   case 0: // Edit
		            		   Event e = new Event(eventDbAdapter.getEvent(id));
		            		   Intent i = new Intent(MainActivity.this, AddEvent.class);
		            		   i.putExtra("id", e.getId());
		            		   i.putExtra("name", e.getName());
		            		   i.putExtra("latitude", e.getLatitude());
		            		   i.putExtra("longitude", e.getLongitude());
		            		   i.putExtra("description", e.getDescription());
		            		   i.putExtra("date", e.getDate());
		            		   i.putExtra("completed", e.isCompleted());
		            		   
		            		   startActivity(i);
		            		   
		            		   //Toast.makeText(getApplicationContext(), "wybra³eœ opcjê edit elementu "+id, Toast.LENGTH_LONG).show();
		            		   //updateListViewData();
		            		   break;
		            	   case 1: // Delete
		            		   eventDbAdapter.deleteEvent(id);
		            		   updateListViewData();
		            		   break;
		            	   default:
		            		   break;
		               }
		           }
		    });
		    return builder.create();
		}
	}
}
