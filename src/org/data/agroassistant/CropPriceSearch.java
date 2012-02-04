package org.data.agroassistant;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CropPriceSearch extends Activity {
	private ContentValues searchInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.crop_search);
	    
	    final RadioGroup rdg_crop = (RadioGroup) findViewById(R.id.rdgrp_csearch);
	    final AutoCompleteTextView edt_crop_search = (AutoCompleteTextView) findViewById(R.id.edt_crop_search);
	    final Button btn_search = (Button) findViewById(R.id.btn_crop_search);
	    
	    AgroApplication agroApp = ((AgroApplication) getApplication());
	    String[] saCrop = agroApp.agroData.getCrop();
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.autocomplete_list, saCrop);
	    edt_crop_search.setAdapter(adapter);
	    
	    final Intent returnIntent = new Intent();
	    
		btn_search.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String userInput = "";
				switch(rdg_crop.getCheckedRadioButtonId()) {
					case R.id.rdo_cropType:
						userInput = edt_crop_search.getText().toString();
						Toast.makeText(CropPriceSearch.this, "Crop Type: " + userInput, Toast.LENGTH_SHORT).show();
						returnIntent.putExtra("selection", "1");
						returnIntent.putExtra("column", "Crop Type");
						returnIntent.putExtra("value", userInput );
						setResult(RESULT_OK,returnIntent);    	
				    	finish();
						break;
					case R.id.rdo_cropGroup:
						Toast.makeText(CropPriceSearch.this, "Crop Group: " + userInput, Toast.LENGTH_SHORT).show();
						returnIntent.putExtra("selection", "1");
						returnIntent.putExtra("column", "Crop Group");
						returnIntent.putExtra("value", userInput );
						setResult(RESULT_OK,returnIntent);    	
				    	finish();
						break;
					case -1:
						Toast.makeText(CropPriceSearch.this, "Please check farmer fame or farmer id", Toast.LENGTH_SHORT).show();
						break;
				}
					
			}
		});
	}

}
