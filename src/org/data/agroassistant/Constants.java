package org.data.agroassistant;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
	
	public static final String DATABASE_NAME = "agroassistant";
	public static final String QUERY_TABLE= "queries";
	public static final String FARMERS_TABLE= "farmers";
	public static final String FARMS_TABLE= "farms";
	public static final String CROPS_TABLE= "crops";
	public static final String PRICES_TABLE= "prices";
	
	//Columns in the Query table
	public static final String QUERY_DATE = "date";
	public static final String QUERY_URL = "url";
	
	//Columns in the Farmers table
	public static final String FARMER_ID = "farmerid";
	public static final String FARMER_FNAME = "firstname";
	public static final String FARMER_LNAME = "lastname";
	public static final String FARMER_SIZE = "farmersize";
	
	//Columns in the Farms table
	public static final String FARM_ID = "farmid";
	public static final String FARM_FARMER_ID = "farmerid";
	public static final String FARM_SIZE = "propertysize";
	public static final String FARM_PARISH = "parish";
	public static final String FARM_EXTENSION = "extension";
	public static final String FARM_DISTRICT = "district";
	public static final String FARM_LAT = "xcoord";
	public static final String FARM_LONG = "ycoord";
	
	//Columns in the Crops table
	public static final String CROPS_ID = "cropid";
	public static final String CROPS_PROPERTY_ID = "propertyid";
	public static final String CROPS_GROUP = "cropgroup";
	public static final String CROPS_TYPE = "croptype";
	public static final String CROPS_AREA = "croparea";
	public static final String CROPS_COUNT = "cropcount";
	public static final String CROPS_DATE = "cropdate";
	public static final String CROP_LAT = "xcoord";
	public static final String CROP_LONG = "ycoord";
	
}
