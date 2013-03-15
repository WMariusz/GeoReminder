package pl.mariusz.georeminder;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class EventsMap extends MapActivity {

	private static final String DEBUG_TAG = "EventMap";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events_map);
		
		MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
	    EventsOverlay itemizedoverlay = new EventsOverlay(drawable, this);
	    
	    GeoPoint point = new GeoPoint(19240000,-99120000);
	    OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
	    
	    itemizedoverlay.addOverlay(overlayitem);
	    mapOverlays.add(itemizedoverlay);
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

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
