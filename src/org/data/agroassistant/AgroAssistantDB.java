package org.data.agroassistant;

import static android.provider.BaseColumns._ID;
import static org.data.agroassistant.Constants.CROPS_TABLE;
import static org.data.agroassistant.Constants.CROP_ID;
import static org.data.agroassistant.Constants.DATABASE_NAME;
import static org.data.agroassistant.Constants.FARMERS_TABLE;
import static org.data.agroassistant.Constants.FARMER_FNAME;
import static org.data.agroassistant.Constants.FARMER_ID;
import static org.data.agroassistant.Constants.FARMER_LNAME;
import static org.data.agroassistant.Constants.FARMER_SIZE;
import static org.data.agroassistant.Constants.FARMS_TABLE;
import static org.data.agroassistant.Constants.FARM_DISTRICT;
import static org.data.agroassistant.Constants.FARM_EXTENSION;
import static org.data.agroassistant.Constants.FARM_FARMER_ID;
import static org.data.agroassistant.Constants.FARM_ID;
import static org.data.agroassistant.Constants.FARM_LAT;
import static org.data.agroassistant.Constants.FARM_LONG;
import static org.data.agroassistant.Constants.FARM_PARISH;
import static org.data.agroassistant.Constants.FARM_SIZE;
import static org.data.agroassistant.Constants.FROM_CROPS;
import static org.data.agroassistant.Constants.FROM_FARMERS;
import static org.data.agroassistant.Constants.*;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
	
	private static final String CREATE_TABLE_CROPS = "create table " + CROPS_TABLE + " ( " 
	+ _ID + " integer primary key autoincrement, "
	+ CROP_FARM_ID + " integer not null, " 
	+ CROP_GROUP + " text not null, "
	+ CROP_TYPE + " text not null, "
	+ CROP_AREA + " integer not null, "
	+ CROP_COUNT + " integer not null, "
	+ CROP_DATE + " text not null);"; 
		
	private static final int DATABASE_VERSION = 8;
	
	private SQLiteDatabase db;
	
	public AgroAssistantDB(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_TABLE_FARMERS);
			Log.d("AgroAssistant", "Create Farmers table: " + CREATE_TABLE_FARMERS);
			db.execSQL(CREATE_TABLE_FARMS);
			Log.d("AgroAssistant", "Create Farms table: " + CREATE_TABLE_FARMS);
			db.execSQL(CREATE_TABLE_CROPS);
			Log.d("AgroAssistant", "Create Farms table: " + CREATE_TABLE_CROPS);
		} catch (RuntimeException e) {
			Log.d("AgroAssistant", "Unable to create tables: " + CREATE_TABLE_FARMERS);
		}
		
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("AgroAssistant", "Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + FARMERS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + FARMS_TABLE);
		Log.d("AgroAssistant", "Upgrade step: " + "DROP TABLE IF EXISTS " + FARMERS_TABLE);
		onCreate(db);
	}
	
	/*
	 * Userdefined function used to run rawQueries again specific tables
	 */
	public Cursor rawQuery(String tableName, String tableColumns, String queryParams) {
		db = this.getReadableDatabase();
		Cursor cursor = null;
		Log.d("AgroAssistant", "Raw Query: SELECT " + tableColumns + " FROM " + tableName +" WHERE " + queryParams);
		try {
			cursor = db.rawQuery("SELECT "+ tableColumns + " FROM " + tableName +" WHERE " + queryParams, null);
			Log.d("AgroAssistant", "Raw Query Result: Returned " + cursor.getCount() + " record(s)");
		} catch (SQLException e) {
			Log.e("AgroAssistant", "Raw Query Exception: " + e.toString());
		}
		return cursor;
	}
	
	public Cursor farmerRawQuery(String tableName, String tableColumns, String queryParams) {
		db = this.getReadableDatabase();
		Log.d("AgroAssistant", "Raw Query: SELECT " + tableColumns + " FROM " + tableName +" WHERE " + queryParams);
		Cursor cursor = db.rawQuery("SELECT "+ tableColumns + " FROM " + tableName +" WHERE " + queryParams, null);
		Log.d("AgroAssistant", "Raw Query Result: Returned " + cursor.getCount() + " record(s)");
		return cursor;
	}
	
	
	public Cursor getFarmers() {
		db = this.getReadableDatabase();
		Cursor cursor = db.query(FARMERS_TABLE, FROM_FARMERS, null, null, null, null, null);
		//Cursor cursor = db.rawQuery("SELECT _id, farmerid, firstname, lastname, farmersize FROM farmers", null);
		
		Log.d("AgroAssistant", "getFarmers: Cusor contains " + cursor.getCount() + " record(s)");
		return cursor;
	}
	
	public boolean insertFarmer(int id, String firstname, String lastname, String farmersize) {
		db = this.getWritableDatabase();
		//Checks if farmer already exists in the database
		if ((db.query(FARMERS_TABLE, FROM_FARMERS, FARMER_ID + "=" + id, null, null, null, null)).getCount() == 1) {
			Log.d("AgroAssistant", "insertFarmer: Farmer " + firstname + " " + lastname + " already exist in table");
		} else {
			ContentValues values = new ContentValues();
			values.put(FARMER_ID, id);
			values.put(FARMER_FNAME, firstname);
			values.put(FARMER_LNAME, lastname);
			values.put(FARMER_SIZE, farmersize);
			try {
				db.insertOrThrow(FARMERS_TABLE, null, values);
				Log.d("AgroAssistant", "Insert Farmer: " + id + " " + firstname + " " + lastname + " " + farmersize);
				db.close();
			}
			catch (RuntimeException e) {
				db.close();
				Log.e("AgroAssistant","Farmer Insertion Exception: "+e.toString());
				return false;
			}
		}
		return true;
	}
	
	public boolean deleteFarmer(SQLiteDatabase db, Long farmerId) {
		db = this.getWritableDatabase();
		if (db.delete(FARMERS_TABLE, FARMER_ID + '=' + farmerId.toString(), null) > 0) {
			db.close();
			return true;
		} else {
			db.close();
			return false;
		}
	}
	
	public boolean insertFarm(int fid, int pid, int p_size, int latitude, int longtitude, String p_parish, String p_extension, String p_district) {
		db = this.getWritableDatabase();
		//Checks if farm already exists in the database
		if ((db.query(FARMS_TABLE, FROM_FARMS, FARM_ID + "=" + pid, null, null, null, null)).getCount() == 1) {
			Log.d("AgroAssistant", "insertFarm: Farm " + pid + " already exist in table");
		} else {
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
				Log.d("AgroAssistant", "Insert Farm: " + fid + " " + pid + " " + p_size + " " + latitude + " " + longtitude + " " + p_extension + " " + p_district);
				db.close();
			}
			catch (RuntimeException e) {
				db.close();
				Log.e("AgroAssistant","Farm Insertion Exception: "+e.toString());
				return false;
			}
		}
		return true;
	}
	
	public Cursor getFarms() {
		db = this.getReadableDatabase();
		Cursor cursor = db.query(FARMS_TABLE, FROM_FARMS, null, null, null, null, null);
		return cursor;
	}
	
	public boolean deleteFarm(Long farmId) {
		db = this.getWritableDatabase();
		if (db.delete(FARMS_TABLE, FARM_ID + '=' + farmId.toString(), null) > 0) {
			db.close();
			return true;
		} else {
			db.close();
			return false;
		}
	}
	
	public boolean insertCrop(int pid, String group, String type, int area, int count, String date) {
		db = this.getWritableDatabase();
		//Checks if farm already exists in the database
		if ((db.query(CROPS_TABLE, FROM_CROPS, CROP_FARM_ID + "=" + pid, null, null, null, null)).getCount() == 1) {
			Log.d("AgroAssistant", "insertCrop: Crop " + pid + " already exist in table");
		} else {
			ContentValues values = new ContentValues();
			values.put(CROP_FARM_ID, pid);
			values.put(CROP_GROUP, group);
			values.put(CROP_TYPE, type);
			values.put(CROP_AREA, area);
			values.put(CROP_COUNT, count);
			values.put(CROP_DATE, date);
			
			try {
				db.insertOrThrow(CROPS_TABLE, null, values);
				Log.d("AgroAssistant", "Insert Crop: " + pid + " " + " " + type + " " + area + " " + count + " " + date);
				db.close();
			}
			catch (RuntimeException e) {
				db.close();
				Log.e("AgroAssistant","Crop Insertion Exception: "+e.toString());
				return false;
			}
		}
		return true;
	}
	
	public Cursor getCrops() {
		db = this.getReadableDatabase();
		Cursor cursor = db.query(CROPS_TABLE, FROM_CROPS, null, null, null, null, null);
		return cursor;
	}
	
	public boolean deleteCrop(Long cropId) {
		db = this.getWritableDatabase();
		if (db.delete(CROPS_TABLE, _ID + '=' + cropId.toString(), null) > 0) {
			db.close();
			return true;
		} else {
			db.close();
			return false;
		}
	}
}
