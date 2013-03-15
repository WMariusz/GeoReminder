package pl.mariusz.georeminder;

import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.text.format.DateUtils;
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
	    long eventDate = event.getDate();
	    
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
	    
	    tvEventName.setText(eventName);
	    tvEventDesctiption.setText(eventDescription);

	    
		int flags = DateUtils.FORMAT_SHOW_DATE;
		String str = android.text.format.DateUtils.formatDateTime(this.getContext(), eventDate, flags);
		tvEventDate.setText(str);
		
		return eventView;
	}
}
