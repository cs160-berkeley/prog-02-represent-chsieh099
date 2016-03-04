package chsieh099.wearproject;

import android.content.Context;
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
        byte[] message = messageEvent.getData();
        System.out.println("WEAR GOT THE MESSAGE");
//        String message = new String(byteMessage);
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
