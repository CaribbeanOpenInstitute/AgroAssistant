package org.data.agroassistant;

public class FarmerObj{
	
	private int farmer_id;
	private String fName;
	private String lName;
	private String farmer_size;

//************************************************************************************************************	
	public FarmerObj(int id, String firstname, String lastname, String farmersize){
		farmer_id   = id;
		fName       = firstname;
		lName       = lastname;
		farmer_size = farmersize;
		
	}
	
	/*public FarmerObj(int id, String firstname, String lastname){
		farmer_id   = id;
		fName       = firstname;
		lName       = lastname;
		farmer_size = "";
	}*/
//************************************************************************************************************
	public int getFarmerID(){
		return farmer_id;
	}
	
	public String getFarmerFName(){
		return fName;
	}
	
	public String getFarmerLName(){
		return lName;
	}
	public String getFarmerSize(){
		return farmer_size;
	}
//************************************************************************************************************	
	public void setFarmerID(int fid){
		farmer_id = fid;
	}
	
	public void setFarmerFName(String firstname){
		fName = firstname;
	}
	
	public void setFarmerLName(String lastname){
		lName = lastname;
	}
	
	public void setFarmersize(String farmersize){
		farmer_size = farmersize;
	}
	
//************************************************************************************************************
	@Override
	public String toString(){
		String retString  = "" +farmer_id + "," + fName +"" + lName + "" +farmer_size;
		return retString;
	}
}

