package com.example.filipe1309.trabalho6_fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by filipe1309 on 26/09/14.
 */
public class Fragment_B extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, container, false);
        Button bt_insert_text = (Button) view.findViewById(R.id.bt_insert_text);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            bt_insert_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getBaseContext(), MyActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        } else {
            bt_insert_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText et_input = (EditText) getActivity().findViewById(R.id.et_input);
                    et_input.requestFocus();
                }
            });
        }
        return view;
    }
}
