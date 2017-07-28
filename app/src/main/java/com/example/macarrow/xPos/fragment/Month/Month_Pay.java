package com.example.macarrow.xPos.fragment.Month;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
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
import com.example.macarrow.xPos.Services.Month_Service;
import com.example.macarrow.xPos.Services.Payment_Services;
import com.example.macarrow.xPos.fragment.Current_Fragment;
import com.example.macarrow.xPos.fragment.Month_Fragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class Month_Pay extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        final String status = mArgs.getString("status");
        final String car_num = mArgs.getString("car_num");
        final String from = mArgs.getString("from");

        final Month_Service month_service = new Month_Service(getActivity(), "month.db", null, 1);
        final Payment_Services payment_services = new Payment_Services(getActivity(), "payment.db", null, 1);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        if (status.equals("new")) {

            View view = inflater.inflate(R.layout.month_pay, null);
            final TextView Title = (TextView)view.findViewById(R.id.title);
            final LinearLayout Do_card = (LinearLayout)view.findViewById(R.id.do_card);
            final LinearLayout Do_cash = (LinearLayout)view.findViewById(R.id.do_cash);
            final LinearLayout Close_pay_month = (LinearLayout)view.findViewById(R.id.close_pay_month);
            final EditText Pay_money = (EditText)view.findViewById(R.id.pay_money);
            final TextView Amount = (TextView)view.findViewById(R.id.amount);
            final TextView Outstanding_amount = (TextView)view.findViewById(R.id.outstanding_amount);
            final TextView Pay_amount = (TextView)view.findViewById(R.id.pay_amount);
            final TextView Start_date = (TextView)view.findViewById(R.id.start_date);
            final TextView End_date = (TextView)view.findViewById(R.id.end_date);

            builder.setNegativeButton("닫기", null);
            builder.setView(view);

            final Map<String, Object> map = month_service.getResultForUpdate(car_num);
            // 다이얼로그 타이틀
            String CarNum = "";
            CarNum += "월차 결제 - [ 차량번호 : ";
            CarNum += (String) map.get("car_num");
            CarNum += " ]";
            Title.setText(CarNum);
            Pay_money.setText((Integer) map.get("amount") +"");
            Amount.setText((Integer) map.get("amount")+"");
            Outstanding_amount.setText((Integer) map.get("amount")+"");

            // 시작날짜
            String sd = "";
            sd += (Integer) map.get("start_date_y");
            sd += ".";
            sd += (Integer) map.get("start_date_m");
            sd += ".";
            sd += (Integer) map.get("start_date_d");
            Start_date.setText(sd);

            // 종료날짜
            String ed = "";
            ed += (Integer) map.get("end_date_y");
            ed += ".";
            ed += (Integer) map.get("end_date_m");
            ed += ".";
            ed += (Integer) map.get("end_date_d");
            End_date.setText(ed);

            final int total_result = (Integer) map.get("amount");
            final int idx = (Integer)map.get("idx");

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
                        Pay_money.setText(0+"");
                    } else if (s.length() < 1) {
                        Pay_money.setText(0+"");
                    } else if (total_result - (int) Integer.parseInt(s.toString()) < 0) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                        adb.setTitle("결제 금액이 총 금액보다 큽니다");
                        adb.setNegativeButton("닫기", null);
                        adb.show();
                        Pay_money.setText(total_result+"");
                    }
                }
            });
            Pay_money.addTextChangedListener(textWatcher);

            View.OnClickListener clickListener = new View.OnClickListener() {
                private FragmentManager fm = getFragmentManager();
                @Override
                public void onClick(View v) {

                    if (Integer.parseInt(Pay_money.getText().toString()) <= 0) {
                        Pay_money.setText(total_result+"");
                    }
                    final int payMoney = (Integer.parseInt(Pay_money.getText().toString()));
                    final int end_date_y = (Integer) map.get("end_date_y");
                    final int end_date_m = (Integer) map.get("end_date_m");
                    final int end_date_d = (Integer) map.get("end_date_d");
                    final int end_date = (Integer) map.get("end_date");
                    GregorianCalendar calendar = new GregorianCalendar();
                    final int year = calendar.get(Calendar.YEAR);
                    final int month = calendar.get(Calendar.MONTH)+1;
                    final int day = calendar.get(Calendar.DAY_OF_MONTH);
                    final long regdate = System.currentTimeMillis();
                    final int lookup_idx = idx;

                    switch (v.getId()) {

                        case R.id.do_card:

                            AlertDialog.Builder do_card = new AlertDialog.Builder(getActivity());
                            do_card.setTitle(payMoney+"원을 카드 결제하시겠습니까?");
                            do_card.setNegativeButton("닫기", null);
                            do_card.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String is_paid;
                                    if (total_result-payMoney == 0) {
                                        is_paid = "Y";
                                    } else {
                                        is_paid = "N";
                                    }
                                    month_service.updatePay(payMoney, end_date_y, end_date_m, end_date_d, end_date, is_paid, regdate, idx);
                                    payment_services.insert(lookup_idx, "month", "card", 0, 0, payMoney, year, month, day, regdate);
                                    dismiss();

                                    if (from.equals("current")) {
                                        fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                                    } else if (from.equals("cal")) {
                                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Calendar()).commit();
                                    } else {
                                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment(from)).commit();
                                    }
                                }
                            });
                            do_card.show();
                            break;

                        case R.id.do_cash:

                            AlertDialog.Builder do_cash = new AlertDialog.Builder(getActivity());
                            do_cash.setTitle(payMoney+"원을 현금 결제하시겠습니까?");
                            do_cash.setNegativeButton("닫기", null);
                            do_cash.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String is_paid;
                                    if (total_result-payMoney == 0) {
                                        is_paid = "Y";
                                    } else {
                                        is_paid = "N";
                                    }
                                    month_service.updatePay(payMoney, end_date_y, end_date_m, end_date_d, end_date, is_paid, regdate, idx);
                                    payment_services.insert(lookup_idx, "month", "cash", 0, 0, payMoney, year, month, day, regdate);
                                    dismiss();

                                    if (from.equals("current")) {
                                        fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                                    } else if (from.equals("cal")) {
                                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Calendar()).commit();
                                    } else {
                                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment(from)).commit();
                                    }
                                }
                            });
                            do_cash.show();
                            break;

                        case R.id.close_pay_month:

                            dismiss();

                            if (from.equals("current")) {
                                fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                            } else if (from.equals("cal")) {
                                fm.beginTransaction().replace(R.id.content_fragment, new Month_Calendar()).commit();
                            } else {
                                fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment(from)).commit();
                            }
                            break;
                    }
                }
            };
            Do_card.setOnClickListener(clickListener);
            Do_cash.setOnClickListener(clickListener);
            Close_pay_month.setOnClickListener(clickListener);
        }

        if (status.equals("add")) {

            View view = inflater.inflate(R.layout.month_pay, null);
            final TextView Title = (TextView)view.findViewById(R.id.title);
            final LinearLayout Do_card = (LinearLayout)view.findViewById(R.id.do_card);
            final LinearLayout Do_cash = (LinearLayout)view.findViewById(R.id.do_cash);
            final LinearLayout Close_pay_month = (LinearLayout)view.findViewById(R.id.close_pay_month);
            final EditText Pay_money = (EditText)view.findViewById(R.id.pay_money);
            final TextView Amount = (TextView)view.findViewById(R.id.amount);
            final TextView Outstanding_amount = (TextView)view.findViewById(R.id.outstanding_amount);
            final TextView Pay_amount = (TextView)view.findViewById(R.id.pay_amount);
            final TextView Start_date = (TextView)view.findViewById(R.id.start_date);
            final TextView End_date = (TextView)view.findViewById(R.id.end_date);

            builder.setNegativeButton("닫기", null);
            builder.setView(view);

            final Map<String, Object> map = month_service.getResultForUpdate(car_num);
            // 다이얼로그 타이틀
            String CarNum = "";
            CarNum += "월차 결제 - [ 차량번호 : ";
            CarNum += (String) map.get("car_num");
            CarNum += " ]";
            Title.setText(CarNum);
            Amount.setText((Integer) map.get("amount")+"");
            Pay_amount.setText((Integer) map.get("pay_amount")+"");

            // 시작날짜
            String sd = "";
            sd += (Integer) map.get("start_date_y");
            sd += ".";
            sd += (Integer) map.get("start_date_m");
            sd += ".";
            sd += (Integer) map.get("start_date_d");
            Start_date.setText(sd);

            // 종료날짜
            String ed = "";
            ed += (Integer) map.get("end_date_y");
            ed += ".";
            ed += (Integer) map.get("end_date_m");
            ed += ".";
            ed += (Integer) map.get("end_date_d");
            End_date.setText(ed);

            final int amount = (Integer) map.get("amount");
            final int pay_amount = (Integer) map.get("pay_amount");
            final int idx = (Integer)map.get("idx");

            final int edy = (Integer) map.get("end_date_y");
            final int edm = (Integer) map.get("end_date_m");
            final int edd = (Integer) map.get("end_date_d");

            Outstanding_amount.setText((amount-pay_amount)+"");
            Pay_money.setText((amount-pay_amount) +"");

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
                        Pay_money.setText(0+"");
                    } else if (s.length() < 1) {
                        Pay_money.setText(0+"");
                    } else if (amount - (int) Integer.parseInt(s.toString()) < 0) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                        adb.setTitle("결제 금액이 총 금액보다 큽니다");
                        adb.setNegativeButton("닫기", null);
                        adb.show();
                        Pay_money.setText((amount-pay_amount)+"");
                    }
                }
            });
            Pay_money.addTextChangedListener(textWatcher);

            View.OnClickListener clickListener = new View.OnClickListener() {
                private FragmentManager fm = getFragmentManager();
                @Override
                public void onClick(View v) {

                    if (Integer.parseInt(Pay_money.getText().toString()) <= 0) {
                        Pay_money.setText((amount-pay_amount)+"");
                    }
                    final int payMoney = (Integer.parseInt(Pay_money.getText().toString()));
                    final int end_date = (Integer)map.get("end_date");
                    final int Amount = (amount-pay_amount);
                    final int lookup_idx = idx;
                    GregorianCalendar calendar = new GregorianCalendar();
                    final int year = calendar.get(Calendar.YEAR);
                    final int month = calendar.get(Calendar.MONTH)+1;
                    final int day = calendar.get(Calendar.DAY_OF_MONTH);
                    final long regdate = System.currentTimeMillis();

                    switch (v.getId()) {

                        case R.id.do_card:

                            AlertDialog.Builder do_card = new AlertDialog.Builder(getActivity());
                            do_card.setTitle(payMoney+"원을 카드 결제하시겠습니까?");
                            do_card.setNegativeButton(year+""+month+""+day+"닫기", null);
                            do_card.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    int pay = pay_amount+payMoney;
                                    String is_paid;
                                    if (amount-pay == 0) {
                                        is_paid = "Y";
                                    } else {
                                        is_paid = "N";
                                    }
                                    month_service.updatePay(pay, edy, edm, edd, end_date, is_paid, regdate, idx);
                                    payment_services.insert(lookup_idx, "month", "card", 0, 0, payMoney, year, month, day, regdate);
                                    dismiss();
                                    if (from.equals("cal")) {
                                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Calendar()).commit();
                                    } else {
                                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment(from)).commit();
                                    }
                                }
                            });
                            do_card.show();
                            break;

                        case R.id.do_cash:

                            AlertDialog.Builder do_cash = new AlertDialog.Builder(getActivity());
                            do_cash.setTitle(payMoney+"원을 현금 결제하시겠습니까?");
                            do_cash.setNegativeButton("닫기", null);
                            do_cash.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    int pay = pay_amount+payMoney;
                                    String is_paid;
                                    if (amount-pay == 0) {
                                        is_paid = "Y";
                                    } else {
                                        is_paid = "N";
                                    }
                                    month_service.updatePay(pay, edy, edm, edd, end_date, is_paid, regdate, idx);
                                    payment_services.insert(lookup_idx, "month", "cash", 0, 0, payMoney, year, month, day, regdate);
                                    dismiss();
                                    if (from.equals("cal")) {
                                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Calendar()).commit();
                                    } else {
                                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment(from)).commit();
                                    }
                                }
                            });
                            do_cash.show();
                            break;

                        case R.id.close_pay_month:

                            dismiss();
                            if (from.equals("cal")) {
                                fm.beginTransaction().replace(R.id.content_fragment, new Month_Calendar()).commit();
                            } else {
                                fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment(from)).commit();
                            }
                            break;
                    }
                }
            };
            Do_card.setOnClickListener(clickListener);
            Do_cash.setOnClickListener(clickListener);
            Close_pay_month.setOnClickListener(clickListener);
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