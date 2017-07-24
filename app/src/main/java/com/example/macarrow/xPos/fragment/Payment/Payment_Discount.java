package com.example.macarrow.xPos.fragment.Payment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.CarType_Services;
import com.example.macarrow.xPos.Services.Cooper_Services;
import com.example.macarrow.xPos.Services.Garage_Service;
import java.text.SimpleDateFormat;
import java.util.Map;

public class Payment_Discount extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        final int idx = mArgs.getInt("idx");
        final int result_charge = mArgs.getInt("result_charge");
        final String status = mArgs.getString("status");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.payment_discount, null);

        final Garage_Service garageService = new Garage_Service(getActivity(), "garage.db", null, 1);
        final Cooper_Services cooper_services = new Cooper_Services(getActivity(), "cooper.db", null, 1);
        final CarType_Services carTypeServices = new CarType_Services(getActivity(), "car_type.db", null, 1);

        final TextView Car_num = (TextView)view.findViewById(R.id.car_num);
        final LinearLayout Dc_skip = (LinearLayout)view.findViewById(R.id.dc_skip);
        final LinearLayout Dc_cooper = (LinearLayout)view.findViewById(R.id.dc_cooper);
        final LinearLayout Dc_day_car = (LinearLayout)view.findViewById(R.id.dc_day_car);
        final LinearLayout Dc_self = (LinearLayout)view.findViewById(R.id.dc_self);
        final EditText Dc_money = (EditText)view.findViewById(R.id.dc_money);
        final TextView Total_amount = (TextView)view.findViewById(R.id.total_amount);
        final TextView In_pay = (TextView)view.findViewById(R.id.in_pay);
        final TextView Pay_amount = (TextView)view.findViewById(R.id.pay_amount);
        final TextView Discount_cooper = (TextView)view.findViewById(R.id.discount_cooper);
        final TextView Discount_self = (TextView)view.findViewById(R.id.discount_self);
        final TextView Start_date = (TextView)view.findViewById(R.id.start_date);
        final TextView End_date = (TextView)view.findViewById(R.id.end_date);

        builder.setNegativeButton("닫기", null);
        builder.setView(view);

        final Map<String, Object> map = garageService.getResultForUpdate(idx);
        // 타이틀
        String car_num = (String) map.get("car_num");
        String CarNum = "";
        CarNum += "할인 - [ 차량번호 : ";
        CarNum += car_num;
        CarNum += " ]";
        Car_num.setText(CarNum);

        final String is_out = (String) map.get("is_out");
        final String is_daycar = (String) map.get("is_daycar");

        // 총 금액
        Total_amount.setText(result_charge+"");
        // 결제해야 될 금액
        In_pay.setText((result_charge-(Integer) map.get("pay_amount")-(Integer) map.get("discount_cooper")-(Integer) map.get("discount_self"))+"");
        // 현재까지 결제된 금액
        Pay_amount.setText((Integer) map.get("pay_amount") + "");
        // 지정 할인된 금액
        Discount_cooper.setText((Integer) map.get("discount_cooper") + "");
        // 임의 할인된 금액
        Discount_self.setText((Integer) map.get("discount_self") + "");
        // 날짜 구하기
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long start_date = (long) map.get("start_date");
        Start_date.setText(simpleDateFormat.format(start_date));

        if (is_out.equals("N")) {
            long end_date = System.currentTimeMillis();
            End_date.setText(simpleDateFormat.format(end_date));
        } else {
            long end_date = (long) map.get("end_date");
            End_date.setText(simpleDateFormat.format(end_date));
        }

        // EidtText가 눌릴때마다 감지하는 부분
        TextWatcher textWatcher = (new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Text가 바뀌고 동작할 코드
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Text가 바뀌기 전 동작할 코드
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.equals("")) {
                    Discount_self.setText((Integer) map.get("discount_self")+"");
                    In_pay.setText((result_charge-(Integer) map.get("pay_amount")-(Integer) map.get("discount_cooper")-(Integer) map.get("discount_self"))+"");
                } else if (s.length() < 1) {
                    Discount_self.setText((Integer) map.get("discount_self")+"");
                    In_pay.setText((result_charge-(Integer) map.get("pay_amount")-(Integer) map.get("discount_cooper")-(Integer) map.get("discount_self"))+"");
                } else if ((result_charge-(Integer) map.get("pay_amount")-(Integer) map.get("discount_cooper")-(Integer) map.get("discount_self")) - (int) Integer.parseInt(s.toString()) < 0) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setTitle("할인 금액이 결제 할 금액 보다 큽니다");
                    adb.setNegativeButton("닫기", null);
                    adb.show();
                    Dc_money.setText("");
                } else {
                    int inPay = result_charge - ((Integer) map.get("discount_self") + (int) Integer.parseInt(s.toString()));
                    In_pay.setText(inPay+"");
                    Discount_self.setText(((Integer) map.get("discount_self") + (int) Integer.parseInt(s.toString()))+"");
                }
            }
        });
        Dc_money.addTextChangedListener(textWatcher);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int discount_self = Integer.parseInt(Discount_self.getText().toString());
                int discount_cooper = Integer.parseInt(Discount_cooper.getText().toString());

                long end_date = 0;
                if (is_out.equals("N")) {
                    end_date = System.currentTimeMillis();
                } else {
                    end_date = (long) map.get("end_date");
                }

                switch (v.getId()) {

                    case R.id.dc_skip:

                        if (discount_self > 0) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setTitle("임의 할인을 하려면 임의 할인 버튼을 눌러주세요");
                            adb.setNegativeButton("닫기", null);
                            adb.show();
                            return;

                        } else {
                            Bundle args = new Bundle();
                            args.putInt("idx", idx);
                            args.putInt("total_amount", result_charge);
                            args.putLong("end_date", end_date);
                            args.putString("status", status);
                            Payment_Input payment_input = new Payment_Input();
                            payment_input.setArguments(args);
                            payment_input.setCancelable(false);
                            payment_input.show(getFragmentManager(), "payment_input");
                        }
                        dismiss();
                        break;

                    case R.id.dc_cooper:

                        if (discount_self > 0) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setTitle("임의 할인을 하려면 임의 할인 버튼을 눌러주세요");
                            adb.setNegativeButton("닫기", null);
                            adb.show();
                            return;

                        } else if (discount_cooper > 0) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setTitle("이미 지정주차 할인을 받았습니다");
                            adb.setNegativeButton("닫기", null);
                            adb.show();
                            return;

                        } else if (cooper_services.cooper() < 1) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setTitle("지정주차 등록을 해주세요");
                            adb.setNegativeButton("확인", null);
                            adb.show();
                            return;

                        } else {
                            Bundle args = new Bundle();
                            args.putInt("idx", idx);
                            args.putInt("total_amount", result_charge);
                            args.putLong("end_date", end_date);
                            args.putString("status", status);
                            Dc_Cooper dc_cooper = new Dc_Cooper();
                            dc_cooper.setArguments(args);
                            dc_cooper.setCancelable(false);
                            dc_cooper.show(getFragmentManager(), "dc_cooper");
                        }
                        dismiss();
                        break;

                    case R.id.dc_day_car:

                        if (discount_self > 0) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setTitle("임의 할인을 하려면 임의 할인 버튼을 눌러주세요");
                            adb.setNegativeButton("닫기", null);
                            adb.show();
                            return;

                        } else if (carTypeServices.car_type("Y") < 1) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setTitle("일차등록을 해주세요");
                            adb.setNegativeButton("확인", null);
                            adb.show();
                            return;

                        } else {
                            Bundle args = new Bundle();
                            args.putInt("idx", idx);
                            args.putLong("end_date", end_date);
                            args.putString("status", status);
                            Dc_Day_Car dc_day_car = new Dc_Day_Car();
                            dc_day_car.setArguments(args);
                            dc_day_car.setCancelable(false);
                            dc_day_car.show(getFragmentManager(), "dc_day_car");
                        }
                        dismiss();
                        break;

                    case R.id.dc_self:

                        if (discount_self <= 0) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setTitle("임의 할인을 하려면 임의 할인 금액을 입력해주세요");
                            adb.setNegativeButton("닫기", null);
                            adb.show();
                            return;
                        } else {
                            Bundle args = new Bundle();
                            args.putInt("idx", idx);
                            args.putInt("total_amount", result_charge);
                            args.putInt("discount_self", discount_self);
                            args.putLong("end_date", end_date);
                            args.putString("status", status);
                            Payment_Input payment_input = new Payment_Input();
                            payment_input.setArguments(args);
                            payment_input.setCancelable(false);
                            payment_input.show(getFragmentManager(), "payment_input");
                        }
                        dismiss();
                        break;
                }
            }
        };
        Dc_skip.setOnClickListener(onClickListener);
        Dc_cooper.setOnClickListener(onClickListener);
        Dc_day_car.setOnClickListener(onClickListener);
        Dc_self.setOnClickListener(onClickListener);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(835, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}