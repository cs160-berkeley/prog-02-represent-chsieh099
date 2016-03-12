package chsieh099.wearproject;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public String zipcode;
    public double latitude;
    public double longitude;
    public String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitZipcode(View view) {

        EditText zipcodeInput = (EditText) findViewById(R.id.editText);
        zipcode = zipcodeInput.getText().toString();
        System.out.println("zipcode is " + zipcode);

        if (zipcode.length() != 5) {
            return;
        }

        Double latitude = (double) 0;
        Double longitude = (double) 0;
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(zipcode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                latitude = address.getLatitude();
                longitude = address.getLongitude();
            }
//            System.out.println("ADDRESSES FIRST IS " + addresses.get(0));
        } catch (IOException e) {
            System.out.println("IOEXCEPTION IN ZIPCODE");
            e.printStackTrace();
        }

        // Make intent to open Congressional view on zipcode entry
        Intent intent = new Intent(this, Congressional.class);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://congress.api.sunlightfoundation.com/legislators/locate?zip=");
        stringBuilder.append(zipcode);
        stringBuilder.append("&apikey=5991ab6be6d24840a1b44cfea83e365e");
        address = stringBuilder.toString();

        intent.putExtra("address", address);
        System.out.println("ADDRESS IS " + address);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // Getting county stuff
        StringBuilder sbGeo = new StringBuilder();
        sbGeo.append("https://maps.googleapis.com/maps/api/geocode/json?latlng=");
        sbGeo.append(Double.toString(latitude));
        sbGeo.append(",");
        sbGeo.append(Double.toString(longitude));
        sbGeo.append("&key=AIzaSyDMZd3Smx4jCyoIHncL3dIkq0daRODgH-Y");
        String geoAddress = sbGeo.toString();
        String[] geoAddressParams = {geoAddress};

        String county = "";
        AsyncTask<String, Void, String> asynctaskGeo = new CongressInfoTask().execute(geoAddressParams);
        try {
            String jsonStringGeo = asynctaskGeo.get();
//                System.out.println("JSON STRING GEO IS " + jsonStringGeo);
            JSONObject mainObject = new JSONObject(jsonStringGeo);
            JSONArray resultsObject = mainObject.getJSONArray("results");
            JSONObject repObject = (JSONObject) resultsObject.get(0);
            JSONArray repAddress = (JSONArray) repObject.get("address_components");
            boolean found = false;
            int i = 0;
            while (!found) {
                JSONObject repAddrObject = (JSONObject) repAddress.get(i);
                String key = (String) repAddrObject.getString("types");
                String checkCompare = key.substring(2);
                System.out.println("CHECK COMPARE " + checkCompare);
                if (checkCompare.length() > "administrative_area_level_2".length()) {
                    if (checkCompare.substring(0, 27).equals("administrative_area_level_2")) {
                        System.out.println("THOSE LETTERS " + checkCompare.substring(0, 27));
                        county = repAddrObject.getString("long_name");
                        System.out.println("COUNTY BITCH " + county);
                        // If the name ends with word county, remove the word county
                        if (county.length() > " county".length()) {
                            System.out.println("LAST CHARACTERS " + county.substring(county.length()-6));
                            if (county.substring(county.length()-6).equals("County")) {
                                county = county.substring(0, county.length()-7);
                                System.out.println("UPDATED COUNTY " + county);
                            }
                        }
                        found = true;
                    }
                }
                i += 1;
            }
            System.out.println("COUNTY IS " + county);

        } catch (Exception e){
            System.out.println("EXCEPTION IN CONGRESSIONAL ONCREATE -- BILLS ADDRESS");
            e.printStackTrace();
        }

        Congressional.county = county;
//        MobileMessage.sendMessage("watchCongressional", "blabla".getBytes(), this);
    }

    public void submitCurrentLocation(View view) throws IOException {
        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            latitude = tracker.getLatitude();
            longitude = tracker.getLongitude();
            System.out.println("latitude is " + Double.toString(latitude));
            System.out.println("longitude is " + Double.toString(longitude));

//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            // Make intent to open Congressional view on current location click
            Intent intent = new Intent(this, Congressional.class);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://congress.api.sunlightfoundation.com/legislators/locate?latitude=");
            stringBuilder.append(Double.toString(latitude));
            stringBuilder.append("&longitude=");
            stringBuilder.append(Double.toString(longitude));
            stringBuilder.append("&apikey=5991ab6be6d24840a1b44cfea83e365e");
            address = stringBuilder.toString();

            intent.putExtra("address", address);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // Getting county stuff
            StringBuilder sbGeo = new StringBuilder();
            sbGeo.append("https://maps.googleapis.com/maps/api/geocode/json?latlng=");
            sbGeo.append(Double.toString(latitude));
            sbGeo.append(",");
            sbGeo.append(Double.toString(longitude));
            sbGeo.append("&key=AIzaSyDMZd3Smx4jCyoIHncL3dIkq0daRODgH-Y");
            String geoAddress = sbGeo.toString();
            String[] geoAddressParams = {geoAddress};

            String county = "";
            AsyncTask<String, Void, String> asynctaskGeo = new CongressInfoTask().execute(geoAddressParams);
            try {
                String jsonStringGeo = asynctaskGeo.get();
//                System.out.println("JSON STRING GEO IS " + jsonStringGeo);
                JSONObject mainObject = new JSONObject(jsonStringGeo);
                JSONArray resultsObject = mainObject.getJSONArray("results");
                JSONObject repObject = (JSONObject) resultsObject.get(0);
                JSONArray repAddress = (JSONArray) repObject.get("address_components");
                boolean found = false;
                int i = 0;
                while (!found) {
                    JSONObject repAddrObject = (JSONObject) repAddress.get(i);
                    String key = (String) repAddrObject.getString("types");
                    String checkCompare = key.substring(2);
                    System.out.println("CHECK COMPARE " + checkCompare);
                    if (checkCompare.length() > "administrative_area_level_2".length()) {
                        if (checkCompare.substring(0, 27).equals("administrative_area_level_2")) {
                            System.out.println("THOSE LETTERS " + checkCompare.substring(0, 27));
                            county = repAddrObject.getString("long_name");
                            System.out.println("COUNTY BITCH " + county);
                            // If the name ends with word county, remove the word county
                            if (county.length() > " county".length()) {
                                System.out.println("LAST CHARACTERS " + county.substring(county.length()-6));
                                if (county.substring(county.length()-6).equals("County")) {
                                    county = county.substring(0, county.length()-7);
                                    System.out.println("UPDATED COUNTY " + county);
                                }
                            }
                            found = true;
                        }
                    }
                    i += 1;
                }
                System.out.println(county);

            } catch (Exception e){
                System.out.println("EXCEPTION IN CONGRESSIONAL ONCREATE -- BILLS ADDRESS");
                e.printStackTrace();
            }

            Congressional.county = county;
//            MobileMessage.sendMessage("watchCongressional", county.getBytes(), this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
