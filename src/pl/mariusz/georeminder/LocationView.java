package pl.mariusz.georeminder;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LocationView extends Activity implements LocationListener {

	private TextView latitudeField;
	private TextView longitudeField;
	private TextView providerField;
	private TextView targetLatField;
	private TextView targetLngField;
	private TextView distance;
	private LocationManager locationManager;
	private String provider;
	private Location targetLocation;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_location);
	    
	    latitudeField = (TextView) findViewById(R.id.TextView02);
	    longitudeField = (TextView) findViewById(R.id.TextView04);
	    providerField = (TextView) findViewById(R.id.provider);
	    targetLatField = (TextView) findViewById(R.id.targetLat);
	    targetLngField = (TextView) findViewById(R.id.targetLng);
	    distance = (TextView) findViewById(R.id.distance);
	    
	    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	    
	    Criteria criteria = new Criteria();
	    //criteria.setAccuracy(Criteria.ACCURACY_COARSE);
	    
	    //provider = locationManager.getBestProvider(criteria, false);
	    provider = LocationManager.GPS_PROVIDER;
	    if(provider == null) provider = LocationManager.GPS_PROVIDER;
	    providerField.setText(provider);
	    
	    try { 
	    	Location location = locationManager.getLastKnownLocation(provider);
	    	latitudeField.setText(String.valueOf(location.getLatitude()));
	    	longitudeField.setText(String.valueOf(location.getLongitude()));
	    	targetLocation = locationManager.getLastKnownLocation(provider);
	    	distance.setText(String.valueOf(location.distanceTo(targetLocation)));
	    } catch (Exception e) {
	    	providerField.setText(e.getMessage());
	    }
	    
	}
	
	/* Request updates at startup */
	@Override
	protected void onResume() {
	    super.onResume();
	    locationManager.requestLocationUpdates(provider, 400, 1, this);
	}

	/* Remove the locationlistener updates when Activity is paused
	@Override
	protected void onPause() {
	    super.onPause();
	    locationManager.removeUpdates(this);
	}
	*/
	
	public void onLocationChanged(Location location) {
	    latitudeField.setText(String.valueOf(location.getLatitude()));
	    longitudeField.setText(String.valueOf(location.getLongitude()));
	    float dist = location.distanceTo(targetLocation);
	    distance.setText(String.valueOf(dist));
	    if(dist >= 100) this.sendNotification(new String());
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	    // TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
	    Toast.makeText(this, "Enabled new provider " + provider,
	        Toast.LENGTH_SHORT).show();

	}

	public void onProviderDisabled(String provider) {
	    Toast.makeText(this, "Disabled provider " + provider,
	        Toast.LENGTH_SHORT).show();
	}
	
	public void onClick(View view) {
		try {
			targetLocation = locationManager.getLastKnownLocation(provider);
			targetLngField.setText(String.valueOf(targetLocation.getLongitude()));
			targetLatField.setText(String.valueOf(targetLocation.getLatitude()));
			distance.setText(String.valueOf(targetLocation.distanceTo(targetLocation)));
		} catch(Exception e) { }
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
	    	this.startActivity(new Intent(LocationView.this, MainActivity.class));
	    	break;
	    case R.id.location:
	    	break;
	    case R.id.add:
	    	this.startActivity(new Intent(LocationView.this, AddEvent.class));
	    	break;
	    case R.id.map:
	    	this.startActivity(new Intent(LocationView.this, EventsMap.class));
	    	break;
	    default:	
	    }

	    return true;
	}
	
	private void sendNotification(String str) {
		// Intent construct
		Intent it = new Intent(LocationView.this, LocationView.class);
		PendingIntent pi = PendingIntent.getActivity(LocationView.this, 0, it, 0);
			
		// Notification construct
		int icon = R.drawable.ic_launcher;
		CharSequence ticketText = "U ran away";
		long when = System.currentTimeMillis();
		Notification noti = new Notification(icon, ticketText, when);
				
		noti.setLatestEventInfo(LocationView.this, "GeoRemind", "You ran away from target location", pi);
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		noti.flags |= Notification.DEFAULT_SOUND;
				
		// Show up notification
		NotificationManager nm = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		nm.notify(0, noti);
	}
		
}