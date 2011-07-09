package org.data.agroassistant;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FarmInfo extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is farm info for this farmer");
        setContentView(textview);
	}

}
