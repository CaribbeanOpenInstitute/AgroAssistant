package org.data.agroassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AreaSearch extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.area_search);
	    
	    final RadioGroup rdg_area = (RadioGroup) findViewById(R.id.rdgrp_asearch);
	    final EditText edt_farmer_search = (EditText) findViewById(R.id.edt_area_search);
	    final Button btn_search = (Button) findViewById(R.id.btn_area_search);
	    
		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String userInput = "";
				switch(rdg_area.getCheckedRadioButtonId()) {
					case R.id.rdo_parish:
						userInput = edt_farmer_search.getText().toString();
						Toast.makeText(AreaSearch.this, "Parish: " + userInput, Toast.LENGTH_SHORT).show();
						break;
					case R.id.rdo_extension:
						Toast.makeText(AreaSearch.this, "Extension: " + userInput, Toast.LENGTH_SHORT).show();
						break;
					case R.id.rdo_district:
						Toast.makeText(AreaSearch.this, "District: " + userInput, Toast.LENGTH_SHORT).show();
						break;
					case -1:
						Toast.makeText(AreaSearch.this, "Please check area to search", Toast.LENGTH_SHORT).show();
						break;
				}
					
			        //Returning query to farmer activity
			        //Intent returnIntent = new Intent();
			    	//returnIntent.putExtra("Selected Location",btn_search.getText().toString());	//Data to be returned
			    	//setResult(RESULT_OK,returnIntent);    	
			    	//finish();
			    	
					//final Intent farmerDataIntent = new Intent(APIImportDemo.this, FarmerData.class);
					//final Bundle b = new Bundle();
	
					//b.putString("farmerId", txtSearch.getText().toString());
					//b.putInt("operation", 1);
	
					//farmerDataIntent.putExtras(b);
					//APIImportDemo.this.startActivity(farmerDataIntent);
			}
		});
	}
}
