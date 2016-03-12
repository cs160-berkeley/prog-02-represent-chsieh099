package chsieh099.wearproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by CassidyHsieh on 3/1/16.
 */
public class ListAdapter extends BaseAdapter {

    public Context context;
    public String jsonString;
    public Person[] reps;
    public int numreps = 0;
    public static String[] jsonStringComm;
    public static String[] jsonStringBills;
    public static String[] jsonStringTweets;
    public static String county;
    public String state;

    public void createRepsArray() {
        try {
            JSONObject mainObject = new JSONObject(jsonString);
            int count = mainObject.getInt("count");
            if (count <= 0) {
                return;
            }

            System.out.println("NUMBER OF REP IS " + Integer.toString(count));

            numreps = count;
            reps = new Person[count];
            jsonStringComm = new String[count];
            jsonStringBills = new String[count];
            jsonStringTweets = new String[count];

            JSONArray resultsObject = mainObject.getJSONArray("results");
            for (int i = 0; i < count; i++) {
                JSONObject repObject = (JSONObject) resultsObject.get(i);

                String first_name = repObject.getString("first_name");
                String last_name = repObject.getString("last_name");
                StringBuilder sb = new StringBuilder(first_name);
                sb.append(" ").append(last_name);
                String full_name = sb.toString();

                String party = repObject.getString("party");

                String title;
                if (repObject.getString("title").equals("Rep")) {
                    title = "Representative";
                } else {
                    title = "Senator";
                }

                String endYear = repObject.getString("term_end").substring(0, 4);

                String website = repObject.getString("website");

                String email = repObject.getString("oc_email");

                String memberId = repObject.getString("bioguide_id");

                String twitterHandle = repObject.getString("twitter_id");

                state = repObject.getString("state_name");

                reps[i] = new Person(title, party, full_name, endYear, website, email, memberId, twitterHandle);
            }
        } catch (Exception e){
            System.out.println("EXCEPTION IN LISTADAPTER -- ORIG ADDR");
            e.printStackTrace();
        }
    }

    public void sendMessageForWatch(String county) {
        System.out.println("DO YOU EVEN GET CALLED BRUH");
        String obamaPercent = "0";
        String romneyPercent = "0";

        if (this.county != null) {
            if (this.county.equals(county)) {
                System.out.println("RETURNED EARLY");
                return;
            }
        }
        this.county = county;

        String voteAddress = "https://raw.githubusercontent.com/cs160-sp16/voting-data/master/election-county-2012.json";
        String[] voteParams = {voteAddress};
        AsyncTask<String, Void, String> asyncTaskVote = new CongressInfoTask().execute(voteParams);
        try {
            String jsonStringVote = asyncTaskVote.get();
//            System.out.println("JSON STRING VOTE IS " + jsonStringVote);
            JSONArray mainArray = new JSONArray(jsonStringVote);
//            System.out.println("MAIN OBJECT IS " + mainArray);
//            System.out.println("FIRST ELEMENT IS " + mainArray.get(0));
//            JSONObject object = new JSONObject(mainArray.get(0).toString());
//            System.out.println("OBJECT IS " + object);
            boolean found = false;
            int index = 0;
            JSONObject object;
            int numCounties = mainArray.length();
            System.out.println("LENGTH OF MAIN ARRAY " + mainArray.length());
            while (!found) {
                object = new JSONObject(mainArray.get(index).toString());
                if (object.get("county-name").equals(this.county)) {
                    obamaPercent = object.get("obama-percentage").toString();
                    romneyPercent = object.get("romney-percentage").toString();
                    System.out.println("OBAMA PERCENT IS " + obamaPercent);
                    System.out.println("ROMNEY PERCENT IS " + romneyPercent);
                    found = true;
                }
                index += 1;
                if (index >= numCounties) {
                    found = true;
                }
            }
        } catch (Exception e){
            System.out.println("EXCEPTION IN LISTADAPTER SENDMESSAGEFORWATCH");
            e.printStackTrace();
        }
//        Message is
//                0. county
//                1. state
//                2. numreps
//                3. title for first rep
//                4. name for first rep
//                5. party for first rep
//                6. title for second rep etc...
        System.out.println("HERE'S MY COUNTY " + county + " AND MY STATE BITCH " + state);
        StringBuilder sb = new StringBuilder();
        sb.append(county);
        sb.append(",");
        sb.append(state);
        sb.append(",");

        System.out.println("PRINT NUMREPS FROM LISTADAPTER " + String.valueOf(numreps));
        sb.append(String.valueOf(numreps));
        sb.append(",");

        for (int i = 0; i < numreps; i++) {
            sb.append(reps[i].title);
            sb.append(",");
            sb.append(reps[i].name);
            sb.append(",");
            sb.append(reps[i].party);
            sb.append(",");
        }
        sb.append(obamaPercent);
        sb.append(",");
        sb.append(romneyPercent);

        String message = sb.toString();
        System.out.println("LONG ASS MESSAGE IS " + message);
        MobileMessage.sendMessage("watchCongressional", sb.toString().getBytes(), this.context);
    }

