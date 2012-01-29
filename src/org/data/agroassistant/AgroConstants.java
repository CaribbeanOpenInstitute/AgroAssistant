package org.data.agroassistant;

public interface AgroConstants {
	
	/*
	 * Query URLs
	 */
	public static final String FARMS_QUERY_URL = "http://www.data.org.jm/api/farms.xml";
	public static final String CROPS_QUERY_URL = "http://www.data.org.jm/api/crops.xml";
	public static final String PRICES_QUERY_URL = "http://www.data.org.jm/api/prices.xml";  
	
	/*
	 * Top Level Search Types
	 */
	public static final int FARMERS_SEARCH = 0;
	public static final int FARMS_SEARCH = 1;
	public static final int CROPS_SEARCH = 2;
	public static final int PRICES_SEARCH = 3;
	
	/*
	 * Agro Detail Search Types
	 */
	public static final int FNAME_SEARCH = 0;
	public static final int FID_SEARCH = 1;
	public static final int PROPERTY_SEARCH = 2;
	public static final int PARISH_SEARCH = 3;
	public static final int EXTENSION_SEARCH = 4;
	public static final int DISTRICT_SEARCH = 5;
	public static final int LOCATION_SEARCH = 6;
	public static final int CROP_SEARCH = 7;
	public static final int CROP_PARISH_SEARCH = 8;
	public static final int CROP_PRICE_SEARCH = 9;
	public static final int DETAILED_SEARCH = 10;
	
	/*
	 * Other Constants
	 */
	public static final String FARMER_NAME = "farmerName";
	public static final String SEARCH_PARAMS = "searchParams";
	public static final String SEARCH_TYPE = "searchType";

}
