package org.data.agroassistant;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FarmerInfo extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		//TODO Implement Farmer tab UI
		//TODO Grey and black UI icons
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is farmer info");
        setContentView(textview);
    }
}