    public void createCommList() {
        try {
            // Add 3 committees for each rep
            for (int i = 0; i < numreps; i++) {
                JSONObject mainObject = new JSONObject(jsonStringComm[i]);
                int commCount = mainObject.getInt("count");
                if (commCount <= 0) {
                    return;
                }
                int index = 0;
                while (index < commCount && index < 3) {
                    JSONArray resultsObject = mainObject.getJSONArray("results");
                    JSONObject repObject = (JSONObject) resultsObject.get(index);
                    String commName = repObject.getString("name");

                    reps[i].committees[index] = commName;
                    index += 1;
                }
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION IN LISTADAPTER -- COMM ADDR");
            e.printStackTrace();
        }
    }

    public void createBillsList() {
        try {
            // Add 3 bills for each rep
            for (int i = 0; i < numreps; i++) {
                JSONObject mainObject = new JSONObject(jsonStringBills[i]);
                int billCount = mainObject.getInt("count");
                if (billCount <= 0) {
                    return;
                }
                int index = 0;
                while (index < billCount && index < 3) {
                    JSONArray resultsObject = mainObject.getJSONArray("results");
                    JSONObject repObject = (JSONObject) resultsObject.get(index);
                    String billName = repObject.getString("short_title");

                    // If bill doesn't have a short name, provide official name
                    if (billName.equals("null")) {
                        billName = repObject.getString("official_title");
                    }
                    reps[i].bills[index] = billName;
                    index += 1;
                }
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION IN LISTADAPTER -- BILLS ADDR");
            e.printStackTrace();
        }
    }

    public void createTweetsList() {
        System.out.println("TWEET TWEET TWEET");
    }

//    public Person barbara = new Person("Senator", "Democrat", "Barbara Boxer",
//            "Putting the country first means Obama nominating a Justice and the Senate doing its constitutional duty by voting on the nominee",
//            "2017", R.drawable.barbaraboxer);
//    public Person dianne = new Person("Senator", "Democrat", "Dianne Feinstein",
//            "Today we reflect on the contributions and leadership of U.S. presidents throughout our nation’s great history. Happy #PresidentsDay.",
//            "2018", R.drawable.diannefeinstein);
//    public Person ken = new Person("Representative", "Republican", "Ken Calvert",
//            "There’s absolutely no excuse for delaying the Keystone pipeline. The President needs to put jobs before politics.",
//            "2019", R.drawable.kencalvert);
//    public Person duncan = new Person("Representative", "Republican", "Duncan Hunter",
//            "Happy @USMC birthday to my good friend @Rep_Hunter #hoorah",
//            "2020", R.drawable.duncanhunter);

//    public Person[] reps = new Person[] {
//            barbara,
//            dianne,
//            ken,
//            duncan
//    };

    public ListAdapter(Context c) {
        context = c;
    }

    @Override
    public int getCount() {
        return numreps;
    }

    @Override
    public Person getItem(int position) {
        return reps[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        final int position = pos;

        if (convertView == null) {
            LayoutInflater creator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = creator.inflate(R.layout.representative_list, null);
        }

        // Get all TextViews
        TextView name = (TextView) convertView.findViewById(R.id.textView2);
        name.setText(getItem(position).name);
        ImageView mImageView = (ImageView) convertView.findViewById(R.id.imageView);
        TextView senOrRep = (TextView) convertView.findViewById(R.id.textView5);
        TextView party = (TextView) convertView.findViewById(R.id.textView3);
        TextView tweet = (TextView) convertView.findViewById(R.id.textView4);
        LinearLayout sidebar = (LinearLayout) convertView.findViewById(R.id.sidebar);
        LinearLayout imagebar = (LinearLayout) convertView.findViewById(R.id.imagebar);

        mImageView.setImageResource(getItem(position).imageId);

        ImageButton webButton = (ImageButton) convertView.findViewById(R.id.imageButton);
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getItem(position).website);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                view.getContext().startActivity(intent);
            }
        });

