package org.data.agroassistant;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

//Utilized Google Developer tutorial -- http://developer.android.com/resources/tutorials/views/hello-mapview.html


public class LocationSearch extends MapActivity implements LocationListener {
	private MapView mapView;
	private MapController mapController;
	private LocationManager mgr;
	String provider;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.location_main);
	    
	    initMapView();
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
	    MapOverlay itemizedoverlay = new MapOverlay(drawable);
	    
	    
	    //Create Access GPS and grab geolocation
	    mgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    provider = mgr.getBestProvider(criteria, true);
	    
	    // Register the listener with the Location Manager to receive location updates
	    mgr.getLastKnownLocation(provider);
	    //mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

	    
	    
	    //Add to overlay
	    GeoPoint point = new GeoPoint(19240000,-99120000);
	    OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
	    
	    itemizedoverlay.addOverlay(overlayitem);
	    mapOverlays.add(itemizedoverlay);
	}
	
	@Override
    public void onLocationChanged(Location location) {
    	// TODO Auto-generated method stub
    	//dumpLocation(location);
		// Called when a new location is found by the network location provider.
	    

    }
	
	@Override
    public void onProviderDisabled(String provider) {
    	// TODO Auto-generated method stub
    	//log("\nProvider enabled: " + provider);
    }
    
    @Override
    public void onProviderEnabled(String provider) {
    	// TODO Auto-generated method stub
    	//log("\nProvider enabled: " + provider);
    }
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    	// TODO Auto-generated method stub
    	//log("\nProvider status changed: " + provider + ", status=" + S[status] + ", extras=" + extras);
    	
    }
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false; 
	}
	
	private void initMapView() {
		mapView = (MapView) findViewById(R.id.mapview);
		mapController = mapView.getController();
	    mapView.setBuiltInZoomControls(true);
	}

}
