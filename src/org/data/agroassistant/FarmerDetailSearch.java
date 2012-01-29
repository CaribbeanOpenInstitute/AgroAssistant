package org.data.agroassistant;

import static org.data.agroassistant.AgroConstants.PARISH_SEARCH;
import static org.data.agroassistant.AgroConstants.SEARCH_PARAMS;
import static org.data.agroassistant.AgroConstants.*;
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

public class FarmerDetailSearch extends Activity {
	private ContentValues searchInput;
	private String fname = "", area = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		searchInput = new ContentValues();

		btn_search.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				fname = edt_farmer_dtl_search.getText().toString().trim();
				area = edt_area_dtl_search.getText().toString().trim();

				// ensure that user filled in at least one of the fields
				if (fname.equals("") && area.equals("")) {
					Toast.makeText(
							FarmerDetailSearch.this,
							"Please Enter Search Information in\n"
									+ "at least one(1) field provided and\n"
									+ "select the corresponding type\nwhere applicable",
							Toast.LENGTH_LONG).show();
				} else {
					if (fname.equals("") && (area.length() > 1)) {
						returnIntent.putExtra("selection", "2");
						getArea();

					} else if ((fname.length() > 1) && area.equals("")) {
						Toast.makeText(FarmerDetailSearch.this,
								"Farmer Name: " + fname, Toast.LENGTH_SHORT)
								.show();
					} else {
						getArea();
						fname.trim();
						String[] name = fname.split(" ");
						if (name.length == 1 ) {
							searchInput.put(FARMER_LNAME, name[0]);
						} else {
							searchInput.put(FARMER_FNAME, name[0]);
							searchInput.put(FARMER_LNAME, name[1]);
						}
						Toast.makeText(FarmerDetailSearch.this,
								"Farmer Name: " + fname, Toast.LENGTH_SHORT)
								.show();

					}
					
					returnIntent.putExtra(SEARCH_TYPE, DETAILED_SEARCH);
					returnIntent.putExtra(SEARCH_PARAMS, searchInput);
					setResult(RESULT_OK, returnIntent);
					finish();
				}
			}

			private void getArea() {
				switch (rdg_area.getCheckedRadioButtonId()) {
				case R.id.rdo_parish:

					Toast.makeText(FarmerDetailSearch.this, "Parish: " + area,
							Toast.LENGTH_SHORT).show();
					searchInput.put(FARM_PARISH, area);
					break;
				case R.id.rdo_extension:
					Toast.makeText(FarmerDetailSearch.this,
							"Extension: " + area, Toast.LENGTH_SHORT).show();
					searchInput.put(FARM_EXTENSION, area);

					break;
				case R.id.rdo_district:
					Toast.makeText(FarmerDetailSearch.this,
							"District: " + area, Toast.LENGTH_SHORT).show();
					searchInput.put(FARM_DISTRICT, area);
					break;
				case -1:
					Toast.makeText(FarmerDetailSearch.this,
							"Please check area to search", Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		});
	}

}
