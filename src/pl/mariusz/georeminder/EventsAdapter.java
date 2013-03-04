package pl.mariusz.georeminder;

import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventsAdapter extends ArrayAdapter<Event> {

	private int resource;

	public EventsAdapter(Context context, int textViewResourceId, List<Event> objects) {
		super(context, textViewResourceId, objects);
        resource = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    RelativeLayout eventView;
	    Event event = getItem(position);
	    
	    String eventName = event.getName();
	    String eventDescription = event.getDescription();
	    Date eventDate = event.getDate();
	    
	    if(convertView == null) {
	        eventView = new RelativeLayout(getContext());
	        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        inflater.inflate(resource, eventView, true);
	    } else {
	        eventView = (RelativeLayout)convertView;
	    }
	    
	    TextView tvEventName = (TextView) eventView.findViewById(R.id.nameInfo);
	    TextView tvEventDesctiption = (TextView) eventView.findViewById(R.id.descriptionInfo);
	    TextView tvEventDate = (TextView) eventView.findViewById(R.id.dateInfo);
	    TextView tvEventTime = (TextView) eventView.findViewById(R.id.timeInfo);
	    
	    tvEventName.setText(eventName);
	    tvEventDesctiption.setText(eventDescription);
	    tvEventDate.setText(eventDate.getDay()+"/"+eventDate.getMonth()+"/"+eventDate.getYear());
	    tvEventTime.setText(eventDate.getHours()+":"+eventDate.getMinutes());
	    
	    return eventView;
	}
}
