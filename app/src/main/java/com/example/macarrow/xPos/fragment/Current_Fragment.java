package com.example.macarrow.xPos.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.Services.Payment_Services;
import com.example.macarrow.xPos.adapter.CurrentViewAdapter;
import com.example.macarrow.xPos.fragment.Payment.Payment_Discount;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class Current_Fragment extends Fragment {

    public Current_Fragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.current, container, false);

        final Garage_Service garageService = new Garage_Service(getActivity(), "garage.db", null, 1);
        final Payment_Services payment_services = new Payment_Services(getActivity(), "payment.db", null, 1);
        final GridView currentView = (GridView) view.findViewById(R.id.currentView);

        final List<Map<String, Object>> list = garageService.getResult("in", "");
        final CurrentViewAdapter adapter = new CurrentViewAdapter(getActivity(), R.layout.current_item, list);
        currentView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // 당겨서 새로고침
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        currentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view1, int position, long id) {

                final String car_num = (String) list.get(position).get("car_num");
                final int idx = (int) list.get(position).get("idx");
                final int month_idx = (int) list.get(position).get("month_idx");
                final View view2 = getActivity().getLayoutInflater().inflate(R.layout.garage_view, null);

                final LinearLayout Out_car = (LinearLayout)view2.findViewById(R.id.out_car);
                final LinearLayout Cancel_car = (LinearLayout)view2.findViewById(R.id.cancel_car);
                final LinearLayout Printin_car = (LinearLayout)view2.findViewById(R.id.printin_car);
                final LinearLayout Pay_status = (LinearLayout)view2.findViewById(R.id.pay_status);
                final TextView Car_num = (TextView)view2.findViewById(R.id.car_num);
                final TextView Start_date = (TextView)view2.findViewById(R.id.start_date);
                final TextView End_date = (TextView)view2.findViewById(R.id.end_date);
                final TextView Month_idx = (TextView)view2.findViewById(R.id.month_idx);
                final TextView Result_charge = (TextView)view2.findViewById(R.id.result_charge);
                final TextView Discount_cooper = (TextView)view2.findViewById(R.id.discount_cooper);
                final TextView Discount_self = (TextView)view2.findViewById(R.id.discount_self);
                final TextView Car_type_title = (TextView)view2.findViewById(R.id.car_type_title);
                final TextView Minute_free = (TextView)view2.findViewById(R.id.minute_free);
                final TextView Basic_amount = (TextView)view2.findViewById(R.id.basic_amount);
                final TextView Basic_minute = (TextView)view2.findViewById(R.id.basic_minute);
                final TextView Amount_unit = (TextView)view2.findViewById(R.id.amount_unit);
                final TextView Minute_unit = (TextView)view2.findViewById(R.id.minute_unit);
                final TextView Is_out = (TextView)view2.findViewById(R.id.is_out);
                final TextView Is_cancel = (TextView)view2.findViewById(R.id.is_cancel);
                TextView resultCharge = null;
                if (month_idx > 0) {
                    Result_charge.setText(0+"원");
                    Pay_status.setVisibility(view2.GONE);
                } else {
                    resultCharge = (TextView) view1.findViewById(R.id.result_charge);
                    Result_charge.setText(resultCharge.getText().toString()+"원");
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

                View.OnClickListener clickListener = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final FragmentManager fm = getFragmentManager();
                        dialog.dismiss();

                        switch (v.getId()) {

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
                                            fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
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
                                fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                                break;

                            case R.id.printin_car:

                                break;

                        }
                    }
                };

                Out_car.setOnClickListener(clickListener);
                Cancel_car.setOnClickListener(clickListener);
                Printin_car.setOnClickListener(clickListener);

            }
        });

        return view;

    }
}