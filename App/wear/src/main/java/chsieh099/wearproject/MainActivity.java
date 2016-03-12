package chsieh099.wearproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.BoxInsetLayout;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.nio.ByteBuffer;


/**
 * Created by CassidyHsieh on 3/2/16.
 */
public class MainActivity extends FragmentActivity {

    public static ViewPager vp;
    public static String county;
    public static String state;
    public static String[] repTitles;
    public static String[] repNames;
    public static String[] repParties;
    public static int numreps;
    public static String obamaPercent;
    public static String romneyPercent;
    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            System.out.println("GET NEW INTENT");
//        }
//
//        vp = (ViewPager) findViewById(R.id.pager);
//        PageAdapter pa = new PageAdapter(getSupportFragmentManager());
//        vp.setAdapter(pa);
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.instance = this;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.county = extras.getString("county");
            this.state = extras.getString("state");
            this.numreps = Integer.parseInt(extras.getString("numreps"));
            this.obamaPercent = extras.getString("obamaPercent");
            this.romneyPercent = extras.getString("romneyPercent");
            System.out.println("NEW COUNTY IS " + county);
        }

        vp = (ViewPager) findViewById(R.id.pager);
        PageAdapter pa = new PageAdapter(getSupportFragmentManager());
        vp.setAdapter(pa);
    }

    public class PageAdapter extends FragmentPagerAdapter {
        public Fragment[] reps = new Fragment[numreps + 1];

        public PageAdapter(FragmentManager fm) {
            super(fm);
            System.out.println("NUMREPS IS " + String.valueOf(numreps));
            for (int i = 0; i < numreps; i++) {
                reps[i] = new Representative();
                Bundle args = new Bundle();
                args.putInt("index", i);
                System.out.println("SETTING INDEX AS " + String.valueOf(i));
                reps[i].setArguments(args);
            }
            reps[getCount() - 1] = new VotePage();
        }

        @Override
        public Fragment getItem(int position) {
            return reps[position];
        }

        @Override
        public int getCount() {
            return reps.length;
        }
    }

    public static class Representative extends Fragment {
        public String title;
        public String name;
        public String party;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle bundle) {
            View root = inflater.inflate(R.layout.fragment_rep, view, false);

            int index = getArguments().getInt("index");

            Button detailsButton = (Button) root.findViewById(R.id.detailsButton);
            detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("BUTTONCLICK");
                    byte pos = (byte) vp.getCurrentItem();
                    byte[] position = new byte[]{pos};
                    WearMessage.sendMessage("repIndex", position, getActivity());
                }
            });

            TextView ititle = (TextView) root.findViewById(R.id.textView15);
            TextView iname = (TextView) root.findViewById(R.id.textView11);
            TextView iparty = (TextView) root.findViewById(R.id.textView12);
//            BoxInsetLayout fragment_rep = (BoxInsetLayout) view.findViewById(R.id.fragcontainer);

            ititle.setText(repTitles[index]);
            iname.setText(repNames[index]);
            if (repParties[index].equals("D")) {
                iparty.setText("DEMOCRAT");
//                if (fragment_rep != null) {
//                    System.out.println("SETTING BACKGROUND IMAGE RESOURCE DEM");
//                    fragment_rep.setBackgroundResource(R.drawable.dembg);
//                }
            } else if (repParties[index].equals("I")) {
                iparty.setText("INDEPENDENT");
            } else {
                iparty.setText("REPUBLICAN");
            }

            return root;
        }
    }

    public static class VotePage extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle bundle) {
            View root = inflater.inflate(R.layout.activity_vote_view, view, false);
            TextView countyTextView = (TextView) root.findViewById(R.id.textView13);
            countyTextView.setText(county);
            TextView stateTextView = (TextView) root.findViewById(R.id.textView14);
            if (state != null) {
                stateTextView.setText(state.toUpperCase());
            }
            TextView obamaView = (TextView) root.findViewById(R.id.textView);
            obamaView.setText(obamaPercent);
            TextView romneyView = (TextView) root.findViewById(R.id.textView2);
            romneyView.setText(romneyPercent);

            return root;
        }
    }


}
