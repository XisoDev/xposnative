package com.example.macarrow.xPos.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.fragment.Config.Config_CarType;

public class Config_Fragment extends Fragment {

    public Config_Fragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.config, container, false);

        final TextView configDefault = (TextView)view.findViewById(R.id.config_default);
        final TextView configCartype = (TextView)view.findViewById(R.id.config_cartype);
        final TextView configDatabase = (TextView)view.findViewById(R.id.config_database);

        // Button case 처리
        View.OnClickListener clickListener = new View.OnClickListener() {

            private FragmentManager fm = getFragmentManager();

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.config_default:

                        break;

                    case R.id.config_cartype:

                        String carType;
                        carType = "nomal";
                        fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType(carType)).commit();
                        break;

                    case R.id.config_database:

                        break;
                }
            }
        };

        configDefault.setOnClickListener(clickListener);
        configCartype.setOnClickListener(clickListener);
        configDatabase.setOnClickListener(clickListener);

        return view;
    }
}