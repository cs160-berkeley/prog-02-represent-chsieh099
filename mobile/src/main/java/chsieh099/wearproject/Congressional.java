package chsieh099.wearproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Congressional extends AppCompatActivity {

    public ListAdapter list;
    public static String address;
    public static String commAddress;
    public static String billAddress;
    public static String twitterAddress;
    public static String county;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        list = new ListAdapter(this);
        if (extras != null) {
            address = extras.getString("address");
        }
        String[] params = {address};
        AsyncTask<String, Void, String> asynctask = new CongressInfoTask().execute(params);
        try {
            String jsonString = asynctask.get();
            list.jsonString = jsonString;
            list.createRepsArray();
        } catch (Exception e){
            System.out.println("EXCEPTION IN CONGRESSIONAL ONCREATE -- ORIG ADDRESS");
            e.printStackTrace();
        }


        // ALL THE COMMITTEE GETTING STUFF
        for (int i = 0; i < list.getCount(); i++) {
            Person person = list.getItem(i);
            // Get committee jsonStrings for each person
            StringBuilder sb = new StringBuilder();
            sb.append("http://congress.api.sunlightfoundation.com/committees?member_ids=");
            sb.append(person.memberId);
            sb.append("&apikey=5991ab6be6d24840a1b44cfea83e365e");
            commAddress = sb.toString();

            String[] commParams = {commAddress};
            AsyncTask<String, Void, String> asynctaskComm = new CongressInfoTask().execute(commParams);
            try {
                String jsonStringComm = asynctaskComm.get();
                list.jsonStringComm[i] = jsonStringComm;
            } catch (Exception e){
                System.out.println("EXCEPTION IN CONGRESSIONAL ONCREATE -- COMM ADDRESS");
                e.printStackTrace();
            }
        }
        try {
            list.createCommList();
        } catch (Exception e) {
            System.out.println("EXCEPTION IN CONGRESSIONAL ONCREATE -- COMM ADDRESS 2");
            e.printStackTrace();
        }

        // ALL THE BILL GETTING STUFF
        for (int i = 0; i < list.getCount(); i++) {
            Person person = list.getItem(i);
            // Get bill jsonStrings for each person
            StringBuilder sb2 = new StringBuilder();
            sb2.append("http://congress.api.sunlightfoundation.com/bills?sponsor_id=");
            sb2.append(person.memberId);
            sb2.append("&apikey=5991ab6be6d24840a1b44cfea83e365e");
            billAddress = sb2.toString();

            String[] billParams = {billAddress};

            AsyncTask<String, Void, String> asynctaskBills = new CongressInfoTask().execute(billParams);
            try {
                String jsonStringBills = asynctaskBills.get();
                list.jsonStringBills[i] = jsonStringBills;
            } catch (Exception e){
                System.out.println("EXCEPTION IN CONGRESSIONAL ONCREATE -- BILLS ADDRESS");
                e.printStackTrace();
            }
        }
        try {
            list.createBillsList();
        } catch (Exception e) {
            System.out.println("EXCEPTION IN CONGRESSIONAL ONCREATE -- BILLS ADDRESS 2");
            e.printStackTrace();
        }


        // Handle Twitter stuff here
//        for (int i = 0; i < list.getCount(); i++) {
//            Person person = list.getItem(i);
//
//            StringBuilder sbTwitter = new StringBuilder();
//            sbTwitter.append("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=");
//            sbTwitter.append(person.twitterHandle);
//            sbTwitter.append("&count=1");
//
//            String[] twitterParams = {sbTwitter.toString()};
//            AsyncTask<String, Void, String> asyncTaskTwitter = new TwitterTask().execute(twitterParams);
//            try {
//                String jsonStringTweets = asyncTaskTwitter.get();
//                System.out.println("JSON STRING TWEETS IS " + jsonStringTweets);
//                list.jsonStringTweets[i] = jsonStringTweets;
//            } catch (Exception e) {
//                System.out.println("EXCEPTION IN CONGRESSIONAL ONCREATE -- TWITTER HANDLING");
//                e.printStackTrace();
//            }
//        } try {
//            list.createTweetsList();
//        } catch (Exception e) {
//            System.out.println("EXCEPTION IN CONGRESSIONAL ONCREATE -- TWITTER HANDLING 2");
//            e.printStackTrace();
//        }

        System.out.println("MAKE SURE COUNTY IS NON NULL HERE " + county);
        list.sendMessageForWatch(county);
        System.out.println("GOT THROUGH ALL DAT SHIT");
        MobileMessage.la = list;
        GridView lv = (GridView) findViewById(R.id.gridView);
        lv.setAdapter(list);
    }

//    public String request(String address) {
//        StringBuffer jsonString = new StringBuffer("");
//        try{
//            URL url = new URL(address);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
//
//            InputStream inputStream = connection.getInputStream();
//
//            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
//            String line = "";
//            while ((line = rd.readLine()) != null) {
//                jsonString.append(line);
//            }
//
//            connection.disconnect();
//        } catch (IOException e) {
//            // writing exception to log
//            System.out.println("GOT AN EXCEPTION" +
//                    "");
//            e.printStackTrace();
//        }
//
//        return jsonString.toString();
//    }
//
//    public String getJSON(String address){
//        StringBuilder builder = new StringBuilder();
//        HttpClient client = new DefaultHttpClient();
//        HttpGet httpGet = new HttpGet(address);
//        try{
//            HttpResponse response = client.execute(httpGet);
//            System.out.println("GOT STATUS");
//            StatusLine statusLine = response.getStatusLine();
//            int statusCode = statusLine.getStatusCode();
//            if(statusCode == 200){
//                HttpEntity entity = response.getEntity();
//                InputStream content = entity.getContent();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//                String line;
//                while((line = reader.readLine()) != null){
//                    builder.append(line);
//                }
//            } else {
//                Log.e(MainActivity.class.toString(), "Failed to get JSON object");
//            }
//        } catch(ClientProtocolException e){
//            e.printStackTrace();
//            System.out.println("FIRST ERROR");
//        } catch (IOException e){
//            e.printStackTrace();
//            System.out.println("SECOND ERROR");
//        }
//        return builder.toString();
//    }
}