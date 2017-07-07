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
import com.example.macarrow.xPos.Services.Cooper_Services;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.adapter.PanelCooperTypeViewAdapter;
import java.util.List;
import java.util.Map;

public class Dc_Cooper extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        final int idx = mArgs.getInt("idx");
        final int totalAmount = mArgs.getInt("total_amount");
        final long endDate = mArgs.getLong("end_date");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.panel_cartypelist, null);

        final Cooper_Services cooper_services = new Cooper_Services(getActivity(), "cooper.db", null, 1);
        final Garage_Service garageService = new Garage_Service(getActivity(), "garage.db", null, 1);

        final List<Map<String, Object>> list = cooper_services.getResult();
        TextView Title = (TextView)view.findViewById(R.id.title);
        GridView gridView = (GridView)view.findViewById(R.id.panel_cartypeList);
        PanelCooperTypeViewAdapter adapter = new PanelCooperTypeViewAdapter(getActivity(), R.layout.panel_cooperlist_item, list);
        gridView.setAdapter(adapter);

        builder.setNegativeButton("닫기", null);
        builder.setView(view);

        final Map<String, Object> map = garageService.getResultForUpdate(idx);
        final long start_date = (long) map.get("start_date");
        // 타이틀
        String car_num = (String) map.get("car_num");
        String CarNum = "";
        CarNum += "지정 할인 선택 - [ 차량번호 : ";
        CarNum += car_num;
        CarNum += " ]";
        Title.setText(CarNum);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int cooper_idx = (int) list.get(position).get("idx");
                int minute_unit = (int) list.get(position).get("minute_unit");
                int minute_max = (int) list.get(position).get("minute_max");

                int amount_unit = (int) list.get(position).get("amount_unit");
                int basic_amount = 0;
                int basic_minute = 0;
                double startDate = start_date;

                double oneSecond = 1000;
                double oneMinute = oneSecond * 60;
                int cooper_charge = 0;
                double park_min = Math.floor(((double) endDate - startDate) / oneMinute);
                double free_min = minute_max;

                if (park_min - free_min > 0) {
                    if(park_min - free_min - basic_minute > 0){
                        double added_min = Math.ceil((park_min - free_min - basic_minute) / minute_unit);
                        cooper_charge = (int) (basic_amount + (added_min * amount_unit));
                    } else {
                        cooper_charge = basic_amount;
                    }
                }
                int discount_cooper = 0;
                if (totalAmount < cooper_charge) {
                    discount_cooper = totalAmount;
                } else {
                    discount_cooper = totalAmount - cooper_charge;
                }

                // 지정할인해서 요금이 더 커진다면?

//                garageService.updateCooper(
//                        cooper_idx,
//                        minute_unit,
//                        minute_max,
//                        amount_unit,
//                        basic_amount,
//                        basic_minute,
//                        idx);

                Bundle args = new Bundle();
                args.putInt("idx", idx);
                args.putInt("total_amount", totalAmount);
                args.putInt("cooper_idx", cooper_idx);
                args.putInt("discount_cooper", discount_cooper);
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
        window.setLayout(835, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}