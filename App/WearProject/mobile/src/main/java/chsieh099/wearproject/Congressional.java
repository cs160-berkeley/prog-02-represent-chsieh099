package chsieh099.wearproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

public class Congressional extends AppCompatActivity {

    public ListAdapter list = new ListAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        Intent intent = getIntent();

        GridView lv = (GridView) findViewById(R.id.gridView);
        lv.setAdapter(list);
    }
}