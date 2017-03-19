package io.github.soojison.stopwatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.layoutMarkContent)
    LinearLayout layoutMarkContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.btnMark)
    public void savePressed(Button btn) {
        final View viewMark =
                getLayoutInflater().inflate(R.layout.layout_mark, null);
        TextView tvMark = (TextView) viewMark.findViewById(R.id.tvMark);
        tvMark.setText("Mark");

        Button btnDel = (Button) viewMark.findViewById(R.id.btnDelete);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutMarkContent.removeView(viewMark);
            }
        });

        layoutMarkContent.addView(viewMark, 0);
    }


}
