package org.data.agroassistant;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class FarmView extends TabActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farm_view);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, FarmInfo.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    /*spec = tabHost.newTabSpec("farmerInfo").setIndicator("Farmer",
	                      res.getDrawable(R.drawable.ic_menu_farmer))
	                  .setContent(intent);
	    tabHost.addTab(spec);*/

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, FarmDetails.class);
	    spec = tabHost.newTabSpec("farmDetails").setIndicator("Farm",
	                      res.getDrawable(R.drawable.ic_menu_farm))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, CropInfo.class);
	    spec = tabHost.newTabSpec("cropInfo").setIndicator("Crops",
	                      res.getDrawable(R.drawable.ic_menu_crop))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(2);
	}

}
