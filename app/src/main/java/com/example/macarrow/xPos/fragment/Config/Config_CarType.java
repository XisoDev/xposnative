package com.example.macarrow.xPos.fragment.Config;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.melnykov.fab.FloatingActionButton;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.CarType_Services;
import com.example.macarrow.xPos.adapter.CarTypeViewAdapter;
import java.util.List;
import java.util.Map;

public class Config_CarType extends Fragment {

    private FloatingActionButton fab;
    private String is_daycar;

    @SuppressLint("ValidFragment")
    public Config_CarType(String is_daycar) { this.is_daycar = is_daycar; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.config_cartype, container, false);
        final CarType_Services carTypeServices = new CarType_Services(getActivity(), "car_type.db", null, 1);
        final TextView Config_cartype = (TextView)view.findViewById(R.id.config_cartype);
        final TextView Config_dayCar = (TextView)view.findViewById(R.id.config_dayCar);
        final ListView Car_type_List = (ListView) view.findViewById(R.id.cartype_list);

        switch (is_daycar) {

            case "nomal":
                is_daycar = "N";
                break;

            case "day":
                is_daycar = "Y";
                break;

        }

        final List<Map<String, Object>> list = carTypeServices.getResult(is_daycar);
        final CarTypeViewAdapter adapter = new CarTypeViewAdapter(getActivity(), R.layout.config_cartype_item, list);
        adapter.notifyDataSetChanged();
        Car_type_List.setAdapter(adapter);


        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToListView(Car_type_List);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString("status", "new");
                args.putString("is_daycar", is_daycar);
                Config_CarType_Add config_carType_add = new Config_CarType_Add();
                config_carType_add.setArguments(args);
                config_carType_add.setCancelable(false);
                config_carType_add.show(getFragmentManager(), "config_carType_add");

            }
        });

        Car_type_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                final int idx = (int) list.get(position).get("idx");

                Bundle args = new Bundle();
                args.putInt("idx", idx);
                args.putString("status", "modify");
                args.putString("is_daycar", is_daycar);
                Config_CarType_Add config_carType_add = new Config_CarType_Add();
                config_carType_add.setArguments(args);
                config_carType_add.setCancelable(false);
                config_carType_add.show(getFragmentManager(), "config_carType_add");

            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {

            private FragmentManager fm = getFragmentManager();

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.config_cartype:

                        is_daycar = "N";
                        fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType(is_daycar)).commit();
                        break;

                    case R.id.config_dayCar:

                        is_daycar = "Y";
                        fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType(is_daycar)).commit();
                        break;

                }
            }
        };

        Config_cartype.setOnClickListener(clickListener);
        Config_dayCar.setOnClickListener(clickListener);

        return view;
    }
}