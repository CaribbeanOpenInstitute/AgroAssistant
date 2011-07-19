package org.data.agroassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FarmerDetailSearch extends Activity {
	
	private String fname = "", area = "";
	
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
	    final View separator = findViewById(R.id.cropview);
	    
	    rdg_crop.setVisibility(8);
	    edt_crop_dtl_search.setVisibility(8);
	    separator.setVisibility(8);
	    
	    
	    final Button btn_search = (Button) findViewById(R.id.btn_dtl_search);
	    
	    final Intent returnIntent = new Intent();
	    
		btn_search.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				
				
				fname = edt_farmer_dtl_search.getText().toString().trim();
				area  = edt_area_dtl_search.getText().toString().trim();
				
				//ensure that user filled in at least one of the fields
				if (fname.equals("") && area.equals("")){
					Toast.makeText(FarmerDetailSearch.this, "Please Enter Search Information in\n" +
													  "at least one(1) field provided and\n" +
													  "select the corresponding type\nwhere applicable", Toast.LENGTH_LONG).show();
				}else {
					if (fname.equals("") && (area.length() > 1) )
					{
						returnIntent.putExtra("selection", "2");
						getArea();
					
					}else if ((fname.length() > 1) && area.equals(""))
					{					
						Toast.makeText(FarmerDetailSearch.this, "Farmer Name: " + fname, Toast.LENGTH_SHORT).show();
						returnIntent.putExtra("selection", "1");
						returnIntent.putExtra("Farmer", fname );
					
					}else
					{
						returnIntent.putExtra("selection", "3");
						getArea();
						returnIntent.putExtra("Farmer", fname );
						Toast.makeText(FarmerDetailSearch.this, "Farmer Name: " + fname, Toast.LENGTH_SHORT).show();
										
					}
					
					setResult(RESULT_OK,returnIntent);    	
			    	finish();
				}
					
			}
			
			private void getArea(){
				switch(rdg_area.getCheckedRadioButtonId()) {
				case R.id.rdo_parish:
					
					Toast.makeText(FarmerDetailSearch.this, "Parish: " + area, Toast.LENGTH_SHORT).show();
					returnIntent.putExtra("AreaCol","Parish");
					returnIntent.putExtra("Parish", area );
					
					break;
				case R.id.rdo_extension:
					Toast.makeText(FarmerDetailSearch.this, "Extension: " + area, Toast.LENGTH_SHORT).show();
					returnIntent.putExtra("AreaCol","Extension");
					returnIntent.putExtra("Extension", area );
					
					break;
				case R.id.rdo_district:
					Toast.makeText(FarmerDetailSearch.this, "District: " + area, Toast.LENGTH_SHORT).show();
					returnIntent.putExtra("AreaCol","District");
					returnIntent.putExtra("District", area );
					
					break;
				case -1:
					Toast.makeText(FarmerDetailSearch.this, "Please check area to search", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}
		
}
