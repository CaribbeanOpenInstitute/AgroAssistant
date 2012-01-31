package org.data.agroassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CropParishSearch extends Activity {
	private String parish;
	private final Intent returnIntent = new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.crop_parish_search);
	    
	    //final RadioGroup rdg_crop = (RadioGroup) findViewById(R.id.rdgrp_csearch);
	    final AutoCompleteTextView edt_crop_search = (AutoCompleteTextView) findViewById(R.id.edt_crop_search);
	    final Button btn_search = (Button) findViewById(R.id.btn_crop_search);
	    
	    //Logic for the Parishes Spinner
	    Spinner spinner = (Spinner) findViewById(R.id.spinner);
	    ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
	            this, R.array.parishes_array, android.R.layout.simple_spinner_item);
	    spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(spAdapter);
	    spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
	    
	    //Crop Edit box Auto complete logic 
	    AgroAssistantDB agroDB = new AgroAssistantDB(this);
	    String[] atxtCrop = agroDB.getCrop();
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.autocomplete_list, atxtCrop);
	    edt_crop_search.setAdapter(adapter);
	    
	    
	    
		btn_search.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				String userInput = edt_crop_search.getText().toString();
				if ( userInput.equals("") || userInput.length() < 1 ){
					Toast.makeText(CropParishSearch.this, "Please enter a Crop group or name", Toast.LENGTH_SHORT).show();
				}else{
					//Gets default string for Parish selection is user has not changed Parish
					/*try {
						if(!returnIntent.hasExtra("Parish"))
							returnIntent.putExtra("Parish", "Kingston");
					} catch (Exception e) {
						Log.e("AgroAssistant", "CropParishSearch > Search Button onClick > Parish Validation " + e.toString() );
					}
					*/
					returnIntent.putExtra("Parish", parish);
					returnIntent.putExtra("selection", "1");
					returnIntent.putExtra("column", "CropType");
					returnIntent.putExtra("Crop Type", userInput );
					setResult(RESULT_OK,returnIntent);    	
			    	finish();
					
			    	/*
					switch(rdg_crop.getCheckedRadioButtonId()) {
						case R.id.rdo_cropType:
							//userInput = edt_crop_search.getText().toString();
							//Toast.makeText(CropParishSearch.this, "Crop Type: " + userInput, Toast.LENGTH_SHORT).show();
							returnIntent.putExtra("selection", "1");
							returnIntent.putExtra("column", "CropType");
							returnIntent.putExtra("Crop Type", userInput );
							setResult(RESULT_OK,returnIntent);    	
					    	finish();
							break;
						case R.id.rdo_cropGroup:
							//Toast.makeText(CropParishSearch.this, "Crop Group: " + userInput, Toast.LENGTH_SHORT).show();
							returnIntent.putExtra("selection", "2");
							returnIntent.putExtra("column", "Crop Group");
							returnIntent.putExtra("Crop Group", userInput );
							setResult(RESULT_OK,returnIntent);    	
					    	finish();
							break;
						case -1:
							Toast.makeText(CropParishSearch.this, "Please check farmer fame or farmer id", Toast.LENGTH_SHORT).show();
							break;
					}
					*/
						
				}
			}
		});
	}
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
            View view, int pos, long id) {
        	parish = parent.getItemAtPosition(pos).toString();
        	Log.d("AgroAssistant", "Changed Parish to " + parish);
          //Toast.makeText(parent.getContext(), "The parish is " + parish, Toast.LENGTH_LONG).show();
          Intent returnIntent = new Intent();
          
          //returnIntent.putExtra("Parish", parish);
          //setResult(RESULT_OK,returnIntent);    	
          //finish();
          
        }

        @SuppressWarnings("rawtypes")
		public void onNothingSelected(AdapterView parent) {
          // Do nothing.
        }
	}

}
