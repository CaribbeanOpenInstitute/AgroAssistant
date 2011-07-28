package org.data.agroassistant;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FarmerInfo extends Activity{
	private String firstname, lastname, id, size;
	
	public void onCreate(Bundle savedInstanceState) {
		//TODO Implement Farmer tab UI
		//TODO Grey and black UI icons
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_info);

        final TextView farmername = (TextView) findViewById(R.id.info_farmername);
        final TextView farmerid   = (TextView) findViewById(R.id.info_farmerid);
        final TextView farmersize   = (TextView) findViewById(R.id.info_farmersize);
        
        Bundle farmerinfo = getIntent().getExtras();
        
        firstname =  farmerinfo.getString("firstname");
        lastname  =  farmerinfo.getString("lastname" );
        id        =  farmerinfo.getString("farmerid" );
        size	  =  farmerinfo.getString("size"     );
        
        
        
        farmername.setText(firstname + " " + lastname);
        farmerid.setText(id);
        farmersize.setText(size);
        
    }
}
