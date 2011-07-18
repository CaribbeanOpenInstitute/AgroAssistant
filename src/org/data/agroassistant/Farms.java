/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.data.agroassistant;

//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
/**
 *
 * @author Gebre
 */
public class Farms extends ListActivity{

	static final int FNAME_SEARCH = 10;
	static final int PARISH_SEARCH = 11;
	static final int PROPERTY_SEARCH = 12;
	static final int LOCATION_SEARCH = 13;
	static final int DETAILED_SEARCH = 14;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farms_main);

        String[] farmItems = getResources().getStringArray(R.array.ary_farms_main);
		this.setListAdapter(new AgroArrayAdapter(this, farmItems));
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.listTitle, farmerItems));

		ListView lv = getListView();
		  lv.setTextFilterEnabled(true);

		  lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		      // When clicked, show a toast with the TextView text
		    	
		    	Intent farmSearchIntent = new Intent();
		    	
		    	switch (position) {
				case 0:
					Toast.makeText(Farms.this, "You selected to Search by Farmer name", Toast.LENGTH_SHORT).show();
					farmSearchIntent.setClass(Farms.this, FarmerSearch.class);
					startActivityForResult(farmSearchIntent,FNAME_SEARCH);
					//Intent farmerIntent = new Intent(Farmers.this, FarmerNameSearch.class);
					//startActivity(farmerIntent);
					break;
				case 1:
					Toast.makeText(Farms.this, "You selected to Search by Parish", Toast.LENGTH_SHORT).show();
					farmSearchIntent.setClass(Farms.this, AreaSearch.class);
					startActivityForResult(farmSearchIntent,PARISH_SEARCH);
					break;
				case 2: 
					Toast.makeText(Farms.this, "You selected to Search by Property ID", Toast.LENGTH_SHORT).show();
					farmSearchIntent.setClass(Farms.this, PIDSearch.class);
					startActivityForResult(farmSearchIntent,DETAILED_SEARCH);
					break;
				case 3:
					Toast.makeText(Farms.this, "You selected to Search by Location", Toast.LENGTH_SHORT).show();
					farmSearchIntent.setClass(Farms.this, LocationSearch.class);
					startActivityForResult(farmSearchIntent,LOCATION_SEARCH);
					break;
				case 4:
					Toast.makeText(Farms.this, "You selected to Search by Detailed search", Toast.LENGTH_SHORT).show();
					farmSearchIntent.setClass(Farms.this, FarmerView.class);
					startActivityForResult(farmSearchIntent,DETAILED_SEARCH);
					break; 
				default:
					Toast.makeText(Farms.this, "Error: The option you selected does not exist", Toast.LENGTH_SHORT).show();
					break;
				}
		      
		    }
		  });
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //TODO: Receive query from search functions
        if (requestCode == FNAME_SEARCH) {
        	if( resultCode == RESULT_OK) {
        		//Call function to pull data from query
        		//intent.
        		Toast.makeText(Farms.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
        	}
        	else if( resultCode == RESULT_CANCELED) {
        		Toast.makeText(Farms.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
        		
        	}
        }
        
    }
}
