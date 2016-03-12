package chsieh099.wearproject;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CassidyHsieh on 3/10/16.
 */
public class CongressInfoTask extends AsyncTask<String, Void, String> {

    public String jsonString;

    @Override
    protected String doInBackground(String... params) {
        if (params != null) {
            StringBuffer jsonStringBuffer = new StringBuffer("");
            try{
                String address = params[0];
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    jsonStringBuffer.append(line);
                }
                connection.disconnect();
            } catch (IOException e) {
                // writing exception to log
                System.out.println("GOT AN EXCEPTION" +
                        "");
                e.printStackTrace();
            }

            jsonString = jsonStringBuffer.toString();
            return jsonStringBuffer.toString();
        }
        return null;
    }


    protected void onProgressUpdate(Integer... progress) {
        return;
    }

    protected void onPostExecute(String result) {
        return;
    }
}
