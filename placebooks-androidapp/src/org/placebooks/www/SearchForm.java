package org.placebooks.www;

import java.util.Locale;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.location.LocationManager;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.widget.ImageButton;


public class SearchForm extends Activity /*implements AdapterView.OnItemSelectedListener*/ {
		
	private String username;
	private String distanceItem;
	private String activityItem;
	private LocationManager locationManager;

	private String languageSelected;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.searchform);
        getWindow().setWindowAnimations(0);	//Do not animate the view when it gets pushed on the screen		
      
        Intent intent = getIntent();
        if(intent != null) username = intent.getStringExtra("username");
        System.out.println("search form Username = " + username);
        if(intent != null) distanceItem = intent.getStringExtra("distanceItem");
        if(intent != null) activityItem = intent.getStringExtra("activityItem");

        CustomApp appState = ((CustomApp)getApplicationContext());
        languageSelected  = appState.getLanguage();  
        Locale locale = new Locale(languageSelected);   
        Locale.setDefault(locale);  
        Configuration config = new Configuration();  
        config.locale = locale;  
        getBaseContext().getResources().updateConfiguration(config,   
        getBaseContext().getResources().getDisplayMetrics()); 
        System.out.println("language selected ==== " + languageSelected);
        setContentView(TabGroupActivity.makeSpinner(getParent()));
       
        Spinner spinnerDistance = (Spinner) findViewById(R.id.spinnerDistance);
        Spinner spinnerActivity = (Spinner) findViewById(R.id.spinnerActivity);


        spinnerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //Object item = parent.getItemAtPosition(pos);
            	distanceItem = parent.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        spinnerActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //Object item = parent.getItemAtPosition(pos);
            	activityItem = parent.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        Button searchButton = (Button) findViewById(R.id.searchButton);
        
    	locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        OnTouchListener searchListener = new OnTouchListener() {
        	public boolean onTouch(View v, MotionEvent event) {
        		 if (event.getAction()==MotionEvent.ACTION_UP){
        			 
        			//Vibrator vib = (Vibrator) getSystemService(SearchForm.this.VIBRATOR_SERVICE);
					//vib.vibrate(300);
        			 
        			 if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
        				 //GPS AND/OR NETWORK IS ENABLED      
        				 
	        			 OnlineCheck oc = new OnlineCheck();	
	        			 if (oc.isOnline(SearchForm.this)){
	        				 					            
		     		        System.out.println("distance = " + distanceItem + " activity ==" + activityItem);
		
		             		Intent intent = new Intent(SearchForm.this, Search.class);
		             		intent.putExtra("username", username);
		             		intent.putExtra("distanceItem", distanceItem);
		             		intent.putExtra("activityItem", activityItem);
		             		
		        			getParent().startActivity(intent);
	        			 }
	        			 else{
	        				 
	 				        AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
	 			        	builder.setTitle(getResources().getString(R.string.unable_search));
	 			        	builder.setMessage(getResources().getString(R.string.unable_search2));
	 			        	builder.setPositiveButton(getResources().getString(R.string.ok), null);
	 			        	builder.show();
	        				 
	        			 }
        			 
        			 }
        			 else{
        				 //GPS AND/OR NETWORK ISN'T ENABLED 
     		    		showGPSDisabledAlertToUser();
     		    	}
        			 

        		 }
        		 return false;
        	}
        };
        
        searchButton.setOnTouchListener(searchListener);   
        
//        ImageButton logoutButton = (ImageButton) findViewById(R.id.logoutButton);

/*        logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
        	
	            //Vibrator vib = (Vibrator) getSystemService(TabGroupActivity.VIBRATOR_SERVICE);
	            //vib.vibrate(300);
	            
	            //ask user are they sure they want to log out
	            AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getParent());
	            helpBuilder.setTitle(getResources().getString(R.string.sign_out));
	            helpBuilder.setMessage(getResources().getString(R.string.sign_out_q));
	            helpBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

	               public void onClick(DialogInterface dialog, int which) {
	            	   //clear the preferences
	            	   clearPreferences();
	            	   //switch to login interface
	            	   Intent intent = new Intent();
	   	        	   intent.setClassName("org.placebooks.www", "org.placebooks.www.PlaceBooks");
	   	        	   startActivity(intent);
	   	        	   finish();
	            	   
	               }
	               });
          	 
	            helpBuilder.setNeutralButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

		             @Override
		             public void onClick(DialogInterface dialog, int which) {
		              //Do nothing
		             }
		            });

		            AlertDialog helpDialog = helpBuilder.create();
		            helpDialog.show();
	               }
	            
	            
			});
 */      
        
	}
	
	private void showGPSDisabledAlertToUser(){
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getParent());
			alertDialogBuilder.setTitle(getResources().getString(R.string.unable_search_location));
			alertDialogBuilder.setMessage(getResources().getString(R.string.unable_search_location2))
			.setCancelable(false)
			.setPositiveButton(getResources().getString(R.string.goto_settings),
			new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					Intent callGPSSettingIntent = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(callGPSSettingIntent);
				}
			});
			alertDialogBuilder.setNegativeButton(getResources().getString(R.string.cancel),
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							dialog.cancel();
						}
					});
			AlertDialog alert = alertDialogBuilder.create();
			alert.show();
	}

	public void clearPreferences(){
		
  	    SharedPreferences prefs = SearchForm.this.getSharedPreferences("LOGIN_DETAILS", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
	    editor.clear();
	    editor.commit();
	     
	}	

}