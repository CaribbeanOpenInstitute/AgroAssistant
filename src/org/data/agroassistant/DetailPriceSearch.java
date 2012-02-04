package org.data.agroassistant;

import static org.data.agroassistant.AgroConstants.DETAILED_SEARCH;
import static org.data.agroassistant.AgroConstants.PARISH_SEARCH;
import static org.data.agroassistant.AgroConstants.SEARCH_PARAMS;
import static org.data.agroassistant.AgroConstants.SEARCH_TYPE;
import static org.data.agroassistant.DBConstants.*;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DetailPriceSearch extends Activity {
	private ContentValues searchInput;
	private String parish = "", cropType = "", price = "", suppStat = "", quality ="", priceMonth = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
			public void onClick(View v) {
				searchInput = new ContentValues();
				
				parish      = edt_parish_dtl_search.getText().toString().trim();
				cropType    = edt_crop_dtl_search.getText().toString().trim();
				price       = edt_price_dtl_search.getText().toString().trim();
				suppStat    = edt_suppStat_dtl_search.getText().toString().trim();
				quality     = edt_quality_dtl_search.getText().toString().trim();
				priceMonth  = edt_priceMonth_dtl_search.getText().toString().trim();
				
				//ensure that user filled in at least one of the fields
				if (parish.equals("") && cropType.equals("") && price.equals("") && suppStat.equals("") && quality.equals("") && priceMonth.equals("")){
					Toast.makeText(DetailPriceSearch.this, "Please Enter Price Search Information in\n" +
													  "at least one (1) of the fields provided and\n" +
													  "scroll to the \"Search Price\" button at the\n" +
													  " end of the screen", Toast.LENGTH_LONG).show();
				} else {
					if (parish.length() > 1){
						searchInput.put(PRICE_PARISH, parish);
					}
					if (cropType.length() > 1){
						searchInput.put(PRICE_CROPTYPE, cropType);
					}
					if (price.length() > 1){
						getPrice();
					}
					if (suppStat.length() > 1){
						searchInput.put(PRICE_SUPPLY, suppStat);
					}
					if (quality.length() > 1){
						searchInput.put(PRICE_QUALITY, quality);
					}
					if (priceMonth.length() > 1){
						searchInput.put(PRICE_MONTH, priceMonth);
					}
					
					returnIntent.putExtra(SEARCH_TYPE, DETAILED_SEARCH);
					returnIntent.putExtra(SEARCH_PARAMS, searchInput);
					setResult(RESULT_OK,returnIntent);    	
			    	finish();
				}
			}
			
			private void getPrice(){
				switch(rdg_price.getCheckedRadioButtonId()) {
				case R.id.rdo_lower:
					searchInput.put(PRICE_LPRICE, price);
					break;
				case R.id.rdo_upper:
					searchInput.put(PRICE_UPRICE, price);
					break;
				case R.id.rdo_freq:
					searchInput.put(PRICE_FPRICE, price);
					break;
				case -1:
					Toast.makeText(DetailPriceSearch.this, "Please check price type to search", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}
}
