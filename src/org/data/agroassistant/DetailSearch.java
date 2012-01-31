package org.data.agroassistant;

import static org.data.agroassistant.AgroConstants.DETAILED_SEARCH;
import static org.data.agroassistant.AgroConstants.SEARCH_PARAMS;
import static org.data.agroassistant.AgroConstants.SEARCH_TYPE;
import static org.data.agroassistant.DBConstants.*;
import static org.data.agroassistant.DBConstants.FARM_EXTENSION;
import static org.data.agroassistant.DBConstants.FARM_PARISH;
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

public class DetailSearch extends Activity {
	private ContentValues searchInput;
	private String fname = "", area = "", crop = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_search);

		final EditText edt_farmer_dtl_search = (EditText) findViewById(R.id.edt_dtl_search);

		final RadioGroup rdg_area = (RadioGroup) findViewById(R.id.rdgrp_dtlsearch);
		final EditText edt_area_dtl_search = (EditText) findViewById(R.id.area_dtl_search);

		final RadioGroup rdg_crop = (RadioGroup) findViewById(R.id.rdgrp_dtl_csearch);
		final EditText edt_crop_dtl_search = (EditText) findViewById(R.id.dtl_crop_search);

		final Button btn_search = (Button) findViewById(R.id.btn_dtl_search);

		final Intent returnIntent = new Intent();
		searchInput = new ContentValues();

		btn_search.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				fname = edt_farmer_dtl_search.getText().toString().trim();
				area = edt_area_dtl_search.getText().toString().trim();
				crop = edt_crop_dtl_search.getText().toString().trim();

				if (fname.equals("") && area.equals("") && crop.equals("")) {
					Toast.makeText(
							DetailSearch.this,
							"Please Enter Search Information in\n"
									+ "at least onesome (1) field provided and\n"
									+ "select the corresponding type\nwhere applicable",
							Toast.LENGTH_SHORT).show();
				} else {
					if (fname.equals("") && (area.length() > 1)
							&& crop.equals("")) {
						returnIntent.putExtra("selection", "2");
						getArea();

					} else if ((fname.length() > 1) && area.equals("")
							&& crop.equals("")) {
						// Toast.makeText(DetailSearch.this, "Farmer Name: " +
						// fname, //Toast.LENGTH_SHORT).show();
						/*returnIntent.putExtra("selection", "1");
						returnIntent.putExtra("Farmer", fname);*/
						getName();

					} else if (fname.equals("") && area.equals("")
							&& (crop.length() > 1)) {
						//returnIntent.putExtra("selection", "3");
						getCrop();

					} else if ((fname.length() > 1) && (area.length() > 1)
							&& crop.equals("")) {
						//returnIntent.putExtra("selection", "4");
						getArea();
						/*returnIntent.putExtra("Farmer", fname);*/
						// Toast.makeText(DetailSearch.this, "Farmer Name: " +
						// fname, //Toast.LENGTH_SHORT).show();
						getName();

					} else if (fname.equals("") && (area.length() > 1)
							&& (crop.length() > 1)) {
						returnIntent.putExtra("selection", "5");
						getArea();
						getCrop();

					} else if ((fname.length() > 1) && area.equals("")
							&& (crop.length() > 1)) {
						/*returnIntent.putExtra("selection", "6");
						returnIntent.putExtra("Farmer", fname);*/
						getName();
						getCrop();
						// Toast.makeText(DetailSearch.this, "Farmer Name: " +
						// fname, //Toast.LENGTH_SHORT).show();

					} else {
						/*returnIntent.putExtra("selection", "7");
						returnIntent.putExtra("Farmer", fname);*/
						getName();
						getArea();
						getCrop();
						// Toast.makeText(DetailSearch.this, "Farmer Name: " +
						// fname, //Toast.LENGTH_SHORT).show();
					}
					
					returnIntent.putExtra(SEARCH_TYPE, DETAILED_SEARCH);
					returnIntent.putExtra(SEARCH_PARAMS, searchInput);
					setResult(RESULT_OK, returnIntent);
					finish();
				}

			}
			
			private void getName() {
				fname.trim();
				String[] name = fname.split(" ");
				if (name.length == 1 ) {
					searchInput.put(FARMER_LNAME, name[0]);
				} else {
					searchInput.put(FARMER_FNAME, name[0]);
					searchInput.put(FARMER_LNAME, name[1]);
				}
				Toast.makeText(DetailSearch.this,
						"Farmer Name: " + fname, Toast.LENGTH_SHORT)
						.show();
			}

			private void getArea() {
				switch (rdg_area.getCheckedRadioButtonId()) {
				case R.id.rdo_parish:

					// Toast.makeText(DetailSearch.this, "Parish: " + area,
					// //Toast.LENGTH_SHORT).show();
					searchInput.put(FARM_PARISH, area);
					break;
				case R.id.rdo_extension:
					// Toast.makeText(DetailSearch.this, "Extension: " + area,
					// //Toast.LENGTH_SHORT).show();
					searchInput.put(FARM_EXTENSION, area);
					break;
				case R.id.rdo_district:
					// Toast.makeText(DetailSearch.this, "District: " + area,
					// //Toast.LENGTH_SHORT).show();
					searchInput.put(FARM_DISTRICT, area);
					break;
				case -1:
					// Toast.makeText(DetailSearch.this,
					// "Please check area to search",
					// //Toast.LENGTH_SHORT).show();
					break;
				}
			}

			private void getCrop() {
				switch (rdg_crop.getCheckedRadioButtonId()) {
				case R.id.rdo_cropType:

					// Toast.makeText(DetailSearch.this, "Crop Type: " + crop,
					// //Toast.LENGTH_SHORT).show();
					/*returnIntent.putExtra("CropCol", "Crop Type");
					returnIntent.putExtra("Crop Type", crop);*/
					
					searchInput.put(CROP_TYPE, crop);
					break;
				case R.id.rdo_cropGroup:
					// Toast.makeText(DetailSearch.this, "Crop Group: " + crop,
					// //Toast.LENGTH_SHORT).show();
					/*returnIntent.putExtra("CropCol", "Crop Group");
					returnIntent.putExtra("Crop Group", crop);*/

					searchInput.put(CROP_GROUP, crop);
					break;
				case -1:
					// Toast.makeText(DetailSearch.this,
					// "Please check Crop Group or Crop Type",
					// //Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}

}
