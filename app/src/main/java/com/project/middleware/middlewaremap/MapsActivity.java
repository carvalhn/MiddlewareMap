package com.project.middleware.middlewaremap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    //Declaring JSON File
    public static final String JSONUrl = "https://dl.dropboxusercontent.com/u/264856774/Middleware.json";

    //Array
    public ArrayList<Attractions> touristAttractions = new ArrayList<Attractions>();

    //JSON Keys for Google Map Tags
    private static final String place = "place";
    private static final String longt = "longt";
    private static final String lat ="lat";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MapLog", "isNetworkAvailableCheck");
        setContentView(R.layout.activity_maps);

        //Load JSON File,get locations
        if(JSONReader.isNetworkAvailable(MapsActivity.this))
        {
            Log.i("MapLog", "isNetworkAvailableCheck");
            new myTask().execute(JSONUrl);
        }else{
            showToast("No Network Connection");
        }
        //Setup the map
        //setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }



    //Asynchroise Mytask to retreive Data from the JSON file
    class myTask extends AsyncTask<String, Void, String>
    {

        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return JSONReader.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {

            super.onPostExecute(result);

            if (null != pDialog && pDialog.isShowing())
            {
                pDialog.dismiss();
            }
            if (null == result || result.length() == 0)
            {
                MapsActivity.this.finish();
            }
            else{
                try{

                    JSONObject mainJson = new JSONObject(result);
                    JSONArray attractionsArray = mainJson.optJSONArray("location");


                    for(int i=0;i<attractionsArray.length();i++)
                    {
                        JSONObject attractionJSON = attractionsArray.optJSONObject(i);
                        String name = attractionJSON.optString(place);

                        double longitude = attractionJSON.optDouble(longt);
                        Log.i("MapLog", longitude+ "longti");
                        double latitude = attractionJSON.optDouble(lat);
                        Log.i("MapLog", latitude+ "lati");

                        Attractions newAttraction = new Attractions(name,longitude,latitude);


                        touristAttractions.add(newAttraction);
                        Log.i("MapLog","After Adding attraction");
                    }
                    setUpMapIfNeeded();

                    Log.i("MapLog","After loading map");
                    setUpMap();

                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }


        }


    }


    private void setUpMap() {

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(53.343791, -6.254567)).zoom(12).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        for(int i=0;i<touristAttractions.size();i++)
        {
            Attractions newAttrac = touristAttractions.get(i);
            String name = newAttrac.getName();
            Double longt = newAttrac.getLongt();
            Double lati = newAttrac.getLat();

            LatLng position = new LatLng(longt,lati);
            mMap.addMarker(new MarkerOptions().position(position).title(name));


        }
        //mMap.addMarker(new MarkerOptions().position(new LatLng(53.343791, -6.254567)).title("Trinity College"));
    }


    public void showToast(String msg)
    {
        Toast.makeText(MapsActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}
