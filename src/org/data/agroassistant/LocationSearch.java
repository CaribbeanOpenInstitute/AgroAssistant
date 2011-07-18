package org.data.agroassistant;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;



//Utilized Google Developer tutorial -- http://developer.android.com/resources/tutorials/views/hello-mapview.html


public class LocationSearch extends MapActivity implements LocationListener{
	private MapView mapView;
	private MapController mapController;
	private LocationManager mgr;
	private GeoPoint currentPoint;
	private Location currentLoc;

	//Map Items
	private List<Overlay> mapOverlays;
	private OverlayItem overlayitem;
	Drawable drawable;
	ItemizedOverlay itemizedoverlay;
	
	
	private double lat;
	private double lng;
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	
	private static final int TEN_MINUTES = 1000 * 60 * 10;
	private static final int FOUR_MINUTES = 1000 * 60 * 4;
	private static final int ONE_MINUTES = 1000 * 60 * 1;
	private static final int TWOFIFTY_METRES = 25;
	Timer timer1;


	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.location_main);
	    
	    initMapView();
	    getLocation();
	    updateMap();
	    
	    final Button btn_refresh = (Button) findViewById(R.id.btn_location_refresh);
	    final Button btn_accept = (Button) findViewById(R.id.btn_location_accept);
	    
    	btn_refresh.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					timer1.cancel();
					Toast.makeText(LocationSearch.this, R.string.btn_location_refresh_message, Toast.LENGTH_SHORT).show();
					getLocation();
				}
			});
    	
    	btn_accept.setOnClickListener(new OnClickListener() {
    		public void onClick(View arg0) {
    			timer1.cancel();
				Intent returnIntent = new Intent();
				
				returnIntent.putExtra("latitude", currentLoc.getLatitude());
				returnIntent.putExtra("longitude", currentLoc.getLongitude());
				
				//Toast.makeText(LocationSearch.this, "Latitude: " + Double.toString(currentLoc.getLatitude()) + "Longitude: " + Double.toString(currentLoc.getLongitude()) , Toast.LENGTH_SHORT).show();
				
				setResult(RESULT_OK,returnIntent);
				finish();
    		}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getLocation();
		//mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TEN_MINUTES, TWOFIFTY_METRES, this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mgr.removeUpdates(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Override for screen rotations
		super.onDestroy();
		timer1.cancel();
	}
	
	public boolean getLocation() {
		//Create Access GPS and grab geolocation
	    mgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	    
	    //exceptions will be thrown if provider is not permitted.
        try{gps_enabled=mgr.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}
	    
        //don't start listeners if no provider is enabled
        if(!gps_enabled && !network_enabled)
            return false;
        
        if(gps_enabled) {
            mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, TEN_MINUTES, TWOFIFTY_METRES, this);
        	//mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        if(network_enabled) {
        	currentLoc = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            //mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TEN_MINUTES, TWOFIFTY_METRES, this);
            mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
        timer1=new Timer();
        timer1.schedule(new GetLastLocation(), FOUR_MINUTES);

	    return true;
	}
	
	LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location newLoc) {
            timer1.cancel();
            if (isBetterLocation(newLoc, currentLoc)) {
				currentLoc = newLoc;
            	updateMap();
            }
            
            mgr.removeUpdates(LocationSearch.this);
            mgr.removeUpdates(locationListenerGps);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

	
	public void onLocationChanged(Location newLoc) {
		// TODO Auto-generated method stub
    	//dumpLocation(location);
		if (newLoc != null) {
			if (isBetterLocation(newLoc, currentLoc)) {
					currentLoc = newLoc;
					updateMap();
			}
			//lat = newLoc.getLatitude();
			//lng = newLoc.getLongitude();
		}
		// Called when a new location is found by the network location provider.
	};
	
	class GetLastLocation extends TimerTask {
        @Override
        public void run() {
             //mgr.removeUpdates(locationListenerGps);
             mgr.removeUpdates(LocationSearch.this);

             Location net_loc=null, gps_loc=null;
             if(gps_enabled)
                 gps_loc=mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
             if(network_enabled)
                 net_loc=mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
             
             //if there are both values use the better one
             if(gps_loc!=null && net_loc!=null){
            	 if (isBetterLocation(gps_loc, net_loc))
                	currentLoc = gps_loc;
            	 	updateMap();
            	 	return;
             }

             if(gps_loc!=null){
            	 if (isBetterLocation(gps_loc, net_loc))
                 	currentLoc = gps_loc;
            	 	updateMap();
            	 	return;
             }
             if(net_loc!=null){
            	 if (isBetterLocation(gps_loc, net_loc))
                 	currentLoc = gps_loc;
            	 	updateMap();
            	 	return;
             }
        }
    }

	
	public void onProviderDisabled(String provider) {
    	// TODO Auto-generated method stub
    	//log("\nProvider enabled: " + provider);
	}
    
    //@Override
    public void onProviderEnabled(String provider) {
    	// TODO Auto-generated method stub
    	//log("\nProvider enabled: " + provider);
    }
    
    //@Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    	// TODO Auto-generated method stub
    	//log("\nProvider status changed: " + provider + ", status=" + S[status] + ", extras=" + extras);
    	
    }
    
    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
   protected boolean isBetterLocation(Location location, Location currentBestLocation) {
       if (currentBestLocation == null) {
           // A new location is always better than no location
           return true;
       }

       // Check whether the new location fix is newer or older
       long timeDelta = location.getTime() - currentBestLocation.getTime();
       boolean isSignificantlyNewer = timeDelta > TEN_MINUTES;
       boolean isSignificantlyOlder = timeDelta < -TEN_MINUTES;
       boolean isNewer = timeDelta > 0;

       // If it's been more than two minutes since the current location, use the new location
       // because the user has likely moved
       if (isSignificantlyNewer) {
           return true;
       // If the new location is more than two minutes older, it must be worse
       } else if (isSignificantlyOlder) {
           return false;
       }

       // Check whether the new location fix is more or less accurate
       int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
       boolean isLessAccurate = accuracyDelta > 0;
       boolean isMoreAccurate = accuracyDelta < 0;
       boolean isSignificantlyLessAccurate = accuracyDelta > 200;

       // Check if the old and new location are from the same provider
       boolean isFromSameProvider = isSameProvider(location.getProvider(),
               currentBestLocation.getProvider());

       // Determine location quality using a combination of timeliness and accuracy
       if (isMoreAccurate) {
           return true;
       } else if (isNewer && !isLessAccurate) {
           return true;
       } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
           return true;
       }
       return false;
   }

   /** Checks whether two providers are the same */
   private boolean isSameProvider(String provider1, String provider2) {
       if (provider1 == null) {
         return provider2 == null;
       }
       return provider1.equals(provider2);
   }
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false; 
	}
	
	private void initMapView() {
		mapView = (MapView) findViewById(R.id.mapview);
		mapController = mapView.getController();
		mapController.setZoom(16);
	    mapView.setBuiltInZoomControls(true);
	    
	    drawable = this.getResources().getDrawable(R.drawable.androidmarker);
	    
	    //itemizedoverlay.onTap(index)
	    
	    mapOverlays = mapView.getOverlays();
	}
	
	private void updateMap() {
		if (currentLoc != null) {
	    	lat = currentLoc.getLatitude()*1E6;
	    	lng = currentLoc.getLongitude()*1E6;
	    	currentPoint = new GeoPoint((int) lat , (int) lng );
	    	
	    	//Remove Previous Items
	    	if(!mapOverlays.isEmpty()) {
	    		mapView.getOverlays().clear();
	    		mapOverlays.clear();
	    		mapView.invalidate();
	        }
	    	
	    	//Initialize map overlay
		    mapOverlays = mapView.getOverlays();
		    itemizedoverlay = new ItemizedOverlay(drawable, this);

	    	//Add new overlay item
	    	overlayitem = new OverlayItem(currentPoint, "You are here", "This is your current location");
	    	itemizedoverlay.addOverlay(overlayitem);
	    	mapOverlays.add(itemizedoverlay);
	    	mapController.animateTo(currentPoint);
		}
		
	}

}
