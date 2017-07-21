package com.example.macarrow.xPos.fragment.Garage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.Services.Payment_Services;
import com.example.macarrow.xPos.fragment.Current_Fragment;
import com.example.macarrow.xPos.fragment.History_Fragment;
import com.example.macarrow.xPos.fragment.Payment.Payment_Discount;
import java.text.SimpleDateFormat;
import java.util.Map;

public class Garage_View extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        final String status = mArgs.getString("status");
        final int idx = mArgs.getInt("idx");
        final int result_charge = mArgs.getInt("result_charge");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final Garage_Service garageService = new Garage_Service(getActivity(), "garage.db", null, 1);
        final Payment_Services paymentServices = new Payment_Services(getActivity(), "payment.db", null, 1);
        final Map<String, Object> map = garageService.getResultForUpdate(idx);
        final int month_idx = (int) map.get("month_idx");
        final String is_daycar = (String) map.get("is_daycar");
        final String car_num = (String) map.get("car_num");
        final long startDate = (long) map.get("start_date");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final long endDate = (long) map.get("end_date");
        final int discount_cooper = (Integer) map.get("discount_cooper");
        final int discount_self = (Integer) map.get("discount_self");
        final int pay_amount = (Integer) map.get("pay_amount");
        final String car_type_title = (String) map.get("car_type_title");
        final int minute_free = (Integer) map.get("minute_free");
        final int basic_amount = (Integer) map.get("basic_amount");
        final int basic_minute = (Integer) map.get("basic_minute");
        final int amount_unit = (Integer) map.get("amount_unit");
        final int minute_unit = (Integer) map.get("minute_unit");
        final String isPaid = (String) map.get("is_paid");
        final String isOut = (String) map.get("is_out");
        final String isCancel = (String) map.get("is_cancel");
        final long regdate = (long) map.get("regdate");

        final View view = inflater.inflate(R.layout.garage_view, null);
        final LinearLayout Out_car = (LinearLayout)view.findViewById(R.id.out_car);
        final LinearLayout Pay_car = (LinearLayout)view.findViewById(R.id.pay_car);
        final LinearLayout Cancel_car = (LinearLayout)view.findViewById(R.id.cancel_car);
        final LinearLayout Cancel_out = (LinearLayout)view.findViewById(R.id.cancel_out);
        final LinearLayout Printin_car = (LinearLayout)view.findViewById(R.id.printin_car);
        final LinearLayout Pay_status = (LinearLayout)view.findViewById(R.id.pay_status);
        final TextView Car_num = (TextView)view.findViewById(R.id.car_num);
        final TextView Start_date = (TextView)view.findViewById(R.id.start_date);
        final TextView End_date = (TextView)view.findViewById(R.id.end_date);
        final TextView Month_idx = (TextView)view.findViewById(R.id.month_idx);
        final TextView Result_charge = (TextView)view.findViewById(R.id.result_charge);
        final TextView Discount_cooper = (TextView)view.findViewById(R.id.discount_cooper);
        final TextView Discount_self = (TextView)view.findViewById(R.id.discount_self);
        final TextView Pay_amount = (TextView)view.findViewById(R.id.pay_amount);
        final TextView Car_type_title = (TextView)view.findViewById(R.id.car_type_title);
        final TextView Minute_free = (TextView)view.findViewById(R.id.minute_free);
        final TextView Basic_amount = (TextView)view.findViewById(R.id.basic_amount);
        final TextView Basic_minute = (TextView)view.findViewById(R.id.basic_minute);
        final TextView Amount_unit = (TextView)view.findViewById(R.id.amount_unit);
        final TextView Minute_unit = (TextView)view.findViewById(R.id.minute_unit);
        final TextView Is_out = (TextView)view.findViewById(R.id.is_out);
        final TextView Is_cancel = (TextView)view.findViewById(R.id.is_cancel);
        builder.setNegativeButton("닫기", null);
        builder.setView(view);

        if (month_idx > 0) {
            Pay_status.setVisibility(View.GONE);
        }

        if (endDate <= 0) {
            Out_car.setVisibility(view.VISIBLE);
            Cancel_car.setVisibility(view.VISIBLE);
            Printin_car.setVisibility(view.VISIBLE);
        }  else if (isPaid.equals("Y")) {
            Cancel_out.setVisibility(view.VISIBLE);
        } else if (isPaid.equals("N") && isCancel.equals("N")) {
            Pay_car.setVisibility(view.VISIBLE);
            Cancel_out.setVisibility(view.VISIBLE);
        }

        Result_charge.setText(result_charge + "원");

        String CarNum = "";
        CarNum += "입출차 정보 열람 - [ 차량번호 : ";
        CarNum += car_num;
        CarNum += " ]";
        Car_num.setText(CarNum);

        Start_date.setText(simpleDateFormat.format(startDate));
        if (endDate <= 0) {
            End_date.setText("");
        } else {
            End_date.setText(simpleDateFormat.format(endDate));
        }

        int monthIdx = (int) map.get("month_idx");
        if (monthIdx == 0) {
            Month_idx.setText("일반고객");
        } if (monthIdx > 0) {
            Month_idx.setText("월차고객");
        } if (is_daycar.equals("Y")) {
            Month_idx.setText("일차고객");
        }

        Discount_cooper.setText(discount_cooper + "원");
        Discount_self.setText(discount_self + "원");
        Pay_amount.setText(pay_amount + "원");
        Car_type_title.setText(car_type_title);
        Minute_free.setText(minute_free + "분");
        Basic_amount.setText(basic_amount + "원");
        Basic_minute.setText(basic_minute + "분");
        Amount_unit.setText(amount_unit + "원");
        Minute_unit.setText(minute_unit + "분");


        if (isOut.equals("Y")) {
            Is_out.setText("출차됨");
        }else {
            Is_out.setText("입차중");
        }

        if (isCancel.equals("Y")) {
            Is_cancel.setText("취소됨");
        }else {
            Is_cancel.setText("아니오");
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fm = getFragmentManager();
                switch (v.getId()) {

                    case R.id.pay_car:
                    case R.id.out_car:

                        if (month_idx > 0) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setTitle("출차 - " + car_num);
                            adb.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String is_out = "Y";
                                    String is_paid = "";
                                    int total_amount = 0;
                                    long endDate = System.currentTimeMillis();
                                    if (total_amount > 0) {
                                        is_paid = "N";
                                    } else {
                                        is_paid = "Y";
                                    }
                                    garageService.updateForceOut(is_out, total_amount, endDate, is_paid, idx);
                                    if (status.equals("current")) {
                                        fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                                    } else {
                                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(status)).commit();
                                    }
                                }
                            });
                            adb.setNegativeButton("닫기", null);
                            adb.show();
                        } if (is_daycar.equals("Y") && isPaid.equals("Y")) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setTitle("출차 - " + car_num);
                            adb.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String is_out = "Y";
                                    long endDate = System.currentTimeMillis();
                                    garageService.outDayCar(endDate, is_out, idx);
                                    if (status.equals("current")) {
                                        fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                                    } else {
                                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(status)).commit();
                                    }
                                }
                            });
                            adb.setNegativeButton("닫기", null);
                            adb.show();

                        } else {
                            Bundle args = new Bundle();
                            args.putInt("idx", idx);
                            args.putInt("result_charge", result_charge);
                            Payment_Discount payment_discount = new Payment_Discount();
                            payment_discount.setArguments(args);
                            payment_discount.setCancelable(false);
                            payment_discount.show(getFragmentManager(), "payment_discount");
                        }
                        dismiss();
                        break;

                    case R.id.cancel_car:

                        long endDate = System.currentTimeMillis();
                        garageService.cancelCar(endDate, idx);
                        if (status.equals("current")) {
                            fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                        } else {
                            fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(status)).commit();
                        }
                        dismiss();
                        break;

                    case R.id.cancel_out:

                        long cancel_date = System.currentTimeMillis();

                        garageService.cancelOutCar(idx);
                        paymentServices.update( cancel_date, "Y", regdate);
                        if (status.equals("current")) {
                            fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                        } else {
                            fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(status)).commit();
                        }
                        dismiss();
                        break;

                    case R.id.printin_car:

                        if (status.equals("current")) {
                            fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                        } else {
                            fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(status)).commit();
                        }
                        dismiss();
                        break;
                }
            }
        };
        Out_car.setOnClickListener(clickListener);
        Pay_car.setOnClickListener(clickListener);
        Cancel_car.setOnClickListener(clickListener);
        Cancel_out.setOnClickListener(clickListener);
        Printin_car.setOnClickListener(clickListener);

        return builder.create();
    }
    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(835, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}