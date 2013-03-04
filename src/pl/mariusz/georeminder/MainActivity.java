package pl.mariusz.georeminder;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static List<Event> events;
	private static TextView tw;
	private static EventsAdapter eventAdapter;
	private static ListView lvEvents;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(this.events==null) events = new ArrayList<Event>();
		
		tw = (TextView) findViewById(R.id.amount);
		tw.setText(String.valueOf(events.size()));
		lvEvents = (ListView) findViewById(R.id.eventsList);
		
		eventAdapter = new EventsAdapter(this, R.layout.single_event, events);
		
		lvEvents.setAdapter(eventAdapter);
		
		eventAdapter.notifyDataSetChanged();
		
	}
	
	public static void addEvent(Event event) {
		if(events == null) {
			events = new ArrayList<Event>();
		}
		events.add(event);
		tw.setText(String.valueOf(events.size()));
		eventAdapter.notifyDataSetChanged();
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
