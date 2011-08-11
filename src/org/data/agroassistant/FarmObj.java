package org.data.agroassistant;

public class FarmObj{
	
	private int farmer_id;
	private int property_id;
	private int property_size;
	private double farm_lat;
	private double farm_long;
	private String parish, extension, district;
	

//************************************************************************************************************	
	public FarmObj(int fid, int pid, int p_size, double latitude, double longitude, String p_parish, String p_extension, String p_district){
		farmer_id     = fid         ;
		property_id   = pid         ;
		property_size = p_size      ;
		farm_lat      = latitude    ;
		farm_long     = longitude   ;
		parish        = p_parish    ;
		extension	  = p_extension ;
		district      = p_district  ;
		
	}
	
//************************************************************************************************************
	public int getFarmerID(){
		return farmer_id;
	}
	
	public int getPropertyID(){
		return property_id;
	}
	
	public int getPropertySize(){
		return property_size;
	}
	
	public double getLatitude(){
		return farm_lat;
	}
	
	public double getLongitude(){
		return farm_long;
	}
	
	public String getParish(){
		return parish;
	}
	
	public String getExtension(){
		return extension;
	}
	public String getDistrict(){
		return district;
	}
//************************************************************************************************************	
	public void setFarmerID(int fid){
		farmer_id = fid;
	}
	
	public void setPropertyID(int pid){
		property_id = pid;
	}
	
	public void setPropertySize(int size){
		property_size = size;
	}
	
	public void setLatitude(int lat){
		farm_lat = lat;
	}
	
	public void setLongitude(int longitude){
		farm_long = longitude;
	}
	public void setLatLong(int lat, int longitude){
		farm_lat = lat;
		farm_long = longitude;
	}
	
	public int setLatLong(String lat, String longitude){
		int num_lat, num_long;
		try {
			num_lat  = Integer.parseInt(lat);
			num_long = Integer.parseInt(longitude);
			
			farm_lat = num_lat;
			farm_long = num_long;
			
			return 1;
			
		}catch(NumberFormatException mismatch){
			return 0;
		}
		
	}
	
	public void setParish(String Parish){
		parish = Parish;
	}
	
	public void setExtension(String ext){
		extension = ext;
	}
	
	public void setDistrict(String dist){
		district = dist;
	}
	
//************************************************************************************************************
	public String toString(){
		String retString  = "" + farmer_id + "," + property_id + "," + property_size + "," + farm_lat + "" +farm_long
						    + "," + parish + "," + extension + "," + district;
		return retString;
	}
}

