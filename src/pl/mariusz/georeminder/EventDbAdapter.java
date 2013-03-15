package pl.mariusz.georeminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EventDbAdapter {
	private static final String DEBUG_TAG = "SqLiteEventManager";
	
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "database.db";
	private static final String DB_EVENT_TABLE = "events"; // events table
	
	public static final String ID_KEY = "id";
	public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
	public static final int ID_COLUMN = 0;
	public static final String NAME_KEY = "name";
	public static final String NAME_OPTIONS = "TEXT NOT NULL";
	public static final int NAME_COLUMN = 1;
	public static final String LATITUDE_KEY = "latitude";
	public static final String LATITUDE_OPTIONS = "NOT NULL";
	public static final int LATITUDE_COLUMN = 2;
	public static final String LONGITUDE_KEY = "longitude";
	public static final String LONGITUDE_OPTIONS = "NOT NULL";
	public static final int LONGITUDE_COLUMN = 3;
	public static final String DESCRIPTION_KEY = "description";
	public static final String DESCRIPTION_OPTIONS = "TEXT NOT NULL";
	public static final int DESCRIPTION_COLUMN = 4;
	public static final String DATE_KEY = "date";
    public static final String DATE_OPTIONS = "INTEGER";
    public static final int DATE_COLUMN = 5;
	public static final String COMPLETED_KEY = "completed";
    public static final String COMPLETED_OPTIONS = "INTEGER DEFAULT 0";
    public static final int COMPLETED_COLUMN = 6;
	
	private static final String DB_CREATE_EVENT_TABLE =
			"CREATE TABLE " + DB_EVENT_TABLE + "( " +
			ID_KEY + " " + ID_OPTIONS + ", " +
			NAME_KEY + " " + DESCRIPTION_OPTIONS + ", " +
			LATITUDE_KEY + " " + LATITUDE_OPTIONS + ", " +
			LONGITUDE_KEY + " " + LONGITUDE_OPTIONS + ", " +
			DESCRIPTION_KEY  + " " + DESCRIPTION_OPTIONS + ", " +
			DATE_KEY + " " + DATE_OPTIONS + ", " +
			COMPLETED_KEY + " " + COMPLETED_OPTIONS +
			");";
	private static final String DROP_EVENT_TABLE =
			"DROP TABLE IF EXISTS " + DB_EVENT_TABLE;
	
	private SQLiteDatabase db;
	private Context context;
	private DatabaseHelper dbHelper;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE_EVENT_TABLE);

			Log.d(DEBUG_TAG, "Database creating...");
			Log.d(DEBUG_TAG, "Table " + DB_EVENT_TABLE + " ver." + DB_VERSION + " created");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DROP_EVENT_TABLE);

			Log.d(DEBUG_TAG, "Database updating...");
			Log.d(DEBUG_TAG, "Table " + DB_EVENT_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
			Log.d(DEBUG_TAG, "All data is lost.");

			onCreate(db);
		}
	}

	public EventDbAdapter(Context context) {
		this.context = context;
	}
	
	public EventDbAdapter open(){
		dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLException e) {
			db = dbHelper.getReadableDatabase();
		}
		return this;
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public long insertEvent(String name, double latitude, double longitude, String description, long date) {
		ContentValues newEventValues = new ContentValues();
		newEventValues.put(NAME_KEY, name);
		newEventValues.put(LATITUDE_KEY, latitude);
		newEventValues.put(LONGITUDE_KEY, longitude);
		newEventValues.put(DESCRIPTION_KEY, description);
		newEventValues.put(DATE_KEY, date);
		return db.insert(DB_EVENT_TABLE, null, newEventValues);
	}

	public boolean updateEvent(Event event) {
		int id = event.getId();
		String name = event.getName();
		double latitude = event.getLatitude();
		double longitude = event.getLongitude();
		String description = event.getDescription();
		long date = event.getDate();
		boolean completed = event.isCompleted();
		return updateEvent(id, name, latitude, longitude, description, date, completed);
	}

	public boolean updateEvent(int id, String name, double latitude, double longitude, String description, long date, boolean completed) {
		String where = ID_KEY + "=" + id;
		ContentValues updateEventValues = new ContentValues();
		updateEventValues.put(NAME_KEY, name);
		updateEventValues.put(LATITUDE_KEY, latitude);
		updateEventValues.put(LONGITUDE_KEY, longitude);
		updateEventValues.put(DESCRIPTION_KEY, description);
		return db.update(DB_EVENT_TABLE, updateEventValues, where, null) > 0;
	}

	public boolean deleteEvent(int id){
		String where = ID_KEY + "=" + id;
		return db.delete(DB_EVENT_TABLE, where, null) > 0;
	}

	public Cursor getAllEvents() {
		String[] columns = {ID_KEY, NAME_KEY, LATITUDE_KEY, LONGITUDE_KEY, DESCRIPTION_KEY, DATE_KEY, COMPLETED_KEY};
		return db.query(DB_EVENT_TABLE, columns, null, null, null, null, null);
	}

	public Event getEvent(int id) {
		String[] columns = {ID_KEY, NAME_KEY, LATITUDE_KEY, LONGITUDE_KEY, DESCRIPTION_KEY, DATE_KEY, COMPLETED_KEY};
		String where = ID_KEY + "=" + id;
		Cursor cursor = db.query(DB_EVENT_TABLE, columns, where, null, null, null, null);
		Event event = null;
		if(cursor != null && cursor.moveToFirst()) {
			String name = cursor.getString(NAME_COLUMN);
			double latitude = cursor.getDouble(LATITUDE_COLUMN);
			double longitude = cursor.getDouble(LONGITUDE_COLUMN);
			String description = cursor.getString(DESCRIPTION_COLUMN);
			long date = cursor.getLong(DATE_COLUMN);
			boolean completed = (cursor.getInt(COMPLETED_COLUMN) != 0);
			event = new Event(name, latitude, longitude, description, date, completed);
		}
		return event;
	}
}
