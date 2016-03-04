package chsieh099.wearproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by CassidyHsieh on 3/2/16.
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager vp = (ViewPager) findViewById(R.id.pager);
        PageAdapter pa = new PageAdapter(getSupportFragmentManager());
        vp.setAdapter(pa);

        WearMessage.sendMessage("this is the path", "blablablabla".getBytes(), this);
    }

    private class PageAdapter extends FragmentPagerAdapter {
        Fragment[] reps = new Fragment[4];

        public PageAdapter(FragmentManager fm) {
            super(fm);
            reps[0] = new Representative();
            reps[1] = new Representative();
            reps[2] = new Representative();
            reps[3] = new VotePage();
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
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle bundle) {
            View root = inflater.inflate(R.layout.fragment_rep, view, false);
            return root;
        }
    }

    public static class VotePage extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle bundle) {
            View root = inflater.inflate(R.layout.activity_vote_view, view, false);
            return root;
        }
    }


}
