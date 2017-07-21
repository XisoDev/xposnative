package com.example.macarrow.xPos.fragment.Config;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.CarType_Services;
import java.util.Map;

public class Config_DayCar_Add extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        final int idx = mArgs.getInt("idx");
        final String status = mArgs.getString("status");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.config_add_daycar, null);
        final CarType_Services carTypeServices = new CarType_Services(getActivity(), "car_type.db", null, 1);
        builder.setView(view);

        if (status.equals("new")) {

            final TextView titleDayCar = (TextView)view.findViewById(R.id.title_day_car);
            final Button insertCartype = (Button)view.findViewById(R.id.insert_cartype);
            final Button closeCartype = (Button)view.findViewById(R.id.close_cartype);
            final EditText Car_type_title = (EditText)view.findViewById(R.id.car_type_title);
            final EditText Basic_amount = (EditText)view.findViewById(R.id.basic_amount);

            titleDayCar.setText("일차 추가");
            insertCartype.setVisibility(View.VISIBLE);

            View.OnClickListener clickListener = new View.OnClickListener() {
                private FragmentManager fm = getFragmentManager();
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {

                        case R.id.insert_cartype :

                            try {

                                if (Car_type_title.getText().toString().equals("")) {
                                    Toast.makeText(v.getContext(), "차종명을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                    return;

                                } else if (Basic_amount.getText().toString().equals("")) {
                                    Toast.makeText(v.getContext(), "기본요금을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                    return;

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String car_type_title = Car_type_title.getText().toString();
                            int minute_free = 0;
                            int basic_amount = Integer.parseInt(Basic_amount.getText().toString());
                            int basic_minute = 0;
                            int amount_unit = 0;
                            int minute_unit = 0;
                            String isDaycar = "Y";
                            carTypeServices.insert(car_type_title, minute_free, basic_amount, basic_minute, amount_unit, minute_unit, isDaycar);

                            fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType("day")).commit();
                            dismiss();
                            break;

                        case R.id.close_cartype :

                            fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType("day")).commit();
                            dismiss();
                            break;
                    }
                }
            };
            insertCartype.setOnClickListener(clickListener);
            closeCartype.setOnClickListener(clickListener);
        }

        if (status.equals("modify")) {

            final TextView titleDayCar = (TextView)view.findViewById(R.id.title_day_car);
            final Button updateCartype = (Button)view.findViewById(R.id.update_cartype);
            final Button deleteCartype = (Button)view.findViewById(R.id.delete_cartype);
            final Button closeCartype = (Button)view.findViewById(R.id.close_cartype);
            final EditText Car_type_title = (EditText)view.findViewById(R.id.car_type_title);
            final EditText Basic_amount = (EditText)view.findViewById(R.id.basic_amount);

            titleDayCar.setText("일차 수정 / 삭제");
            updateCartype.setVisibility(View.VISIBLE);
            deleteCartype.setVisibility(View.VISIBLE);

            Map<String, Object> map = carTypeServices.getResultForUpdate(idx);
            Car_type_title.setText((String) map.get("car_type_title"));
            Basic_amount.setText((Integer) map.get("basic_amount") + "");

            View.OnClickListener clickListener = new View.OnClickListener() {

                private FragmentManager fm = getFragmentManager();

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {

                        case R.id.update_cartype :

                            try {

                                if (Car_type_title.getText().toString().equals("")) {
                                    Toast.makeText(v.getContext(), "차종명을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                    return;

                                } else if (Basic_amount.getText().toString().equals("")) {
                                    Toast.makeText(v.getContext(), "기본요금을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                    return;

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String car_type_title = Car_type_title.getText().toString();
                            int minute_free = 0;
                            int basic_amount = Integer.parseInt(Basic_amount.getText().toString());
                            int basic_minute = 0;
                            int amount_unit = 0;
                            int minute_unit = 0;

                            carTypeServices.update(car_type_title, minute_free, basic_amount, basic_minute, amount_unit, minute_unit, idx);
                            fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType("day")).commit();
                            dismiss();
                            break;

                        case R.id.delete_cartype :

                            carTypeServices.delete(idx);
                            fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType("day")).commit();
                            dismiss();
                            break;

                        case R.id.close_cartype :

                            fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType("day")).commit();
                            dismiss();
                            break;
                    }
                }
            };
            updateCartype.setOnClickListener(clickListener);
            deleteCartype.setOnClickListener(clickListener);
            closeCartype.setOnClickListener(clickListener);
        }
        return builder.create();
    }
    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(535, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}