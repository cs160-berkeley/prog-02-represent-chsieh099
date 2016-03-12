package chsieh099.wearproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by CassidyHsieh on 3/2/16.
 */
public class MobileMessage extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks {

    private static GoogleApiClient gac;
    static String path;
    static byte[] message;
    public static ListAdapter la;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String path = messageEvent.getPath();
        byte[] msg = messageEvent.getData();
        System.out.println("MOBILE GOT THE MESSAGE");
        System.out.println((String) path);

//        String message = msg.toString();

        if (path.equals("repIndex")) {
            int index = msg[0];
            System.out.println(index);

            if (la == null) {
                System.out.println("LA IS NULL");
                return;
            }

            Drawable image = this.getDrawable(la.getItem(index).imageId);
            DetailedView.image = image;

            Intent intent = new Intent(this, DetailedView.class);
            intent.putExtra("title", la.getItem(index).title);
            intent.putExtra("party", la.getItem(index).party);
            intent.putExtra("name", la.getItem(index).name);
            intent.putExtra("endyear", la.getItem(index).endyear);
            intent.putExtra("committees", la.getItem(index).committees);
            intent.putExtra("bills", la.getItem(index).bills);
            intent.putExtra("state", la.state);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public static void sendMessage(String path, byte[] message, Context ctx) {
        System.out.println("MOBILE MESSAGE DOING " + path);
        MobileMessage.path = path;
        MobileMessage.message = message;
        gac = new GoogleApiClient.Builder(ctx)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new MobileMessage())
                .build();
        gac.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodeResult = Wearable.NodeApi.getConnectedNodes(gac).await();
                for (Node n : nodeResult.getNodes()) {
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
