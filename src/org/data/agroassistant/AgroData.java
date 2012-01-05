package org.data.agroassistant;

import static android.provider.BaseColumns._ID;
import static org.data.agroassistant.DBConstants.CROPS_TABLE;
import static org.data.agroassistant.DBConstants.CROP_AREA;
import static org.data.agroassistant.DBConstants.CROP_COUNT;
import static org.data.agroassistant.DBConstants.CROP_DATE;
import static org.data.agroassistant.DBConstants.CROP_FARM_ID;
import static org.data.agroassistant.DBConstants.CROP_GROUP;
import static org.data.agroassistant.DBConstants.CROP_TYPE;
import static org.data.agroassistant.DBConstants.DATABASE_NAME;
import static org.data.agroassistant.DBConstants.FARMERS_TABLE;
import static org.data.agroassistant.DBConstants.FARMER_FNAME;
import static org.data.agroassistant.DBConstants.FARMER_ID;
import static org.data.agroassistant.DBConstants.FARMER_LNAME;
import static org.data.agroassistant.DBConstants.FARMER_SIZE;
import static org.data.agroassistant.DBConstants.FARMS_TABLE;
import static org.data.agroassistant.DBConstants.FARM_DISTRICT;
import static org.data.agroassistant.DBConstants.FARM_EXTENSION;
import static org.data.agroassistant.DBConstants.FARM_FARMER_ID;
import static org.data.agroassistant.DBConstants.FARM_ID;
import static org.data.agroassistant.DBConstants.FARM_LAT;
import static org.data.agroassistant.DBConstants.FARM_LONG;
import static org.data.agroassistant.DBConstants.FARM_PARISH;
import static org.data.agroassistant.DBConstants.FARM_SIZE;
import static org.data.agroassistant.DBConstants.FROM_FARMERS;
import static org.data.agroassistant.DBConstants.PRICES_TABLE;
import static org.data.agroassistant.DBConstants.PRICE_CROPTYPE;
import static org.data.agroassistant.DBConstants.PRICE_FPRICE;
import static org.data.agroassistant.DBConstants.PRICE_LPRICE;
import static org.data.agroassistant.DBConstants.PRICE_MONTH;
import static org.data.agroassistant.DBConstants.PRICE_PARISH;
import static org.data.agroassistant.DBConstants.PRICE_QUALITY;
import static org.data.agroassistant.DBConstants.PRICE_SUPPLY;
import static org.data.agroassistant.DBConstants.PRICE_UPRICE;
import static org.data.agroassistant.DBConstants.QUERY_PARAMS;
import static org.data.agroassistant.DBConstants.QUERY_TABLE;
import static org.data.agroassistant.DBConstants.QUERY_URI;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class AgroData {
	private static final String TAG = AgroData.class.getSimpleName();
	
	Context context;
	DbHelper dbHelper;
	
	public AgroData(Context context){
		this.context = context;
		dbHelper = new DbHelper(context);
	}
	
	/**
	 * @param table Name of table record to be inserted into
	 * @param values Name-value pairs 
	 */
	public void insert(String table, ContentValues values) {
		
		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		//Log.d(TAG, String.format("Current API level: %d. Froyo Version build: %d", currentApiVersion, android.os.Build.VERSION_CODES.FROYO));
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		//db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		//Log.d(TAG, String.format("Record %s inserted into table %s", values.toString(),table));
		
		if (currentApiVersion >= android.os.Build.VERSION_CODES.FROYO){ //Phone versions >= froyo
			Log.d(TAG, String.format("Current API level: >= Froyo "));
			db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_IGNORE);
			Log.d(TAG, String.format("Record %s inserted into table %s", values.toString(),table));
		} else {//Phones running SDK < Froyo (2.2)
			Cursor cursor = db.query(table, null, _ID + "=" +  values.get(_ID), null, null, null, null);
			//Checks if farmer already exists in the database
			if (cursor.getCount() == 1) {
				Log.d(TAG, String.format("Record already exists in table %s", table));
			} else {
				try {
					db.insertOrThrow(table, null, values);
					Log.d(TAG, String.format("Inserting into table %s", table));
				}
				catch (RuntimeException e) {
					Log.e(TAG,"Farmer Insertion Exception: "+e.toString());
				}
			}
		}
		db.close();
	}
	
	/**
	 * 
	 * @param table
	 * @param queryParams 
	 * @return TRUE for DB Call & FALSE for API call
	 */
	public boolean queryExists(int table, String queryParams) {
		return false;
	}
	
	private class DbHelper extends SQLiteOpenHelper {
		
		private static final String CREATE_TABLE_FARMERS = "create table " + FARMERS_TABLE + " ( " 
			+ _ID + " integer primary key autoincrement, "
			+ FARMER_ID + " int not null, "
			+ FARMER_FNAME + " text not null, "
			+ FARMER_LNAME + " text not null, "
			+ FARMER_SIZE + " text not null);";

		private static final String CREATE_TABLE_FARMS = "create table " + FARMS_TABLE + " ( " 
			+ _ID + " integer primary key autoincrement, "
			+ FARM_ID + " integer not null, " 
			+ FARM_FARMER_ID + " integer not null, "
			+ FARM_SIZE + " text not null, "
			+ FARM_PARISH + " text not null, "
			+ FARM_EXTENSION + " text not null, "  
			+ FARM_DISTRICT + " text not null, "
			+ FARM_LAT + " double not null, "
			+ FARM_LONG + " double not null);";
			
		private static final String CREATE_TABLE_CROPS = "create table " + CROPS_TABLE + " ( " 
			+ _ID + " integer primary key autoincrement, "
			+ CROP_FARM_ID + " integer not null, " 
			+ CROP_GROUP + " text not null, "
			+ CROP_TYPE + " text not null, "
			+ CROP_AREA + " integer not null, "
			+ CROP_COUNT + " integer not null, "
			+ CROP_DATE + " text not null);";
			
		private static final String CREATE_TABLE_QUERY = "create table " + QUERY_TABLE + " ( " 
			+ _ID + " integer primary key autoincrement, "
			+ QUERY_URI + " text not null, " 
			+ QUERY_PARAMS + " text not null);";
			
		private static final String CREATE_TABLE_PRICES = "create table " + PRICES_TABLE + " ( " 
			+ _ID + " integer primary key autoincrement, "
			+ PRICE_PARISH + " text not null, " 
			+ PRICE_CROPTYPE + " text not null, "
			+ PRICE_LPRICE + " integer not null, "
			+ PRICE_UPRICE + " integer not null, "
			+ PRICE_FPRICE + " integer not null, "
			+ PRICE_SUPPLY + " text not null, "
			+ PRICE_QUALITY + " text not null, "
			+ PRICE_MONTH + " text not null); ";
			//+ PRICE_LAT + " double not null, "
			//+ PRICE_LONG + " double not null);";
		
		private static final int DB_VERSION = 27;

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(CREATE_TABLE_FARMERS);
				Log.d(TAG, "Create FARMERS table: " + CREATE_TABLE_FARMERS);
				db.execSQL(CREATE_TABLE_FARMS);
				Log.d(TAG, "Create FARMS table: " + CREATE_TABLE_FARMS);
				db.execSQL(CREATE_TABLE_CROPS);
				Log.d(TAG, "Create CROPS table: " + CREATE_TABLE_CROPS);
				db.execSQL(CREATE_TABLE_QUERY);
				Log.d(TAG, "Create QUERIES table: " + CREATE_TABLE_QUERY);
				db.execSQL(CREATE_TABLE_PRICES);
				Log.d(TAG, "Create PRICES table: " + CREATE_TABLE_PRICES);
			} catch (RuntimeException e) {
				Log.d("AgroAssistant", "Unable to create tables: ");
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion == 2) {
				String sqlUpgrade1 = ""; //Using Alter table
				db.execSQL(sqlUpgrade1);
				this.onUpgrade(db, oldVersion++, newVersion);
			}
			
			Log.w(TAG, String.format("Upgrading database from version %d  to %d which will destroy all old data", oldVersion, newVersion));
			try {
				db.execSQL("DROP TABLE IF EXISTS " + FARMERS_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + FARMS_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + CROPS_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + QUERY_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + PRICES_TABLE);
			} catch (SQLException e) {
				Log.d(TAG, "Upgrade step: " + "Unable to DROP TABLES");
			}
			
			this.onCreate(db);
		}
		
	}
}
