package org.data.agroassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class agroAssistant extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
    }
    
    public void onFarmerClick(View v) {
        //Toast.makeText(this, "Clicked Farmers", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(agroAssistant.this, Farmers.class);
        agroAssistant.this.startActivity(myIntent);

    }

    public void onFarmClick(View v) {
        //Toast.makeText(this, "Clicked Farms", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(agroAssistant.this, Farms.class);
        agroAssistant.this.startActivity(myIntent);
    }

    public void onCropClick(View v) {
        //Toast.makeText(this, "Clicked Crops", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(agroAssistant.this, Crops.class);
        agroAssistant.this.startActivity(myIntent);
    }

    public void onPriceClick(View v) {
        //Toast.makeText(this, "Clicked Prices", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(agroAssistant.this, Prices.class);
        agroAssistant.this.startActivity(myIntent);
    }
    
    /*==========================================================================
     * MENU Functions
     ==========================================================================*/
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_menu, menu);
		
		return true;
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
			case R.id.itemPrefs:
				startActivity(new Intent(this, UserPrefs.class));
			break;
			case R.id.aboutPrefs:
				Toast.makeText(this, "About activity to be implemented", Toast.LENGTH_SHORT).show();
				//startActivity(new Intent(this, About.class));	//To About activity be implemented
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}