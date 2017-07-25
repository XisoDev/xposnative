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
import com.example.macarrow.xPos.adapter.PanelDayCarTypeViewAdapter;
import java.util.List;
import java.util.Map;

public class Dc_Day_Car extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        final int idx = mArgs.getInt("idx");
        final long endDate = mArgs.getLong("end_date");
        final String status = mArgs.getString("status");

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
                int total_amount = (int) list.get(position).get("basic_amount");

                garageService.updateDayCar(
                        car_type_title,
                        0,
                        0,
                        0,
                        0,
                        0,
                        total_amount,
                        "Y",
                        idx);

                Bundle args = new Bundle();
                args.putInt("idx", idx);
                args.putInt("total_amount", total_amount);
                args.putLong("end_date", endDate);
                args.putString("status", status);
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
        window.setLayout(835, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}