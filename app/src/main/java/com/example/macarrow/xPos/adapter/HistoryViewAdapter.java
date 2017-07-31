package com.example.macarrow.xPos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class HistoryViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public HistoryViewAdapter(Context context, int layout, List<Map<String, Object>> list) {

        this.context = context;
        this.layout = layout;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryView_Item item = null;

        if(convertView == null) {

            convertView = inflater.inflate(layout, null);
            TextView Car_num = (TextView) convertView.findViewById(R.id.car_num);
            TextView Start_date = (TextView) convertView.findViewById(R.id.start_date);
            TextView End_date = (TextView) convertView.findViewById(R.id.end_date);
            TextView Total_amount = (TextView) convertView.findViewById(R.id.total_amount);
            TextView Discount = (TextView) convertView.findViewById(R.id.discount);
            TextView Pay_amount = (TextView) convertView.findViewById(R.id.pay_amount);
            TextView In_status = (TextView) convertView.findViewById(R.id.in_status);
            TextView Pay_status = (TextView) convertView.findViewById(R.id.pay_status);

            item = new HistoryView_Item();
            item.carNum = Car_num;
            item.startDate = Start_date;
            item.endDate = End_date;
            item.totalAmount = Total_amount;
            item.discounT = Discount;
            item.payAmount = Pay_amount;
            item.inStatus = In_status;
            item.payStatus = Pay_status;
            convertView.setTag(item);

        } else {

            item = (HistoryView_Item) convertView.getTag();

        }

        int total_amount = (int) list.get(position).get("total_amount");
        int payAmount = (int) list.get(position).get("pay_amount");

        // 차량 번호
        item.carNum.setText((String) list.get(position).get("car_num"));

        // 입차 시간
        long startDate = (long) list.get(position).get("start_date");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        item.startDate.setText(simpleDateFormat.format(startDate) + "");

        // 출차 시간
        long endDate = (long) list.get(position).get("end_date");
        if (endDate <= 0) {
            item.endDate.setText("");
        } else {
            item.endDate.setText(simpleDateFormat.format(endDate) + "");
        }

        // 이용 요금
        int monthIdx = (int) list.get(position).get("month_idx");
        String is_daycar = (String) list.get(position).get("is_daycar");

        double oneSecond = 1000;
        double oneMinute = oneSecond * 60;
        int result_charge = 0;
        double park_min = Math.floor(((double) System.currentTimeMillis() - startDate) / oneMinute);
        int basic_amount = (int) list.get(position).get("basic_amount");
        int amount_unit = (int) list.get(position).get("amount_unit");
        double free_min = (int) list.get(position).get("minute_free");
        double basic_min= (int) list.get(position).get("basic_minute");
        double minute_unit = (int) list.get(position).get("minute_unit");

        if (park_min - free_min > 0) {
            if(park_min - free_min - basic_min > 0){
                double added_min = Math.ceil((park_min - free_min - basic_min) / minute_unit);
                result_charge = (int) (basic_amount + (added_min * amount_unit));
            } else {
                result_charge = basic_amount;
            }
        }

        String is_out = (String) list.get(position).get("is_out");
        if (monthIdx > 0) {
            item.totalAmount.setText(0 + "");
        } if (is_daycar.equals("Y")) {
            item.totalAmount.setText(total_amount + "");
        } else if(monthIdx == 0 && is_out.equals("Y")) {
            item.totalAmount.setText(total_amount + "");
        } else if (monthIdx == 0 && is_out.equals("N")){
            item.totalAmount.setText(result_charge + "");
        }

        // 할인 금액
        int discountCooper = (int) list.get(position).get("discount_cooper");
        int discountSelf = (int) list.get(position).get("discount_self");
        if(discountCooper+discountSelf <= 0) {
            item.discounT.setText("0원");
        } else {
            item.discounT.setText(String.format("%,d원",discountCooper+discountSelf));
        }

        // 결제 금액
        int pay_amount = (Integer) list.get(position).get("pay_amount");
        item.payAmount.setText(String.format("%,d원",pay_amount));

        // 입차 구분
        String isCancel = (String) list.get(position).get("is_cancel");
        String isOut = (String) list.get(position).get("is_out");
        if(isCancel.equals("N") && isOut.equals("N")) {
            item.inStatus.setText("입차중");
        } if (isOut.equals("Y") && isCancel.equals("N")) {
            item.inStatus.setText("출차완료");
        } if (isOut.equals("Y") && isCancel.equals("Y")) {
            item.inStatus.setText("입차취소");
        }

        // 결제 구분
        if(monthIdx == 0 && isCancel.equals("N") && isOut.equals("Y")) {
            if (total_amount-payAmount-discountCooper-discountSelf > 0) {
                item.payStatus.setText("미결제");
            } else {
                item.payStatus.setText("결제완료");
            }
        } if (isCancel.equals("Y")) {
            item.payStatus.setText("입차취소");
        } if (isOut.equals("N")) {
            item.payStatus.setText("");
        } if (monthIdx > 0) {
            item.payStatus.setText("월차");
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

class HistoryView_Item {

    TextView carNum;
    TextView startDate;
    TextView endDate;
    TextView totalAmount;
    TextView discounT;
    TextView payAmount;
    TextView inStatus;
    TextView payStatus;

}