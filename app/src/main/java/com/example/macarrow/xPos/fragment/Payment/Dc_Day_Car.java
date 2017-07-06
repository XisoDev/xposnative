package com.example.macarrow.xPos.fragment.Payment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.CarType_Services;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.adapter.PanelCooperTypeViewAdapter;
import com.example.macarrow.xPos.adapter.PanelDayCarTypeViewAdapter;

import java.util.List;
import java.util.Map;

public class Dc_Day_Car extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        final int idx = mArgs.getInt("idx");
        final long endDate = mArgs.getLong("end_date");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.panel_cartypelist, null);

        final CarType_Services carType_services = new CarType_Services(getActivity(), "car_type.db", null, 1);
        final Garage_Service garageService = new Garage_Service(getActivity(), "garage.db", null, 1);

        final List<Map<String, Object>> list = carType_services.getResult("Y");
        TextView Title = (TextView)view.findViewById(R.id.title);
        GridView gridView = (GridView)view.findViewById(R.id.panel_cartypeList);
        PanelDayCarTypeViewAdapter adapter = new PanelDayCarTypeViewAdapter(getActivity(), R.layout.panel_daylist_item, list);
        gridView.setAdapter(adapter);

        builder.setNegativeButton("닫기", null);
        builder.setView(view);

        final Map<String, Object> map = garageService.getResultForUpdate(idx);
        // 타이틀
        Title.setText("일차 요금으로 변경");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String car_type_title = (String) list.get(position).get("car_type_title");
                int minute_unit = (int) list.get(position).get("minute_unit");
                int minute_free = (int) list.get(position).get("minute_free");
                int amount_unit = (int) list.get(position).get("amount_unit");
                int basic_amount = (int) list.get(position).get("basic_amount");
                int basic_minute = (int) list.get(position).get("basic_minute");

                double oneSecond = 1000;
                double oneMinute = oneSecond * 60;
                int result_charge = 0;
                double startDate = (long) map.get("start_date");
                double park_min = Math.floor(((double) System.currentTimeMillis() - startDate) / oneMinute);
                double free_min = (int) list.get(position).get("minute_free");
                double basic_min= (int) list.get(position).get("basic_minute");
                double minuteUnit = (int) list.get(position).get("minute_unit");

                if (park_min - free_min > 0) {
                    if(park_min - free_min - basic_min > 0){
                        double added_min = Math.ceil((park_min - free_min - basic_min) / minuteUnit);
                        result_charge = (int) (basic_amount + (added_min * amount_unit));
                    } else {
                        result_charge = basic_amount;
                    }
                }

                garageService.updateDayCar(
                        car_type_title,
                        minute_unit,
                        minute_free,
                        amount_unit,
                        basic_amount,
                        basic_minute,
                        idx);

                Bundle args = new Bundle();
                args.putInt("idx", idx);
                args.putInt("total_amount", result_charge);
                args.putLong("end_date", endDate);
                Payment_Input payment_input = new Payment_Input();
                payment_input.setArguments(args);
                payment_input.setCancelable(false);
                payment_input.show(getFragmentManager(), "payment_input");
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(845, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}