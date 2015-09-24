package pl.mariusz.georeminder;

import android.content.Context;
import android.graphics.Paint;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {

	private int resource;

	public EventsAdapter(Context context, int textViewResourceId, List<Event> objects) {
		super(context, textViewResourceId, objects);
        resource = textViewResourceId;
	}
	
	private void strikeThroughtText(TextView tv) {
	    tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	}
	
	private void unStrikeThroughtText(TextView tv) {
	    tv.setPaintFlags(tv.getPaintFlags() &~ Paint.STRIKE_THRU_TEXT_FLAG);
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
	    TextView tvEventDistance = (TextView) eventView.findViewById(R.id.distanceInfo);
	    
	    tvEventName.setText(eventName);
	    tvEventDesctiption.setText(eventDescription);
	    int flags = DateUtils.FORMAT_SHOW_DATE;
		String str = android.text.format.DateUtils.formatDateTime(this.getContext(), eventDate, flags);
		tvEventDate.setText(str);
		tvEventDistance.setText(String.valueOf(event.getId()));
		
		if(event.isCompleted()) {
			strikeThroughtText(tvEventName);
			strikeThroughtText(tvEventDesctiption);
			strikeThroughtText(tvEventDate);
			//strikeThroughtText(tvEventRange);
		} else {
			unStrikeThroughtText(tvEventName);
			unStrikeThroughtText(tvEventDesctiption);
			unStrikeThroughtText(tvEventDate);
			//unStrikeThroughtText(tvEventRange);
		}
		
		return eventView;
	}
}
