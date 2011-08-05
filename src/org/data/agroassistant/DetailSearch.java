package org.data.agroassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailSearch extends Activity {
	
	private String fname = "", area = "", crop = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.detail_search);
	    
	    
	    final EditText edt_farmer_dtl_search = (EditText) findViewById(R.id.edt_dtl_search);
	    
	    final RadioGroup rdg_area = (RadioGroup) findViewById(R.id.rdgrp_dtlsearch);
	    final EditText edt_area_dtl_search = (EditText) findViewById(R.id.area_dtl_search);
	    
	    final RadioGroup rdg_crop = (RadioGroup) findViewById(R.id.rdgrp_dtl_csearch);
	    final EditText edt_crop_dtl_search = (EditText) findViewById(R.id.dtl_crop_search);
	    
	    final Button btn_search = (Button) findViewById(R.id.btn_dtl_search);
	    
	    final Intent returnIntent = new Intent();
	    
		btn_search.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				
				
				fname = edt_farmer_dtl_search.getText().toString().trim();
				area  = edt_area_dtl_search.getText().toString().trim();
				crop  = edt_crop_dtl_search.getText().toString().trim();
				
				//ensure that user filled in at least one of the fields
				if (fname.equals("") && area.equals("") && crop.equals("")){
					//Toast.makeText(DetailSearch.this, "Please Enter Search Information in\n" +
					//								  "at least onesome (1) field provided and\n" +
					//								  "select the corresponding type\nwhere applicable", //Toast.LENGTH_LONG).show();
				}else {
					if (fname.equals("") && (area.length() > 1) && crop.equals(""))
					{
						returnIntent.putExtra("selection", "2");
						getArea();
					
					}else if ((fname.length() > 1) && area.equals("") && crop.equals(""))
					{					
						//Toast.makeText(DetailSearch.this, "Farmer Name: " + fname, //Toast.LENGTH_SHORT).show();
						returnIntent.putExtra("selection", "1");
						returnIntent.putExtra("Farmer", fname );
					
					}else if (fname.equals("") && area.equals("") && (crop.length() > 1))
					{					
						returnIntent.putExtra("selection", "3");
						getCrop();
					
					}else if ((fname.length() > 1) && (area.length() > 1) && crop.equals(""))
					{
						returnIntent.putExtra("selection", "4");
						getArea();
						returnIntent.putExtra("Farmer", fname );
						//Toast.makeText(DetailSearch.this, "Farmer Name: " + fname, //Toast.LENGTH_SHORT).show();
										
					}else if (fname.equals("") && (area.length() > 1) && (crop.length() > 1))
					{
						returnIntent.putExtra("selection", "5");
						getArea();
						getCrop();
										
					}else if ((fname.length() > 1) && area.equals("")  && (crop.length() > 1))
					{
						returnIntent.putExtra("selection", "6");
						returnIntent.putExtra("Farmer", fname );
						getCrop();
						//Toast.makeText(DetailSearch.this, "Farmer Name: " + fname, //Toast.LENGTH_SHORT).show();
						
					}else
					{
						returnIntent.putExtra("selection", "7");
						returnIntent.putExtra("Farmer", fname );
						getArea();
						getCrop();
						//Toast.makeText(DetailSearch.this, "Farmer Name: " + fname, //Toast.LENGTH_SHORT).show();
					}
					
					setResult(RESULT_OK,returnIntent);    	
			    	finish();
				}
					
			}
			
			private void getArea(){
				switch(rdg_area.getCheckedRadioButtonId()) {
				case R.id.rdo_parish:
					
					//Toast.makeText(DetailSearch.this, "Parish: " + area, //Toast.LENGTH_SHORT).show();
					returnIntent.putExtra("AreaCol","Parish");
					returnIntent.putExtra("Parish", area );
					
					break;
				case R.id.rdo_extension:
					//Toast.makeText(DetailSearch.this, "Extension: " + area, //Toast.LENGTH_SHORT).show();
					returnIntent.putExtra("AreaCol","Extension");
					returnIntent.putExtra("Extension", area );
					
					break;
				case R.id.rdo_district:
					//Toast.makeText(DetailSearch.this, "District: " + area, //Toast.LENGTH_SHORT).show();
					returnIntent.putExtra("AreaCol","District");
					returnIntent.putExtra("District", area );
					
					break;
				case -1:
					//Toast.makeText(DetailSearch.this, "Please check area to search", //Toast.LENGTH_SHORT).show();
					break;
				}
			}
			
			private void getCrop(){
				switch(rdg_crop.getCheckedRadioButtonId()) {
				case R.id.rdo_cropType:
					
					//Toast.makeText(DetailSearch.this, "Crop Type: " + crop, //Toast.LENGTH_SHORT).show();
					returnIntent.putExtra("CropCol","Crop Type");
					returnIntent.putExtra("Crop Type", crop );
					
					break;
				case R.id.rdo_cropGroup:
					//Toast.makeText(DetailSearch.this, "Crop Group: " + crop, //Toast.LENGTH_SHORT).show();
					returnIntent.putExtra("CropCol","Crop Group");
					returnIntent.putExtra("Crop Group", crop );
					
					break;
				case -1:
					//Toast.makeText(DetailSearch.this, "Please check Crop Group or Crop Type", //Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}
		
}
