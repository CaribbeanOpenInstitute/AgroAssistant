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

public class DetailPriceSearch extends Activity {
	
	private String parish = "", crop = "", price = "", suppStat = "", quality ="", priceMonth = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.price_dtl_search);
	    
	    
	    final EditText edt_parish_dtl_search = (EditText) findViewById(R.id.price_dtl_search_parish );
	    final EditText edt_crop_dtl_search   = (EditText) findViewById(R.id.price_dtl_search_crop   );
	    
	    
	    final RadioGroup rdg_price = (RadioGroup) findViewById(R.id.rdgrp_price_dtlsearch  );
	    final EditText edt_price_dtl_search = (EditText) findViewById(R.id.dtl_price_search);
	    	    
	    
	    final EditText edt_suppStat_dtl_search     = (EditText) findViewById(R.id.price_dtl_search_supp    );
	    final EditText edt_quality_dtl_search      = (EditText) findViewById(R.id.price_dtl_search_quality );
	    final EditText edt_priceMonth_dtl_search   = (EditText) findViewById(R.id.price_dtl_search_pricem  );
	    
	    
	    final Button btn_search = (Button) findViewById(R.id.btn_dtl_price_search);
	    
	    final Intent returnIntent = new Intent();
	    
		btn_search.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				
				
				parish      = edt_parish_dtl_search.getText().toString().trim();
				crop        = edt_crop_dtl_search.getText().toString().trim();
				price       = edt_price_dtl_search.getText().toString().trim();
				suppStat    = edt_suppStat_dtl_search.getText().toString().trim();
				quality     = edt_quality_dtl_search.getText().toString().trim();
				priceMonth  = edt_priceMonth_dtl_search.getText().toString().trim();
				
				//ensure that user filled in at least one of the fields
				if (parish.equals("") && crop.equals("") && price.equals("") && suppStat.equals("") && quality.equals("") && priceMonth.equals("")){
					Toast.makeText(DetailPriceSearch.this, "Please Enter Price Search Information in\n" +
													  "at least one (1) of the fields provided and\n" +
													  "scroll to the \"Search Price\" button at the\n" +
													  " end of the screen", Toast.LENGTH_LONG).show();
				}else {
					
					returnIntent.putExtra("Parish", parish         );
					returnIntent.putExtra("Crop Type", crop        );
					if (price.equals("")){
						returnIntent.putExtra("Price", "" );
					}else{
						getPrice();
					}
					
					returnIntent.putExtra("Supply Status", suppStat);
					returnIntent.putExtra("Quality", quality       );
					returnIntent.putExtra("Price Month", priceMonth);
					
					setResult(RESULT_OK,returnIntent);    	
			    	finish();
				}
					
			}
			
			private void getPrice(){
				switch(rdg_price.getCheckedRadioButtonId()) {
				case R.id.rdo_lower:
					
					Toast.makeText(DetailPriceSearch.this, "Lower Price: " + price, Toast.LENGTH_SHORT).show();
					returnIntent.putExtra("Column","Lower");
					returnIntent.putExtra("Price", price );
					
					break;
				case R.id.rdo_upper:
					Toast.makeText(DetailPriceSearch.this, "Upper Price: " + price, Toast.LENGTH_SHORT).show();
					returnIntent.putExtra("Column","Upper");
					returnIntent.putExtra("Price", price );
					
					break;
				case R.id.rdo_freq:
					Toast.makeText(DetailPriceSearch.this, "Frequent Price: " + price, Toast.LENGTH_SHORT).show();
					returnIntent.putExtra("Column","Frequent");
					returnIntent.putExtra("Price", price);
					
					break;
				case -1:
					Toast.makeText(DetailPriceSearch.this, "Please check price type to search", Toast.LENGTH_SHORT).show();
					break;
				}
			}
			
		});
	}
		
}
