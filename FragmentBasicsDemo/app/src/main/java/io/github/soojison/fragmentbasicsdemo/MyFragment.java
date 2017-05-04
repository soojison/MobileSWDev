package io.github.soojison.fragmentbasicsdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

// don't have to register in the manifest
// also use the one that's coming from the support library
public class MyFragment extends Fragment {

    // constants for tags
    public static final String TAG = "MyFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // second param is where to insert the fragment
        View rootView = inflater.inflate(R.layout.my_fragment, container, false);

        Button btn = (Button) rootView.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when a fragment is active, it must be attached to an activity
                // so you can access which activity you are attached to: getActivity() method
                Toast.makeText(getActivity(), "Wow! Button!!", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;

    }

}
