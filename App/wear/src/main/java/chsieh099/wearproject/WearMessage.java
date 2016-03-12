package chsieh099.wearproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by CassidyHsieh on 3/2/16.
 */
public class WearMessage extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks {

    private static GoogleApiClient gac;
    static String path;
    static byte[] message;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String path = messageEvent.getPath();
        byte[] byteMessage = messageEvent.getData();
        System.out.println("WEAR GOT THE MESSAGE");
        String message = new String(byteMessage);

        String[] data = message.split(",");
        System.out.println(data[0]);

        // Calls this on start of phone, so it will start watch
        if (path.equals("watchCongressional")) {
            System.out.println("WATCH GOT MESSAGE TO START");

            if (MainActivity.instance != null) {
                MainActivity.instance.finish();
                MainActivity.instance = null;
            }

            Intent watchStart = new Intent(this, MainActivity.class);
            watchStart.putExtra("county", data[0]);
            watchStart.putExtra("state", data[1]);
            watchStart.putExtra("numreps", data[2]);

            int numreps = Integer.parseInt(data[2]);

            MainActivity.repTitles = new String[numreps];
            MainActivity.repNames = new String[numreps];
            MainActivity.repParties = new String[numreps];
            int i;
            int j = 0;
            for (i = 3; i <= numreps * 3; i += 3) {
                MainActivity.repTitles[j] = data[i];
                MainActivity.repNames[j] = data[i+1];
                System.out.println("NAME IS " + MainActivity.repNames[j]);
                MainActivity.repParties[j] = data[i+2];
                j++;
            }
            System.out.println("SHOULD BE OBAMA PERCENT " + data[i]);
            System.out.println("SHOULD BE ROMNEY PERCENT " + data[i+1]);
            watchStart.putExtra("obamaPercent", data[i]);
            watchStart.putExtra("romneyPercent", data[i+1]);
            watchStart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            System.out.println("START WATCH ACTIVITY NOW");
            startActivity(watchStart);
        }

        // Calls this to fill in rep fragments on watch
        if (path.equals("reps")) {
            System.out.println("WEARMESSAGE: GETTING MORE DATA AND REPS");
            Intent fillIntent = new Intent(this, MainActivity.class);
            for (int i = 0; i < data.length; i ++) {
                fillIntent.putExtra(String.valueOf(i), data[i]);
            }
            fillIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(fillIntent);
        }
    }

    public static void sendMessage(String path, byte[] message, Context ctx) {
        WearMessage.path = path;
        WearMessage.message = message;
        gac = new GoogleApiClient.Builder(ctx)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new WearMessage())
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        System.out.println(connectionResult.getErrorCode());
                        System.out.println("bruh please");
                    }
                })
                .build();
        gac.connect();
        System.out.println("WE CONNECTING BRUH");
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("BEFORE THREAD STARTS");
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodeResult = Wearable.NodeApi.getConnectedNodes(gac).await();
                for (Node n : nodeResult.getNodes()) {
                    System.out.println("HIT SOME NODES");
                    Wearable.MessageApi.sendMessage(gac, n.getId(), path, message);
                }
                gac.disconnect();
            }
        }).start();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
}
