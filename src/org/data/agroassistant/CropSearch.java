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

public class CropSearch extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.crop_search);
	    
	    final RadioGroup rdg_crop = (RadioGroup) findViewById(R.id.rdgrp_csearch);
	    final EditText edt_farmer_search = (EditText) findViewById(R.id.edt_crop_search);
	    final Button btn_search = (Button) findViewById(R.id.btn_crop_search);
	    
		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String userInput = "";
				switch(rdg_crop.getCheckedRadioButtonId()) {
					case R.id.rdo_cropType:
						userInput = edt_farmer_search.getText().toString();
						Toast.makeText(CropSearch.this, "Crop type: " + userInput, Toast.LENGTH_SHORT).show();
						break;
					case R.id.rdo_cropGroup:
						Toast.makeText(CropSearch.this, "Crop group: " + userInput, Toast.LENGTH_SHORT).show();
						break;
					case -1:
						Toast.makeText(CropSearch.this, "Please check farmer fame or farmer id", Toast.LENGTH_SHORT).show();
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
