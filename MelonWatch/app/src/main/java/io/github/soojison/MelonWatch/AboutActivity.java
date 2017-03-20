package io.github.soojison.MelonWatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    private List<Info> infoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private InfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolBarAbout = (Toolbar) findViewById(R.id.toolBarAbout);
        setSupportActionBar(toolBarAbout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolBarAbout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new InfoAdapter(infoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        prepareInfoData();
    }

    private void prepareInfoData() {
        Info info = new Info("Icon Design", "Sooji Son");
        infoList.add(info);
        info = new Info("Feature 1", "Toolbar");
        infoList.add(info);
        info = new Info("Feature 2", "Multiple Activities");
        infoList.add(info);
        info = new Info("Feature 3", "TimerTask");
        infoList.add(info);
        info = new Info("Feature 4", "Drawable Import");
        infoList.add(info);
        info = new Info("Feature 5", "RecyclerView");
        infoList.add(info);
        info = new Info("Feature 6", "ButterKnife");
        infoList.add(info);

        mAdapter.notifyDataSetChanged();
    }
}
