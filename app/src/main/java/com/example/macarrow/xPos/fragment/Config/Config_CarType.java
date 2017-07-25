package com.example.macarrow.xPos.fragment.Config;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.macarrow.xPos.adapter.DayCarViewAdapter;
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

        final CarType_Services carTypeServices = new CarType_Services(getActivity(), "car_type.db", null, 1);
        View view = null;

        switch (is_daycar) {

            case "nomal":
                is_daycar = "N";

                view = inflater.inflate(R.layout.config_cartype, container, false);
                ListView Car_type_List = (ListView) view.findViewById(R.id.cartype_list);
                final List<Map<String, Object>> list = carTypeServices.getResult(is_daycar);
                CarTypeViewAdapter adapter = new CarTypeViewAdapter(getActivity(), R.layout.config_cartype_item, list);
                adapter.notifyDataSetChanged();
                Car_type_List.setAdapter(adapter);


                fab = (FloatingActionButton) view.findViewById(R.id.fab);
                fab.attachToListView(Car_type_List);
                fab.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Bundle args = new Bundle();
                        args.putString("status", "new");
                        args.putString("from", "cartype");
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
                        Config_CarType_Add config_carType_add = new Config_CarType_Add();
                        config_carType_add.setArguments(args);
                        config_carType_add.setCancelable(false);
                        config_carType_add.show(getFragmentManager(), "config_carType_add");
                    }
                });
                break;

            case "day":
                is_daycar = "Y";

                view = inflater.inflate(R.layout.config_daycar, container, false);
                ListView carTypeList = (ListView) view.findViewById(R.id.cartype_list);
                final List<Map<String, Object>> dlist = carTypeServices.getResult(is_daycar);
                DayCarViewAdapter dayCarViewAdapter = new DayCarViewAdapter(getActivity(), R.layout.config_daycar_item, dlist);
                dayCarViewAdapter.notifyDataSetChanged();
                carTypeList.setAdapter(dayCarViewAdapter);


                fab = (FloatingActionButton) view.findViewById(R.id.fab);
                fab.attachToListView(carTypeList);
                fab.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Bundle args = new Bundle();
                        args.putString("status", "new");
                        Config_DayCar_Add config_dayCar_add = new Config_DayCar_Add();
                        config_dayCar_add.setArguments(args);
                        config_dayCar_add.setCancelable(false);
                        config_dayCar_add.show(getFragmentManager(), "config_dayCar_add");
                    }
                });

                carTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        final int idx = (int) dlist.get(position).get("idx");

                        Bundle args = new Bundle();
                        args.putInt("idx", idx);
                        args.putString("status", "modify");
                        Config_DayCar_Add config_dayCar_add = new Config_DayCar_Add();
                        config_dayCar_add.setArguments(args);
                        config_dayCar_add.setCancelable(false);
                        config_dayCar_add.show(getFragmentManager(), "config_dayCar_add");
                    }
                });
                break;

        }

        final TextView Config_cartype = (TextView)view.findViewById(R.id.config_cartype);
        final TextView Config_dayCar = (TextView)view.findViewById(R.id.config_dayCar);

        if (is_daycar.equals("N")) {
            Config_cartype.setBackground(getResources().getDrawable(R.drawable.nav_bg_on));
            Config_cartype.setTextColor(getResources().getColor(R.color.nav_txt_on));
        } if (is_daycar.equals("Y")) {
            Config_dayCar.setBackground(getResources().getDrawable(R.drawable.nav_bg_on));
            Config_dayCar.setTextColor(getResources().getColor(R.color.nav_txt_on));
        }

        View.OnClickListener clickListener = new View.OnClickListener() {

            private FragmentManager fm = getFragmentManager();

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.config_cartype:

                        fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType("nomal")).commit();
                        break;

                    case R.id.config_dayCar:

                        fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType("day")).commit();
                        break;

                }
            }
        };

        Config_cartype.setOnClickListener(clickListener);
        Config_dayCar.setOnClickListener(clickListener);

        return view;
    }
}