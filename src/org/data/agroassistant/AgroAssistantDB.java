package org.data.agroassistant;

import static android.provider.BaseColumns._ID;
import static org.data.agroassistant.Constants.*;

import java.net.URL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AgroAssistantDB extends SQLiteOpenHelper {
	
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
		+ FARM_LAT + " long not null, "
		+ FARM_LONG + " long not null);"; 
		
	private static final String[] FROM_FARMERS = {_ID, FARMER_ID, FARMER_FNAME, FARMER_LNAME, FARMER_SIZE};
	private static final String[] FROM_FARMS = {_ID, FARM_ID, FARM_FARMER_ID, FARM_SIZE, FARM_PARISH, FARM_EXTENSION, FARM_DISTRICT, FARM_LAT, FARM_LONG};
	
	private static final int DATABASE_VERSION = 1;
	
	private SQLiteDatabase db;
	
	public AgroAssistantDB(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase dbl) {
		try {
			dbl.execSQL(CREATE_TABLE_FARMERS);
			Log.d("CaribeReader", "Create Farmers table: " + CREATE_TABLE_FARMERS);
			dbl.execSQL(CREATE_TABLE_FARMS);
			Log.d("AgroAssistant", "Create Farms table: " + CREATE_TABLE_FARMS);
		} catch (RuntimeException e) {
			Log.d("AgroAssistant", "Unable to create tables: " + CREATE_TABLE_FARMERS + CREATE_TABLE_FARMS);
		}
		
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase dbl, int oldVersion, int newVersion) {
		Log.w("CaribeReader", "Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		dbl.execSQL("DROP TABLE IF EXISTS " + FARMERS_TABLE);
		dbl.execSQL("DROP TABLE IF EXISTS " + FARMS_TABLE);
		Log.d("CaribeReader", "Upgrade step: " + "DROP TABLE IF EXISTS " + FARMERS_TABLE + FARMS_TABLE);
		onCreate(dbl);
	}
	
	public Cursor getFarmers() {
		db = this.getReadableDatabase();
		Cursor cursor = db.query(FARMERS_TABLE, FROM_FARMERS, null, null, null, null, null);
		return cursor;
	}
	
	public boolean insertFarmer(int id, String firstname, String lastname, String farmersize) {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FARMER_ID, id);
		values.put(FARMER_FNAME, firstname);
		values.put(FARMER_LNAME, lastname);
		values.put(FARMER_SIZE, farmersize);
		try {
			db.insertOrThrow(FARMERS_TABLE, null, values);
			db.close();
			return true;
		}
		catch (RuntimeException e) {
			db.close();
			Log.e("AgroAssistant","Agroassistant: "+e.toString());
			return false;
		}
	}
	
	public boolean deleteFarmer(Long farmerId) {
		db = this.getWritableDatabase();
		if (db.delete(FARMERS_TABLE, _ID + '=' + farmerId.toString(), null) > 0) {
			db.close();
			return true;
		} else {
			db.close();
			return false;
		}
	}
	
	public boolean insertFarm(int fid, int pid, int p_size, int latitude, int longtitude, String p_parish, String p_extension, String p_district) {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FARM_ID, pid);
		values.put(FARM_FARMER_ID, fid);
		values.put(FARM_SIZE, p_size);
		values.put(FARM_LAT, latitude);
		values.put(FARM_LONG, longtitude);
		values.put(FARM_PARISH, p_parish);
		values.put(FARM_EXTENSION, p_extension);
		values.put(FARM_DISTRICT, p_district);
		
		try {
			db.insertOrThrow(FARMS_TABLE, null, values);
			db.close();
			return true;
		}
		catch (RuntimeException e) {
			db.close();
			Log.e("AgroAssistant","Agroassistant: "+e.toString());
			return false;
		}
	}
	
	public Cursor getFarms() {
		db = this.getReadableDatabase();
		Cursor cursor = db.query(FARMS_TABLE, FROM_FARMS, null, null, null, null, null);
		return cursor;
	}
	
	public boolean deleteFarm(Long farmId) {
		db = this.getWritableDatabase();
		if (db.delete(FARMS_TABLE, _ID + '=' + farmId.toString(), null) > 0) {
			db.close();
			return true;
		} else {
			db.close();
			return false;
		}
	}
}
