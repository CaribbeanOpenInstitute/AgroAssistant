package org.data.agroassistant;

import java.util.Date;

public class CropObj{
	
	private int    property_id ;
	private String crop_group  ;
	private String crop_type   ;
	private int    crop_area   ;
	private int    crop_count  ;
	private Date   crop_date   ;

//************************************************************************************************************	
	public CropObj(int id, String group, String type, int area, int count, Date date){
		property_id = id;
		crop_group 	= group;
		crop_type  	= type;
		crop_area   = area;
		crop_count  = count;
		crop_date   = date;
		
	}
	
	public CropObj(int id, String group, String type, int area, int count){
		property_id = id;
		crop_group 	= group;
		crop_type  	= type;
		crop_area   = area;
		crop_count  = count;
		
	}
//************************************************************************************************************
	public int getPropertyID(){
		return property_id;
	}
	
	public String getGroup(){
		return crop_group;
	}
	
	public String getType(){
		return crop_type;
	}
	public int getCropArea(){
		return crop_area;
	}
	
	public int getCropCount(){
		return crop_count;
	}
	
	public Date getCropDate(){
		return crop_date;
	}
	
	public String getCropDateString(){
		return crop_date.toString();
	}
	
	
//************************************************************************************************************	
	public void setPropertyID(int pid){
		property_id = pid;
	}
	
	public void setGroup(String group){
		crop_group = group;
	}
	
	public void setType(String type){
		crop_type = type;
	}
	
	public void setCropArea(int area){
		crop_area = area;
	}
	
	public void setCropCount(int count){
		crop_count = count;
	}
	
	public void setCropdate(Date date){
		crop_date = date;
	}
	
//************************************************************************************************************
	@Override
	public String toString(){
		String retString  = "" +property_id + "," + crop_group + "," + crop_type + "," + crop_area + "," + crop_count
							+ "," + crop_date.toGMTString();
		return retString;
	}
}

