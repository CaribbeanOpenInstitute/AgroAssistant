package org.data.agroassistant;

import static org.data.agroassistant.AgroConstants.*;
import static org.data.agroassistant.AgroConstants.SEARCH_PARAMS;
import static org.data.agroassistant.AgroConstants.SEARCH_TYPE;
import static org.data.agroassistant.DBConstants.*;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CropSearch extends Activity {
	private ContentValues searchInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.crop_search);
	    
	    final RadioGroup rdg_crop = (RadioGroup) findViewById(R.id.rdgrp_csearch);
	    //final EditText edt_farmer_search = (EditText) findViewById(R.id.edt_crop_search);
	    final AutoCompleteTextView edt_crop_search = (AutoCompleteTextView) findViewById(R.id.edt_crop_search);
	    final Button btn_search = (Button) findViewById(R.id.btn_crop_search);
	    
	    //AutoComplete for Crops
	    AgroApplication agroApp = ((AgroApplication)getApplication());
	    String[] saCrop = agroApp.agroData.getCrop();
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.autocomplete_list, saCrop);
	    edt_crop_search.setAdapter(adapter);
	    
	    final Intent returnIntent = new Intent();
	    searchInput = new ContentValues();
	    
		btn_search.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String userInput = "";
				userInput = edt_crop_search.getText().toString();
				switch(rdg_crop.getCheckedRadioButtonId()) {
					case R.id.rdo_cropType:
						searchInput.put(CROP_TYPE, userInput);
						returnIntent.putExtra(SEARCH_TYPE, CROP_SEARCH);
						returnIntent.putExtra(SEARCH_PARAMS, searchInput);
						
						setResult(RESULT_OK,returnIntent);    	
				    	finish();
						break;
					case R.id.rdo_cropGroup:
						searchInput.put(CROP_GROUP, userInput);
						returnIntent.putExtra(SEARCH_TYPE, CROP_SEARCH);
						returnIntent.putExtra(SEARCH_PARAMS, searchInput);
						
						setResult(RESULT_OK,returnIntent);    	
				    	finish();
						break;
					case -1:
						Toast.makeText(CropSearch.this, "Please check farmer fame or farmer id", Toast.LENGTH_SHORT).show();
						break;
				}
					
			}
		});
	}

}
