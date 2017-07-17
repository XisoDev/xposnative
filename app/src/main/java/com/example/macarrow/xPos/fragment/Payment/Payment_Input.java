package com.example.macarrow.xPos.fragment.Payment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.Services.Payment_Services;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class Payment_Input extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        final int idx = mArgs.getInt("idx");
        int totalAmount = mArgs.getInt("total_amount");
        final int cooperIdx = mArgs.getInt("cooper_idx");
        final String cooper_title = mArgs.getString("cooper_title");
        final int discountCooper = mArgs.getInt("discount_cooper");
        final int minute_free = mArgs.getInt("minute_free");
        int discountSelf = mArgs.getInt("discount_self");
        final long endDate = mArgs.getLong("end_date");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.payment_input, null);

        final Garage_Service garageService = new Garage_Service(getActivity(), "garage.db", null, 1);
        final Payment_Services payment_services = new Payment_Services(getActivity(), "payment.db", null, 1);

        final TextView Car_num = (TextView)view.findViewById(R.id.car_num);
        final LinearLayout Do_card = (LinearLayout)view.findViewById(R.id.do_card);
        final LinearLayout Do_cash = (LinearLayout)view.findViewById(R.id.do_cash);
        final LinearLayout Force_out = (LinearLayout)view.findViewById(R.id.force_out);
        final EditText Pay_money = (EditText)view.findViewById(R.id.pay_money);
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
        CarNum += "결제 - [ 차량번호 : ";
        CarNum += car_num;
        CarNum += " ]";
        Car_num.setText(CarNum);

        // 총 금액
        Total_amount.setText(totalAmount+"");
        // 현재까지 결제된 금액
        Pay_amount.setText((Integer) map.get("pay_amount") + "");
        // 지정 할인된 금액
        if ((Integer) map.get("discount_cooper") <= 0) {
            Discount_cooper.setText(discountCooper + "");
        } else {
            Discount_cooper.setText((Integer) map.get("discount_cooper") + "");
        }
        // 임의 할인된 금액
        Discount_self.setText(discountSelf+"");
        // 결제 해야될 금액
        final int inPay = totalAmount - (Integer) map.get("pay_amount") - Integer.parseInt(Discount_cooper.getText().toString()) - discountSelf;
        In_pay.setText(inPay+"");
        // 날짜가져오기
        final long startDate = (long) map.get("start_date");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Start_date.setText(simpleDateFormat.format(startDate));
        End_date.setText(simpleDateFormat.format(endDate));

        Pay_money.setText(inPay + "");
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
                    Pay_money.setText(0 + "");
                } else if (s.length() < 1) {
                    Pay_money.setText(0 + "");
                } else if (inPay - (int) Integer.parseInt(s.toString()) < 0) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setTitle("결제 금액이 총 금액 보다 큽니다");
                    adb.setNegativeButton("닫기", null);
                    adb.show();
                    Pay_money.setText(inPay+"");
                }
            }
        });
        Pay_money.addTextChangedListener(textWatcher);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int total_amount = (Integer.parseInt(Total_amount.getText().toString()));
                final int pay_amount = (Integer.parseInt(Pay_amount.getText().toString()));
                final int discount_cooper = (Integer.parseInt(Discount_cooper.getText().toString()));
                final int discount_self = (Integer.parseInt(Discount_self.getText().toString()));
                final int pay_money = (Integer.parseInt(Pay_money.getText().toString()));
                final int payAmount = pay_amount+pay_money;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                final int cooper_start = Integer.parseInt(sdf.format(startDate));
                final int cooper_end = Integer.parseInt(sdf.format(endDate));
                GregorianCalendar calendar = new GregorianCalendar();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH)+1;
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                if (Integer.parseInt(Pay_money.getText().toString()) <= 0) {
                    Pay_money.setText(inPay+"");
                }

                switch (v.getId()) {

                    case R.id.do_card:
                        AlertDialog.Builder do_card = new AlertDialog.Builder(getActivity());
                        do_card.setTitle(pay_money+"원을 카드 결제 하시겠습니까?");
                        do_card.setNegativeButton("닫기", null);
                        do_card.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (discount_cooper > 0) {

                                    garageService.updateCooper(
                                        cooperIdx,
                                        cooper_title,
                                        cooper_start,
                                        cooper_end,
                                        discount_cooper,
                                        minute_free,
                                        idx);

                                }

                                String is_out = "Y";
                                String is_paid = "";
                                if (total_amount-pay_money-pay_amount-discount_cooper-discount_self == 0) {
                                    is_paid = "Y";
                                } else {
                                    is_paid = "N";
                                }

                                garageService.outCar(endDate, total_amount, payAmount, cooperIdx, discount_cooper, discount_self, is_out, is_paid, idx);
                                String lookup_type = "garage";
                                String pay_type = "card";
                                int cooper_amount = discount_cooper+discount_self;
                                payment_services.insert(idx, lookup_type, pay_type, total_amount, cooper_amount, pay_money, year, month, day);
                                dismiss();
                            }
                        });
                        do_card.show();
                        break;

                    case R.id.do_cash:
                        AlertDialog.Builder do_cash = new AlertDialog.Builder(getActivity());
                        do_cash.setTitle(pay_money+"원을 현금 결제 하시겠습니까?");
                        do_cash.setNegativeButton("닫기", null);
                        do_cash.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (discount_cooper > 0) {

                                    garageService.updateCooper(
                                            cooperIdx,
                                            cooper_title,
                                            cooper_start,
                                            cooper_end,
                                            discount_cooper,
                                            minute_free,
                                            idx);
                                }

                                String is_out = "Y";
                                String is_paid = "";
                                if (total_amount-pay_money-payAmount-discount_cooper-discount_self == 0) {
                                    is_paid = "Y";
                                } else {
                                    is_paid = "N";
                                }

                                garageService.outCar(endDate, total_amount, payAmount, cooperIdx, discount_cooper, discount_self, is_out, is_paid, idx);
                                String lookup_type = "garage";
                                String pay_type = "cash";
                                int cooper_amount = discount_cooper+discount_self;
                                payment_services.insert(idx, lookup_type, pay_type, total_amount, cooper_amount, pay_money, year, month, day);
                                dismiss();
                            }
                        });
                        do_cash.show();
                        break;

                    case R.id.force_out:
                        AlertDialog.Builder force_out = new AlertDialog.Builder(getActivity());
                        force_out.setTitle("결제 없이 출차하시겠습니까?");
                        force_out.setNegativeButton("닫기", null);
                        force_out.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (discountCooper > 0) {
                                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                                    adb.setTitle("지정주차 요금이 있어 결제 없이 출차가 불가능합니다");
                                    adb.setNegativeButton("닫기", null);
                                    adb.show();
                                }

                                String is_out = "Y";
                                String is_paid = "";

                                if (total_amount > 0) {
                                    is_paid = "N";
                                } else {
                                    is_paid = "Y";
                                }

                                garageService.updateForceOut(is_out, total_amount, endDate, is_paid, idx);
                                dismiss();
                            }
                        });
                        force_out.show();
                        break;
                }
            }
        };
        Do_card.setOnClickListener(clickListener);
        Do_cash.setOnClickListener(clickListener);
        Force_out.setOnClickListener(clickListener);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(835, WindowManager.LayoutParams.WRAP_CONTENT);
    }

}