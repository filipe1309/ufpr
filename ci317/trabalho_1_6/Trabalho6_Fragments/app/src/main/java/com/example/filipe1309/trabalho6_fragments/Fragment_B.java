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
        Button bt_func_0 = (Button) view.findViewById(R.id.bt_func_0);
        Button bt_func_1 = (Button) view.findViewById(R.id.bt_func_1);
        Button bt_func_2 = (Button) view.findViewById(R.id.bt_func_2);
        Button bt_func_3 = (Button) view.findViewById(R.id.bt_func_3);
        Button bt_func_4 = (Button) view.findViewById(R.id.bt_func_4);
        Button bt_func_5 = (Button) view.findViewById(R.id.bt_func_5);


        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            bt_func_0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getBaseContext(), MyActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("chosen_function", "função 0");
                    startActivity(intent);
                }
            });

            bt_func_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getBaseContext(), MyActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("chosen_function", "função 1");
                    startActivity(intent);
                }
            });

            bt_func_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getBaseContext(), MyActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("chosen_function", "função 2");
                    startActivity(intent);
                }
            });

            bt_func_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getBaseContext(), MyActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("chosen_function", "função 3");
                    startActivity(intent);
                }
            });

            bt_func_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getBaseContext(), MyActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("chosen_function", "função 4");
                    startActivity(intent);
                }
            });

            bt_func_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getBaseContext(), MyActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("chosen_function", "função 5");
                    startActivity(intent);
                }
            });
        } else {
            bt_func_0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText et_input = (EditText) getActivity().findViewById(R.id.et_input);
                    et_input.setText("função 0");
                    et_input.requestFocus();
                }
            });

            bt_func_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText et_input = (EditText) getActivity().findViewById(R.id.et_input);
                    et_input.setText("função 1");
                    et_input.requestFocus();
                }
            });

            bt_func_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText et_input = (EditText) getActivity().findViewById(R.id.et_input);
                    et_input.setText("função 2");
                    et_input.requestFocus();
                }
            });

            bt_func_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText et_input = (EditText) getActivity().findViewById(R.id.et_input);
                    et_input.setText("função 3");
                    et_input.requestFocus();
                }
            });

            bt_func_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText et_input = (EditText) getActivity().findViewById(R.id.et_input);
                    et_input.setText("função 4");
                    et_input.requestFocus();
                }
            });

            bt_func_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText et_input = (EditText) getActivity().findViewById(R.id.et_input);
                    et_input.setText("função 5");
                    et_input.requestFocus();
                }
            });
        }



        return view;
    }
}
