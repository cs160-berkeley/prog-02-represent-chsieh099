package chsieh099.wearproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by CassidyHsieh on 3/1/16.
 */
public class DetailedView extends AppCompatActivity {

    public static Drawable image;

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);
        Intent intent = getIntent();
        String ititle = intent.getStringExtra("title");
        String iparty = intent.getStringExtra("party");
        String iname = intent.getStringExtra("name");
        String iendyear= intent.getStringExtra("endyear");

        TextView title = (TextView) findViewById(R.id.textView8);
        title.setText(ititle);
        TextView party = (TextView) findViewById(R.id.textView6);
        party.setText(iparty.toUpperCase());
        TextView name = (TextView) findViewById(R.id.textView7);
        name.setText(iname);
        TextView endyear = (TextView) findViewById(R.id.textView10);
        endyear.setText(iendyear);
        TextView divider = (TextView) findViewById(R.id.textView9);
        LinearLayout detailedll = (LinearLayout) findViewById(R.id.detailedll);
        LinearLayout committeebillsll = (LinearLayout) findViewById(R.id.committeebillsll);

        if (image != null) {
            ImageView imageView = (ImageView) findViewById(R.id.imageView3);
            imageView.setImageDrawable(image);
        }

        if (iparty.equals("Democrat")) {
            party.setBackgroundColor(getResources().getColor(R.color.navy));
            name.setTextColor(getResources().getColor(R.color.navy));
            title.setTextColor(getResources().getColor(R.color.navy));
            endyear.setTextColor(getResources().getColor(R.color.navy));
            divider.setTextColor(getResources().getColor(R.color.navy));
            detailedll.setBackground(getResources().getDrawable(R.drawable.detailedbgdem));
            committeebillsll.setBackgroundColor(getResources().getColor(R.color.lightnavy));
        } else {
            party.setBackgroundColor(getResources().getColor(R.color.red));
            name.setTextColor(getResources().getColor(R.color.red));
            title.setTextColor(getResources().getColor(R.color.red));
            endyear.setTextColor(getResources().getColor(R.color.red));
            divider.setTextColor(getResources().getColor(R.color.red));
            detailedll.setBackground(getResources().getDrawable(R.drawable.detailedbgrep));
            committeebillsll.setBackgroundColor(getResources().getColor(R.color.lightred));
        }

    }
}
