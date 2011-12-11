package org.data.agroassistant; 

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class FarmerSearch extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.farmer_search);
	    
	    //***************Dialog Edits
		Context mContext = getApplicationContext();
	    LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.farmer_search, null);
        new AlertDialog.Builder(mContext)
            .setIcon(R.drawable.ic_farmer)
            .setTitle(R.string.fname_title)
            .setView(textEntryView)
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked OK so do some stuff */
                }
            })
            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked cancel so do some stuff */
                }
            })
            .create();
	    
	    //******************
	    
	    final RadioGroup rdg_farmer = (RadioGroup) findViewById(R.id.rdgrp_fsearch);
	    final EditText edt_farmer_search = (EditText) findViewById(R.id.edt_farmer_search);
	    final Button btn_search = (Button) findViewById(R.id.btn_farmer_search);
        
	    final Intent returnIntent = new Intent();
		
	    btn_search.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				String userInput = edt_farmer_search.getText().toString();
				if ( userInput.equals("") || userInput.length() < 1 ){
					//Toast.makeText(FarmerSearch.this, "Please enter a Farmer ID or Name\nThen select farmer fame or farmer id", //Toast.LENGTH_SHORT).show();
				}else{
					
					switch(rdg_farmer.getCheckedRadioButtonId()) {
						case R.id.rdo_fname:
							//userInput = edt_farmer_search.getText().toString();
							//Toast.makeText(FarmerSearch.this, "Farmer Name: " + userInput, //Toast.LENGTH_SHORT).show();
							returnIntent.putExtra("selection", "1");
							returnIntent.putExtra("column", "Farmer Name");
							returnIntent.putExtra("value", userInput );
							setResult(RESULT_OK,returnIntent);    	
					    	finish();
							break;
						case R.id.rdo_fid:
							//Toast.makeText(FarmerSearch.this, "Farmer ID: " + userInput, //Toast.LENGTH_SHORT).show();
							returnIntent.putExtra("selection", "2");
							returnIntent.putExtra("column", "Farmer ID");
							returnIntent.putExtra("value", userInput );
							setResult(RESULT_OK,returnIntent);    	
					    	finish();
							break;
						case -1:
							//Toast.makeText(FarmerSearch.this, "Please check farmer fame or farmer id " + userInput , //Toast.LENGTH_SHORT).show();
							break;
					}
				}
					
			        
			    	//returnIntent.putExtra("Selected Location",btn_search.getText().toString());	//Data to be returned
			    	
			    	
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
