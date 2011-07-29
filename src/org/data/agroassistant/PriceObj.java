package org.data.agroassistant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PriceObj{
	
	private String parish      ;
	private String crop_type   ;
	private int    lower_price ;
	private int    upper_price ;
	private int    freq_price  ;
	private String supply_status  ;
	private String quality     ;
	private Date   price_month ;

//************************************************************************************************************	
	public PriceObj(String in_parish, String type, int lprice, int uprice, int fprice, String status, String crop_quality, Date date){
		parish      =  in_parish 	;
		crop_type   =  type      	;
		lower_price =  lprice    	;
		upper_price =  uprice    	;
		freq_price  =  fprice    	;
		supply_status  =  status       ;
		quality     =  crop_quality ;
		price_month =  date			;
		
	}
	
	public PriceObj(String in_parish, String type, int lprice, int uprice, int fprice, String status, String crop_quality){
		parish      =  in_parish 	;
		crop_type   =  type      	;
		lower_price =  lprice    	;
		upper_price =  uprice    	;
		freq_price  =  fprice    	;
		supply_status  =  status       ;
		quality     =  crop_quality ;
		
	}
//************************************************************************************************************
	public String getParish(){
		return parish;
	}
		
	public String getCropType(){
		return crop_type;
	}
	public int getLPrice(){
		return lower_price;
	}
	
	public int getUPrice(){
		return upper_price;
	}
	
	public int getFPrice(){
		return freq_price;
	}
	
	public String getStatus(){
		return supply_status;
	}
	
	public String getQuality(){
		return quality;
	}
	
	public Date getCropDate(){
		return price_month;
	}
	
	public String getCropDateString(){
		return price_month.toString();
	}
	
	
//************************************************************************************************************	
	public void setParish(String in_parish){
		parish = in_parish;
	}
		
	public void settCropType(String type){
		crop_type = type;
	}
	public void setLPrice(int price){
		lower_price = price;
	}
	
	public void setUPrice(int price){
		upper_price = price;
	}
	
	public void setFPrice(int price){
		freq_price = price;
	}
	
	public void setStatus( String status){
		supply_status = status;
	}
	
	public void setQuality(String crop_quality){
		quality = crop_quality;
	}
	
	public void setCropDate(Date date){
		price_month = date;
	}
	
	public void setCropDate(String date){
		try {
			 price_month = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
		}catch(ParseException e){
			price_month = new Date(0,0,0);
		}
		
	}
	
//************************************************************************************************************
	public String toString(){
		String retString  = "" +parish + "," +  crop_type + "," + lower_price + "," + upper_price + "," + freq_price
							+ "," + supply_status + "," + quality + "," + price_month.toGMTString();
		return retString;
	}
}

