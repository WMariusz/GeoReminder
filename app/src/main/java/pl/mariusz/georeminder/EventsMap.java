package pl.mariusz.georeminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;


public class EventsMap extends AppCompatActivity {
	private static final String DEBUG_TAG = "EventMap";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events_map);
		ButterKnife.bind(this);
	}

	@Override
	protected void onDestroy() {
		ButterKnife.unbind(this);
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
	    	this.startActivity(new Intent(EventsMap.this, MainActivity.class));
	    	break;
	    case R.id.location:
	    	this.startActivity(new Intent(EventsMap.this, LocationView.class));
	    	break;
	    case R.id.add:
	    	this.startActivity(new Intent(EventsMap.this, AddEvent.class));
	    	break;
	    case R.id.map:
	    	break;
	    default:
	    }

	    return true;
	}
}
