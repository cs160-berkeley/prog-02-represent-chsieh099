package chsieh099.wearproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by CassidyHsieh on 3/1/16.
 */
public class ListAdapter extends BaseAdapter {

    public Context context;

    public Person barbara = new Person("Senator", "Democrat", "Barbara Boxer",
            "Putting the country first means Obama nominating a Justice and the Senate doing its constitutional duty by voting on the nominee",
            "2017", R.drawable.barbaraboxer);
    public Person dianne = new Person("Senator", "Democrat", "Dianne Feinstein",
            "Today we reflect on the contributions and leadership of U.S. presidents throughout our nation’s great history. Happy #PresidentsDay.",
            "2018", R.drawable.diannefeinstein);
    public Person ken = new Person("Representative", "Republican", "Ken Calvert",
            "There’s absolutely no excuse for delaying the Keystone pipeline. The President needs to put jobs before politics.",
            "2019", R.drawable.kencalvert);
    public Person duncan = new Person("Representative", "Republican", "Duncan Hunter",
            "Happy @USMC birthday to my good friend @Rep_Hunter #hoorah",
            "2020", R.drawable.duncanhunter);

    public Person[] reps = new Person[] {
            barbara,
            dianne,
            ken,
            duncan
    };

//    public String[] reps = new String[]{
//            "Barbara Boxer",
//            "Dianne Feinstein",
//            "Ken Calvert",
//            "Duncan Hunter"
//    };

    public int numreps = reps.length;

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
        TextView name = (TextView) convertView.findViewById(R.id.textView2);
        name.setText(getItem(position).name);
        ImageView mImageView = (ImageView) convertView.findViewById(R.id.imageView);
        TextView senOrRep = (TextView) convertView.findViewById(R.id.textView5);
        TextView party = (TextView) convertView.findViewById(R.id.textView3);
        TextView tweet = (TextView) convertView.findViewById(R.id.textView4);
        LinearLayout sidebar = (LinearLayout) convertView.findViewById(R.id.sidebar);
        LinearLayout imagebar = (LinearLayout) convertView.findViewById(R.id.imagebar);

        int dem = 1;
        String twitterpost = "";


        if (getItem(position).name == "Barbara Boxer") {
            mImageView.setImageResource(R.drawable.barbaraboxer);
        } if (getItem(position).name == "Dianne Feinstein") {
            mImageView.setImageResource(R.drawable.diannefeinstein);
        } if (getItem(position).name == "Ken Calvert") {
            mImageView.setImageResource(R.drawable.kencalvert);
        } if (getItem(position).name == "Duncan Hunter") {
            mImageView.setImageResource(R.drawable.duncanhunter);
        }

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
                view.getContext().startActivity(intent);
            }
        });

        senOrRep.setText(getItem(position).title.toUpperCase());
        tweet.setText(getItem(position).tweet);

        if (getItem(position).party.equals("Democrat")) {
            name.setTextColor(context.getResources().getColor(R.color.navy));
            party.setText("DEMOCRAT");
            party.setBackgroundColor(context.getResources().getColor(R.color.navy));
            tweet.setTextColor(context.getResources().getColor(R.color.navy));
            sidebar.setBackgroundColor(context.getResources().getColor(R.color.navy));
            imagebar.setBackgroundColor(context.getResources().getColor(R.color.lightnavy));
            senOrRep.setBackgroundColor(context.getResources().getColor(R.color.darknavy));
        }
        else {
            name.setTextColor(context.getResources().getColor(R.color.red));
            party.setText("REPUBLICAN");
            party.setBackgroundColor(context.getResources().getColor(R.color.red));
            tweet.setTextColor(context.getResources().getColor(R.color.red));
            sidebar.setBackgroundColor(context.getResources().getColor(R.color.red));
            imagebar.setBackgroundColor(context.getResources().getColor(R.color.lightred));
            senOrRep.setBackgroundColor(context.getResources().getColor(R.color.darkred));
        }
        return convertView;
    }
}