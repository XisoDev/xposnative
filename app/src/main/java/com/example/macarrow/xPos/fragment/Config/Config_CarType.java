package com.example.macarrow.xPos.fragment.Config;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.melnykov.fab.FloatingActionButton;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.CarType_Services;
import com.example.macarrow.xPos.adapter.CarTypeViewAdapter;
import java.util.List;
import java.util.Map;

public class Config_CarType extends Fragment {

    private FloatingActionButton fab;
    private String is_daycar;

    public Config_CarType() {}

    @SuppressLint("ValidFragment")
    public Config_CarType(String is_daycar) { this.is_daycar = is_daycar; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.config_cartype, container, false);
        final CarType_Services carTypeServices = new CarType_Services(getActivity(), "car_type.db", null, 1);
        final List<Map<String, Object>> list = carTypeServices.getResult(is_daycar);

        final ListView Car_type_List = (ListView) view.findViewById(R.id.cartype_list);
        final CarTypeViewAdapter adapter = new CarTypeViewAdapter(getActivity(), R.layout.config_cartype_item, list);
        //adapter.notifyDataSetChanged();
        //adapter.notifyDataSetInvalidated();
        Car_type_List.setAdapter(adapter);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToListView(Car_type_List);
        fab.setOnClickListener(new View.OnClickListener() {

            private FragmentManager fm = getFragmentManager();

            @Override
            public void onClick(View v) {

                final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

                View view = getActivity().getLayoutInflater().inflate(R.layout.config_addcartype, null);
                final TextView titleCarType = (TextView)view.findViewById(R.id.title_car_type);
                final Button insertCartype = (Button)view.findViewById(R.id.insert_cartype);
                final Button closeCartype = (Button)view.findViewById(R.id.close_cartype);
                final EditText Car_type_title = (EditText)view.findViewById(R.id.car_type_title);
                final EditText Minute_unit = (EditText)view.findViewById(R.id.minute_unit);
                final EditText Minute_free = (EditText)view.findViewById(R.id.minute_free);
                final EditText Amount_unit = (EditText)view.findViewById(R.id.amount_unit);
                final EditText Basic_amount = (EditText)view.findViewById(R.id.basic_amount);
                final EditText Basic_minute = (EditText)view.findViewById(R.id.basic_minute);

                adb.setView(view);
                // 다이얼 로그 크기 조정
                final Dialog dialog = adb.create();
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.copyFrom(dialog.getWindow().getAttributes());
                params.width = 845;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                Window window = dialog.getWindow();
                window.setAttributes(params);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                if (is_daycar.equals("N")) {
                    titleCarType.setText("차종 추가");
                } if (is_daycar.equals("Y")) {
                    titleCarType.setText("일차 추가");
                }

                insertCartype.setVisibility(View.VISIBLE);

                View.OnClickListener clickListener = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {

                            case R.id.insert_cartype :

                                try {

                                    if (Car_type_title.getText().toString().equals("")) {
                                        Toast.makeText(v.getContext(), "차종명을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                        return;

                                    } else if (Minute_free.getText().toString().equals("")) {
                                        Toast.makeText(v.getContext(), "최초무료시간을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                        return;

                                    } else if (Basic_amount.getText().toString().equals("")) {
                                        Toast.makeText(v.getContext(), "기본요금을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                        return;

                                    } else if (Basic_minute.getText().toString().equals("")) {
                                        Toast.makeText(v.getContext(), "기본시간을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                        return;

                                    } else if (Amount_unit.getText().toString().equals("")) {
                                        Toast.makeText(v.getContext(), "추가요금을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                        return;

                                    } else if (Minute_unit.getText().toString().equals("")) {
                                        Toast.makeText(v.getContext(), "추가요금 단위(분)을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                        return;

                                    }

                                } catch (Exception e) {

                                    e.printStackTrace();

                                }

                                String car_type_title = Car_type_title.getText().toString();
                                int minute_free = Integer.parseInt(Minute_free.getText().toString());
                                int basic_amount = Integer.parseInt(Basic_amount.getText().toString());
                                int basic_minute = Integer.parseInt(Basic_minute.getText().toString());
                                int amount_unit = Integer.parseInt(Amount_unit.getText().toString());
                                int minute_unit = Integer.parseInt(Minute_unit.getText().toString());
                                String isDaycar = is_daycar;
                                carTypeServices.insert(car_type_title, minute_free, basic_amount, basic_minute, amount_unit, minute_unit, isDaycar);

                                fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType(is_daycar)).commit();
                                dialog.dismiss();
                                break;

                            case R.id.close_cartype :

                                fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType(is_daycar)).commit();
                                dialog.dismiss();
                                break;

                        }
                    }
                };

                insertCartype.setOnClickListener(clickListener);
                closeCartype.setOnClickListener(clickListener);

            }
        });

        Car_type_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private FragmentManager fm = getFragmentManager();

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                final int idx = (int) list.get(position).get("idx");
                Car_type_List.setAdapter(adapter);

                final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

                View v = getActivity().getLayoutInflater().inflate(R.layout.config_addcartype, null);
                final TextView titleCarType = (TextView)v.findViewById(R.id.title_car_type);
                final Button updateCartype = (Button)v.findViewById(R.id.update_cartype);
                final Button deleteCartype = (Button)v.findViewById(R.id.delete_cartype);
                final Button closeCartype = (Button)v.findViewById(R.id.close_cartype);
                final EditText Car_type_title = (EditText)v.findViewById(R.id.car_type_title);
                final EditText Minute_unit = (EditText)v.findViewById(R.id.minute_unit);
                final EditText Minute_free = (EditText)v.findViewById(R.id.minute_free);
                final EditText Amount_unit = (EditText)v.findViewById(R.id.amount_unit);
                final EditText Basic_amount = (EditText)v.findViewById(R.id.basic_amount);
                final EditText Basic_minute = (EditText)v.findViewById(R.id.basic_minute);

                adb.setView(v);
                // 다이얼 로그 크기 조정
                final Dialog dialog = adb.create();
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.copyFrom(dialog.getWindow().getAttributes());
                params.width = 845;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                Window window = dialog.getWindow();
                window.setAttributes(params);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                if (is_daycar.equals("N")) {
                    titleCarType.setText("차종 수정 / 삭제");
                } if (is_daycar.equals("Y")) {
                    titleCarType.setText("일차 수정 / 삭제");
                }

                updateCartype.setVisibility(View.VISIBLE);
                deleteCartype.setVisibility(View.VISIBLE);

                Map<String, Object> map = carTypeServices.getResultForUpdate(idx);
                Car_type_title.setText((String) map.get("car_type_title"));
                Minute_free.setText((Integer) map.get("minute_free") + "");
                Basic_amount.setText((Integer) map.get("basic_amount") + "");
                Basic_minute.setText((Integer) map.get("basic_minute") + "");
                Amount_unit.setText((Integer) map.get("amount_unit") + "");
                Minute_unit.setText((Integer) map.get("minute_unit") + "");

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

                                    } else if (Minute_free.getText().toString().equals("")) {
                                        Toast.makeText(v.getContext(), "최초무료시간을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                        return;

                                    } else if (Basic_amount.getText().toString().equals("")) {
                                        Toast.makeText(v.getContext(), "기본요금을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                        return;

                                    } else if (Basic_minute.getText().toString().equals("")) {
                                        Toast.makeText(v.getContext(), "기본시간을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                        return;

                                    } else if (Amount_unit.getText().toString().equals("")) {
                                        Toast.makeText(v.getContext(), "추가요금을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                        return;

                                    } else if (Minute_unit.getText().toString().equals("")) {
                                        Toast.makeText(v.getContext(), "추가요금 단위(분)을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                        return;

                                    }

                                } catch (Exception e) {

                                    e.printStackTrace();

                                }

                                String car_type_title = Car_type_title.getText().toString();
                                int minute_free = Integer.parseInt(Minute_free.getText().toString());
                                int basic_amount = Integer.parseInt(Basic_amount.getText().toString());
                                int basic_minute = Integer.parseInt(Basic_minute.getText().toString());
                                int amount_unit = Integer.parseInt(Amount_unit.getText().toString());
                                int minute_unit = Integer.parseInt(Minute_unit.getText().toString());

                                carTypeServices.update(car_type_title, minute_free, basic_amount, basic_minute, amount_unit, minute_unit, idx);
                                fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType(is_daycar)).commit();
                                dialog.dismiss();
                                break;

                            case R.id.delete_cartype :

                                carTypeServices.delete(idx);
                                fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType(is_daycar)).commit();
                                dialog.dismiss();
                                break;

                            case R.id.close_cartype :

                                fm.beginTransaction().replace(R.id.content_fragment, new Config_CarType(is_daycar)).commit();
                                dialog.dismiss();
                                break;

                        }
                    }
                };

                updateCartype.setOnClickListener(clickListener);
                deleteCartype.setOnClickListener(clickListener);
                closeCartype.setOnClickListener(clickListener);

            }
        });
        return view;
    }
}