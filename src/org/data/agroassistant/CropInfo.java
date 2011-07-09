package org.data.agroassistant;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CropInfo extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is crop info");
        setContentView(textview);
	}
}