        ImageButton emailButton = (ImageButton) convertView.findViewById(R.id.imageButton2);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Email")
                        .setMessage(getItem(position).name + "'s email is " + getItem(position).email + ".")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .show();
            }
        });

        ImageButton infoButton = (ImageButton) convertView.findViewById(R.id.imageButton3);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable image = context.getDrawable(getItem(position).imageId);
                DetailedView.image = image;

                Intent intent = new Intent(view.getContext(), DetailedView.class);
                intent.putExtra("title", getItem(position).title);
                intent.putExtra("party", getItem(position).party);
                intent.putExtra("name", getItem(position).name);
                intent.putExtra("endyear", getItem(position).endyear);
                intent.putExtra("committees", getItem(position).committees);
                intent.putExtra("bills", getItem(position).bills);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

        senOrRep.setText(getItem(position).title.toUpperCase());
        tweet.setText(getItem(position).tweet);

        if (getItem(position).party.equals("D")) {
            party.setText("DEMOCRAT");
            name.setTextColor(context.getResources().getColor(R.color.navy));
            party.setBackgroundColor(context.getResources().getColor(R.color.navy));
            tweet.setTextColor(context.getResources().getColor(R.color.navy));
            sidebar.setBackgroundColor(context.getResources().getColor(R.color.navy));
            imagebar.setBackgroundColor(context.getResources().getColor(R.color.lightnavy));
            senOrRep.setBackgroundColor(context.getResources().getColor(R.color.darknavy));
        } else if (getItem(position).party.equals("I")) {
            party.setText("INDEPENDENT");
            name.setTextColor(context.getResources().getColor(R.color.navy));
            party.setBackgroundColor(context.getResources().getColor(R.color.navy));
            tweet.setTextColor(context.getResources().getColor(R.color.navy));
            sidebar.setBackgroundColor(context.getResources().getColor(R.color.navy));
            imagebar.setBackgroundColor(context.getResources().getColor(R.color.lightnavy));
            senOrRep.setBackgroundColor(context.getResources().getColor(R.color.darknavy));
        } else {
            party.setText("REPUBLICAN");
            name.setTextColor(context.getResources().getColor(R.color.red));
            party.setBackgroundColor(context.getResources().getColor(R.color.red));
            tweet.setTextColor(context.getResources().getColor(R.color.red));
            sidebar.setBackgroundColor(context.getResources().getColor(R.color.red));
            imagebar.setBackgroundColor(context.getResources().getColor(R.color.lightred));
            senOrRep.setBackgroundColor(context.getResources().getColor(R.color.darkred));
        }
        return convertView;
    }
}