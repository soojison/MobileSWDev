package hu.ait.todoinflaterdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.layoutContent)
    LinearLayout layoutContent;

    @BindView(R.id.etTodo)
    EditText etTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSave)
    public void savePressed(Button btn) {
        final View viewTodo = // representing one line of the to-do item
                getLayoutInflater().inflate(R.layout.layout_todo, null);

        TextView tvTodo = (TextView) viewTodo.findViewById(R.id.tvTodo);
        tvTodo.setText(etTodo.getText().toString());

        // don't forget to do viewTodo.findViewById because if you just do
        // findViewById by itself tries to find it in the original layout
        // and the button does not exist
        Button btnDel = (Button) viewTodo.findViewById(R.id.btnDelete);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // must remove the list from the layoutContent
                layoutContent.removeView(viewTodo);
            }
        });

        layoutContent.addView(viewTodo);
        // if you want to add the new item to the top, you can use
        // layoutContent.addView(viewTodo, 0);
    }
}
