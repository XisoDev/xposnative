package com.example.macarrow.xPos.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.adapter.HistoryViewAdapter;
import com.example.macarrow.xPos.fragment.Payment.Payment_Discount;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class History_Fragment extends Fragment {

    private String history;
    @SuppressLint("ValidFragment")
    public History_Fragment(String history) { this.history = history; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history, container, false);

        final Garage_Service garageService = new Garage_Service(getActivity(), "garage.db", null, 1);

        final TextView historyAll = (TextView)view.findViewById(R.id.history_all);
        final TextView historyIn = (TextView)view.findViewById(R.id.history_in);
        final TextView historyOut = (TextView)view.findViewById(R.id.history_out);
        final TextView historyNoPay = (TextView)view.findViewById(R.id.history_no_pay);
        final TextView historyCancel = (TextView)view.findViewById(R.id.history_cancel);
        final ListView History_list = (ListView)view.findViewById(R.id.history_list);
        final EditText Search_car_num = (EditText)view.findViewById(R.id.search_car_num);
        final Button Input_car_num = (Button)view.findViewById(R.id.input_car_num);

        String status = null;

        switch (history) {

            case "all":
                status = "all";
                break;

            case "in":
                status = "in";
                break;

            case "out":
                status = "out";
                break;

            case "no_pay":
                status = "no_pay";
                break;

            case "cancel":
                status = "cancel";
                break;

        }

        Input_car_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final String car_num = "";

        final List<Map<String, Object>> list = garageService.getResult(status, car_num);
        final HistoryViewAdapter adapter = new HistoryViewAdapter(getActivity(), R.layout.history_item, list);
        History_list.setAdapter(adapter);

        History_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view1, int position, long id) {

                final String car_num = (String) list.get(position).get("car_num");
                final int idx = (int) list.get(position).get("idx");
                final int month_idx = (int) list.get(position).get("month_idx");
                final View view2 = getActivity().getLayoutInflater().inflate(R.layout.history_view, null);

                final LinearLayout Out_car = (LinearLayout)view2.findViewById(R.id.out_car);
                final LinearLayout Pay_car = (LinearLayout)view2.findViewById(R.id.pay_car);
                final LinearLayout Cancel_car = (LinearLayout)view2.findViewById(R.id.cancel_car);
                final LinearLayout Cancel_out = (LinearLayout)view2.findViewById(R.id.cancel_out);
                final LinearLayout Printin_car = (LinearLayout)view2.findViewById(R.id.printin_car);
                final LinearLayout Pay_status = (LinearLayout)view2.findViewById(R.id.pay_status);
                final TextView Car_num = (TextView)view2.findViewById(R.id.car_num);
                final TextView Start_date = (TextView)view2.findViewById(R.id.start_date);
                final TextView End_date = (TextView)view2.findViewById(R.id.end_date);
                final TextView Month_idx = (TextView)view2.findViewById(R.id.month_idx);
                final TextView Result_charge = (TextView)view2.findViewById(R.id.result_charge);
                final TextView Discount_cooper = (TextView)view2.findViewById(R.id.discount_cooper);
                final TextView Discount_self = (TextView)view2.findViewById(R.id.discount_self);
                final TextView Pay_amount = (TextView)view2.findViewById(R.id.pay_amount);
                final TextView Car_type_title = (TextView)view2.findViewById(R.id.car_type_title);
                final TextView Minute_free = (TextView)view2.findViewById(R.id.minute_free);
                final TextView Basic_amount = (TextView)view2.findViewById(R.id.basic_amount);
                final TextView Basic_minute = (TextView)view2.findViewById(R.id.basic_minute);
                final TextView Amount_unit = (TextView)view2.findViewById(R.id.amount_unit);
                final TextView Minute_unit = (TextView)view2.findViewById(R.id.minute_unit);
                final TextView Is_out = (TextView)view2.findViewById(R.id.is_out);
                final TextView Is_cancel = (TextView)view2.findViewById(R.id.is_cancel);

                final TextView totalAmount = (TextView) view1.findViewById(R.id.total_amount);
                final int resultcharge = Integer.parseInt(totalAmount.getText().toString());
                Result_charge.setText(resultcharge+"원");

                if (month_idx > 0) {
                    Pay_status.setVisibility(View.GONE);
                }

                final AlertDialog.Builder garageViewDialog = new AlertDialog.Builder(getActivity());
                garageViewDialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                garageViewDialog.setView(view2);

                // 다이얼 로그 크기 조정
                final Dialog dialog = garageViewDialog.create();
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.copyFrom(dialog.getWindow().getAttributes());
                params.width = 845;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                Window window = dialog.getWindow();
                window.setAttributes(params);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // DB에서 데이터 가져오기
                final Map<String, Object> map = garageService.getResultForUpdate(idx);

                // 타이틀
                String CarNum = "";
                CarNum += "입출차 정보 열람 - [ 차량번호 : ";
                CarNum += (String) map.get("car_num");
                CarNum += " ]";
                Car_num.setText(CarNum);

                long startDate = (long) map.get("start_date");
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Start_date.setText(simpleDateFormat.format(startDate));

                long endDate = (long) map.get("end_date");
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
                }

                Discount_cooper.setText((Integer) map.get("discount_cooper") + "원");
                Discount_self.setText((Integer) map.get("discount_self") + "원");
                Pay_amount.setText((Integer) map.get("pay_amount") + "원");
                Car_type_title.setText((String) map.get("car_type_title"));
                Minute_free.setText((Integer) map.get("minute_free") + "분");
                Basic_amount.setText((Integer) map.get("basic_amount") + "원");
                Basic_minute.setText((Integer) map.get("basic_minute") + "분");
                Amount_unit.setText((Integer) map.get("amount_unit") + "원");
                Minute_unit.setText((Integer) map.get("minute_unit") + "분");

                String isOut = (String) map.get("is_out");
                if (isOut.equals("Y")) {
                    Is_out.setText("출차됨");
                }else {
                    Is_out.setText("입차중");
                }

                String isCancel = (String) map.get("is_cancel");
                if (isCancel.equals("Y")) {
                    Is_cancel.setText("취소됨");
                }else {
                    Is_cancel.setText("아니오");
                }

                String isPaid = (String) map.get("is_paid");
                if (endDate <= 0) {
                    Out_car.setVisibility(view2.VISIBLE);
                    Cancel_car.setVisibility(view2.VISIBLE);
                    Printin_car.setVisibility(view2.VISIBLE);
                }  else if (isPaid.equals("Y")) {
                    Cancel_out.setVisibility(view2.VISIBLE);
                } else if (isCancel.equals("Y")) {

                } else if (isPaid.equals("N") && isCancel.equals("N")) {
                    Pay_car.setVisibility(view2.VISIBLE);
                    Cancel_out.setVisibility(view2.VISIBLE);
                }

                View.OnClickListener clickListener = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        FragmentManager fm = getFragmentManager();
                        dialog.dismiss();

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
                                        }
                                    });
                                    adb.setNegativeButton("닫기", null);
                                    adb.show();
                                } else {
                                    Bundle args = new Bundle();
                                    args.putInt("idx", idx);
                                    Payment_Discount payment_discount = new Payment_Discount();
                                    payment_discount.setArguments(args);
                                    payment_discount.setCancelable(false);
                                    payment_discount.show(getFragmentManager(), "payment_discount");
                                }
                                break;

                            case R.id.cancel_car:

                                long endDate = System.currentTimeMillis();
                                String is_cancel = "Y";
                                String is_out = "Y";
                                int total_amount = 0;
                                int pay_amount = 0;
                                int cooper_idx = 0;
                                int discount_cooper = 0;
                                int discount_self = 0;
                                garageService.cancelCar(is_cancel, is_out, endDate, cooper_idx, discount_cooper, discount_self, total_amount, pay_amount, idx);
                                fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(history)).commit();
                                break;

                            case R.id.cancel_out:

                                garageService.cancelOutCar(idx);
                                fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(history)).commit();
                                break;

                            case R.id.printin_car:

                                break;

                        }
                    }
                };

                Out_car.setOnClickListener(clickListener);
                Pay_car.setOnClickListener(clickListener);
                Cancel_car.setOnClickListener(clickListener);
                Cancel_out.setOnClickListener(clickListener);
                Printin_car.setOnClickListener(clickListener);

            }
        });

        // Button case 처리
        View.OnClickListener clickListener = new View.OnClickListener() {

            private FragmentManager fm = getFragmentManager();

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.history_all:

                        history = "all";
                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(history)).commit();
                        break;

                    case R.id.history_in:

                        history = "in";
                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(history)).commit();
                        break;

                    case R.id.history_out:

                        history = "out";
                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(history)).commit();
                        break;

                    case R.id.history_no_pay:

                        history = "no_pay";
                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(history)).commit();
                        break;

                    case R.id.history_cancel:

                        history = "cancel";
                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(history)).commit();
                        break;

                }
            }
        };

        historyAll.setOnClickListener(clickListener);
        historyIn.setOnClickListener(clickListener);
        historyOut.setOnClickListener(clickListener);
        historyNoPay.setOnClickListener(clickListener);
        historyCancel.setOnClickListener(clickListener);

        return view;

    }
}