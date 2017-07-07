package com.example.macarrow.xPos.fragment.Month;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Month_Service;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class Month_Add extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        final String status = mArgs.getString("status");
        final String car_num = mArgs.getString("car_num");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final Month_Service month_service = new Month_Service(getActivity(), "month.db", null, 1);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        if (status.equals("new")) {

            final View view = inflater.inflate(R.layout.month_add, null);
            final TextView Month_title = (TextView)view.findViewById(R.id.month_title);
            final EditText Car_num = (EditText)view.findViewById(R.id.car_num);
            final EditText Car_name = (EditText)view.findViewById(R.id.car_name);
            final EditText Car_type_title = (EditText)view.findViewById(R.id.car_type_title);
            final DatePicker Start_date = (DatePicker)view.findViewById(R.id.start_date);
            final DatePicker End_date = (DatePicker)view.findViewById(R.id.end_date);
            final EditText Amount = (EditText)view.findViewById(R.id.amount);
            final EditText User_name = (EditText)view.findViewById(R.id.user_name);
            final EditText Mobile = (EditText)view.findViewById(R.id.mobile);
            final Button Add_month = (Button)view.findViewById(R.id.add_month);
            final Button Close_month = (Button)view.findViewById(R.id.close_month);

            builder.setView(view);

            Month_title.setText("월차 추가");
            Add_month.setVisibility(View.VISIBLE);

            Start_date.init(Start_date.getYear(), Start_date.getMonth(), Start_date.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String startDate = String.format("%d. %d. %d", year, monthOfYear + 1, dayOfMonth);
                    Toast.makeText(getActivity(), startDate, Toast.LENGTH_SHORT).show();
                }
            });

            End_date.init(End_date.getYear(), End_date.getMonth(), End_date.getDayOfMonth() + 30, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String endDate = String.format("%d. %d. %d", year, monthOfYear + 1, dayOfMonth);
                    Toast.makeText(getActivity(), endDate, Toast.LENGTH_SHORT).show();

//                        Calendar end = new GregorianCalendar(End_date.getYear(), End_date.getMonth(), End_date.getDayOfMonth());
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//                        int end_date = Integer.parseInt(sdf.format(end.getTime()));
//                        Toast.makeText(getActivity(), end_date+"", Toast.LENGTH_SHORT).show();
                }
            });

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (v.getId()) {

                        case R.id.add_month:

                            if (Car_num.getText().toString().equals("")) {
                                Toast.makeText(v.getContext(), "차량 번호를 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (month_service.doubleCarNum(Car_num.getText().toString()) > 0) {
                                Toast.makeText(v.getContext(), "이미 등록된 차 번호입니다", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (Car_name.getText().toString().equals("")) {
                                Toast.makeText(v.getContext(), "차종을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (Car_type_title.getText().toString().equals("")) {
                                Toast.makeText(v.getContext(), "구분을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (Amount.getText().toString().equals("")) {
                                Toast.makeText(v.getContext(), "월차금액을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (User_name.getText().toString().equals("")) {
                                Toast.makeText(v.getContext(), "차주명을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (Mobile.getText().toString().equals("")) {
                                Toast.makeText(v.getContext(), "연락처를 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                return;

                            } else {

                                EditText amount = (EditText) view.findViewById(R.id.amount);
                                final int amounT = Integer.parseInt(amount.getText().toString());
                                final String car_num = Car_num.getText().toString();
                                final String car_name = Car_name.getText().toString();
                                final String car_type_title = Car_type_title.getText().toString();
                                final String user_name = User_name.getText().toString();
                                final String mobile = Mobile.getText().toString();

                                final int SDY = Start_date.getYear();
                                final int SDM = Start_date.getMonth() + 1;
                                final int SDD = Start_date.getDayOfMonth();
                                final int EDY = End_date.getYear();
                                final int EDM = End_date.getMonth() + 1;
                                final int EDD = End_date.getDayOfMonth();

                                Calendar start = new GregorianCalendar(Start_date.getYear(), Start_date.getMonth(), Start_date.getDayOfMonth());
                                Calendar end = new GregorianCalendar(End_date.getYear(), End_date.getMonth(), End_date.getDayOfMonth());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                final int start_date = Integer.parseInt(sdf.format(start.getTime()));
                                final int end_date = Integer.parseInt(sdf.format(end.getTime()));

                                month_service.insert(SDY, SDM, SDD, start_date, EDY, EDM, EDD, end_date, amounT, car_num, car_name, car_type_title, user_name, mobile);
                                Bundle args = new Bundle();
                                args.putString("status", "new");
                                args.putString("car_num", car_num);
                                Month_Pay month_pay = new Month_Pay();
                                month_pay.setArguments(args);
                                month_pay.setCancelable(false);
                                month_pay.show(getFragmentManager(), "month_pay");
                                dismiss();
                            }
                            break;

                        case R.id.close_month :

                            dismiss();
                            break;
                    }
                }
            };
            Add_month.setOnClickListener(clickListener);
            Close_month.setOnClickListener(clickListener);
        }

        if (status.equals("modify")) {

            final View view = inflater.inflate(R.layout.month_add, null);
            final TextView Month_title = (TextView)view.findViewById(R.id.month_title);
            final EditText Car_num = (EditText)view.findViewById(R.id.car_num);
            final EditText Car_name = (EditText)view.findViewById(R.id.car_name);
            final EditText Car_type_title = (EditText)view.findViewById(R.id.car_type_title);
            final DatePicker Start_date = (DatePicker)view.findViewById(R.id.start_date);
            final DatePicker End_date = (DatePicker)view.findViewById(R.id.end_date);
            final EditText Amount = (EditText)view.findViewById(R.id.amount);
            final LinearLayout Outstanding_amount_lay = (LinearLayout)view.findViewById(R.id.outstanding_amount_lay);
            final TextView Outstanding_amount = (TextView)view.findViewById(R.id.outstanding_amount);
            final LinearLayout Pay_amount_lay = (LinearLayout)view.findViewById(R.id.pay_amount_lay);
            final TextView Pay_amount = (TextView)view.findViewById(R.id.pay_amount);
            final EditText User_name = (EditText)view.findViewById(R.id.user_name);
            final EditText Mobile = (EditText)view.findViewById(R.id.mobile);
            final Switch Is_stop = (Switch)view.findViewById(R.id.is_stop);
            final LinearLayout Is_stop_lay = (LinearLayout)view.findViewById(R.id.is_stop_lay);
            final Button Update_month = (Button)view.findViewById(R.id.update_month);
            final Button Open_ext_month = (Button)view.findViewById(R.id.open_ext_month);
            final Button Open_pay_month = (Button)view.findViewById(R.id.open_pay_month);
            final Button Close_month = (Button)view.findViewById(R.id.close_month);

            builder.setView(view);

            Update_month.setVisibility(View.VISIBLE);
            Is_stop_lay.setVisibility(View.VISIBLE);
            Outstanding_amount_lay.setVisibility(View.VISIBLE);
            Pay_amount_lay.setVisibility(View.VISIBLE);

            // DB에서 데이터 가져오기
            final Map<String, Object> map = month_service.getResultForUpdate(car_num);
            final int idx = (int) map.get("idx");

            // 결제 완료시 결제창 안뜨게하기
            String is_paid =  (String) map.get("is_paid");
            if (is_paid.equals("N")) {
                Open_pay_month.setVisibility(View.VISIBLE);
            }
            // 미결제시 연장창 안뜨게하기
            if (is_paid.equals("Y")) {
                Open_ext_month.setVisibility(View.VISIBLE);
            }

            Month_title.setText((String) map.get("car_num") + " 월차 수정");
            Car_num.setText((String) map.get("car_num"));
            Car_name.setText((String) map.get("car_name"));
            Car_type_title.setText((String) map.get("car_type_title"));

            Car_num.setFocusable(false);
            Car_num.setClickable(false);

            // DatePicker로 get+1 set-1 해야지 제대로 보임
            Start_date.updateDate((Integer) map.get("start_date_y"), (Integer) map.get("start_date_m")-1, (Integer) map.get("start_date_d"));
            End_date.updateDate((Integer) map.get("end_date_y"), (Integer) map.get("end_date_m")-1, (Integer) map.get("end_date_d"));

            // Switch
            final String isStop = (String) map.get("is_stop");
            if (isStop.equals("N")) {
                Is_stop.setChecked(true);
                Is_stop.setText("활성");
            } else if (isStop.equals("Y")) {
                Is_stop.setChecked(false);
                Is_stop.setText("중단");
            }

            Is_stop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Is_stop.setText("활성");
                    } else if (!isChecked) {
                        Is_stop.setText("중단");
                    }
                }
            });

            Amount.setText((Integer) map.get("amount") + "");
            Pay_amount.setText((Integer) map.get("pay_amount") + "");
            // 미수금액
            Outstanding_amount.setText((Integer) map.get("amount")-(Integer) map.get("pay_amount")+"");
            User_name.setText((String) map.get("user_name"));
            Mobile.setText((String) map.get("mobile"));

            View.OnClickListener clickListener = new View.OnClickListener() {
                private FragmentManager fm = getFragmentManager();

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {

                        case R.id.update_month:

                            if (Car_name.getText().toString().equals("")) {
                                Toast.makeText(v.getContext(), "차종을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (Car_type_title.getText().toString().equals("")) {
                                Toast.makeText(v.getContext(), "구분을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (Amount.getText().toString().equals("")) {
                                Toast.makeText(v.getContext(), "월차금액을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (User_name.getText().toString().equals("")) {
                                Toast.makeText(v.getContext(), "차주명을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (Mobile.getText().toString().equals("")) {
                                Toast.makeText(v.getContext(), "연락처를 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                return;

                            } else {
                                String is_stop = "";
                                if (Is_stop.getText().equals("활성")) {
                                    is_stop = "N";
                                } else if (Is_stop.getText().equals("중단")) {
                                    is_stop = "Y";
                                }
                                int sdy = Start_date.getYear();
                                int sdm = Start_date.getMonth() + 1;
                                int sdd = Start_date.getDayOfMonth();
                                int edy = End_date.getYear();
                                int edm = End_date.getMonth() + 1;
                                int edd = End_date.getDayOfMonth();

                                Calendar start = new GregorianCalendar(Start_date.getYear(), Start_date.getMonth(), Start_date.getDayOfMonth());
                                Calendar end = new GregorianCalendar(End_date.getYear(), End_date.getMonth(), End_date.getDayOfMonth());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                final int start_date = Integer.parseInt(sdf.format(start.getTime()));
                                final int end_date = Integer.parseInt(sdf.format(end.getTime()));

                                EditText amount = (EditText) view.findViewById(R.id.amount);
                                int amounT = Integer.parseInt(amount.getText().toString());
                                String car_name = Car_name.getText().toString();
                                String car_type_title = Car_type_title.getText().toString();
                                String user_name = User_name.getText().toString();
                                String mobile = Mobile.getText().toString();
                                month_service.update(sdy, sdm, sdd, start_date, edy, edm, edd, end_date, amounT, car_name, car_type_title, user_name, mobile, is_stop, idx);
                            }
                            dismiss();
                            break;

                        case R.id.open_ext_month :

                            final View view = getActivity().getLayoutInflater().inflate(R.layout.month_add, null);
                            final TextView Month_title = (TextView)view.findViewById(R.id.month_title);
                            final EditText Car_num = (EditText)view.findViewById(R.id.car_num);
                            final EditText Car_name = (EditText)view.findViewById(R.id.car_name);
                            final EditText Car_type_title = (EditText)view.findViewById(R.id.car_type_title);
                            final DatePicker Start_date = (DatePicker)view.findViewById(R.id.start_date);
                            final DatePicker End_date = (DatePicker)view.findViewById(R.id.end_date);
                            final EditText Amount = (EditText)view.findViewById(R.id.amount);
                            final EditText User_name = (EditText)view.findViewById(R.id.user_name);
                            final EditText Mobile = (EditText)view.findViewById(R.id.mobile);
                            final Button Add_month = (Button)view.findViewById(R.id.add_month);
                            final Button Close_month = (Button)view.findViewById(R.id.close_month);

                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setView(view);
                            // 다이얼 로그 크기 조정
                            final Dialog monthExt = adb.create();
                            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                            params.copyFrom(monthExt.getWindow().getAttributes());
                            params.width = 845;
                            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            monthExt.show();
                            Window window = monthExt.getWindow();
                            window.setAttributes(params);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            Add_month.setVisibility(View.VISIBLE);
                            Add_month.setText("연장");

                            // DB에서 데이터 가져오기
                            final Map<String, Object> map = month_service.getResultForUpdate(car_num);
                            final int idx = (int) map.get("idx");

                            final String carNum = (String) map.get("car_num");
                            Month_title.setText(carNum + " 월차 연장");
                            Car_num.setText(carNum);
                            Car_num.setFocusable(false);
                            Car_num.setClickable(false);
                            Car_name.setText((String) map.get("car_name"));
                            Car_type_title.setText((String) map.get("car_type_title"));

                            // DatePicker로 get+1 set-1 해야지 제대로 보임
                            Start_date.updateDate((Integer) map.get("start_date_y"), (Integer) map.get("start_date_m")-1, (Integer) map.get("start_date_d"));
                            End_date.updateDate((Integer) map.get("end_date_y"), (Integer) map.get("end_date_m")-1, (Integer) map.get("end_date_d"));

                            Start_date.init(Start_date.getYear(), Start_date.getMonth(), Start_date.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                                @Override
                                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                }
                            });

                            End_date.init(End_date.getYear(), End_date.getMonth(), End_date.getDayOfMonth() + 30, new DatePicker.OnDateChangedListener() {
                                @Override
                                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                }
                            });

                            Amount.setText((Integer) map.get("amount") + "");
                            User_name.setText((String) map.get("user_name"));
                            Mobile.setText((String) map.get("mobile"));

                            View.OnClickListener clickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    switch (v.getId()) {

                                        case R.id.add_month:

                                            if (Car_name.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "차종을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Car_type_title.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "구분을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Amount.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "월차금액을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (User_name.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "차주명을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Mobile.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "연락처를 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else {

                                                EditText amount = (EditText) view.findViewById(R.id.amount);
                                                final int amounT = Integer.parseInt(amount.getText().toString());
                                                final String car_name = Car_name.getText().toString();
                                                final String car_type_title = Car_type_title.getText().toString();
                                                final String user_name = User_name.getText().toString();
                                                final String mobile = Mobile.getText().toString();

                                                int sdy = Start_date.getYear();
                                                int sdm = Start_date.getMonth() + 1;
                                                int sdd = Start_date.getDayOfMonth();
                                                int edy = End_date.getYear();
                                                int edm = End_date.getMonth() + 1;
                                                int edd = End_date.getDayOfMonth();

                                                Calendar start = new GregorianCalendar(Start_date.getYear(), Start_date.getMonth(), Start_date.getDayOfMonth());
                                                Calendar end = new GregorianCalendar(End_date.getYear(), End_date.getMonth(), End_date.getDayOfMonth());
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                                final int start_date = Integer.parseInt(sdf.format(start.getTime()));
                                                final int end_date = Integer.parseInt(sdf.format(end.getTime()));

                                                month_service.update(sdy, sdm, sdd, start_date, edy, edm, edd, end_date, amounT, car_name, car_type_title, user_name, mobile, "N", idx);
                                                month_service.updatePay(0, edy, edm, edd, end_date, "N", idx);
                                                monthExt.dismiss();
                                                Bundle args = new Bundle();
                                                args.putString("status", "new");
                                                args.putString("car_num", carNum);
                                                Month_Pay month_pay = new Month_Pay();
                                                month_pay.setArguments(args);
                                                month_pay.setCancelable(false);
                                                month_pay.show(getFragmentManager(), "month_pay");
                                            }
                                            dismiss();
                                            break;

                                        case R.id.close_month :

                                            monthExt.dismiss();
                                            break;
                                    }
                                }
                            };
                            Add_month.setOnClickListener(clickListener);
                            Close_month.setOnClickListener(clickListener);

                            break;

                        case R.id.open_pay_month :

                            Bundle args = new Bundle();
                            args.putString("status", "add");
                            args.putString("car_num", car_num);
                            Month_Pay month_pay = new Month_Pay();
                            month_pay.setArguments(args);
                            month_pay.setCancelable(false);
                            month_pay.show(getFragmentManager(), "month_pay");
                            dismiss();
                            break;

                        case R.id.close_month :

                            dismiss();
                            break;
                    }
                }
            };
            Update_month.setOnClickListener(clickListener);
            Open_ext_month.setOnClickListener(clickListener);
            Open_pay_month.setOnClickListener(clickListener);
            Close_month.setOnClickListener(clickListener);
        }

        return builder.create();
    }
    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(835, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}