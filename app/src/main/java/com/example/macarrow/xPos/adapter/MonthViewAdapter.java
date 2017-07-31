package com.example.macarrow.xPos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import java.util.List;
import java.util.Map;

public class MonthViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public MonthViewAdapter(Context context, int layout, List<Map<String, Object>> list) {

        this.context = context;
        this.layout = layout;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MonthView_Item item = null;

        if(convertView == null) {

            convertView = inflater.inflate(layout, null);
            TextView Car_num = (TextView) convertView.findViewById(R.id.car_num);
            TextView Start_date = (TextView) convertView.findViewById(R.id.start_date);
            TextView End_date = (TextView) convertView.findViewById(R.id.end_date);
            TextView Amount = (TextView) convertView.findViewById(R.id.amount);
            TextView Pay_amount = (TextView) convertView.findViewById(R.id.pay_amount);
            TextView Outstanding_amount = (TextView) convertView.findViewById(R.id.outstanding_amount);
            TextView Car_name = (TextView) convertView.findViewById(R.id.car_name);
            TextView Car_type_title = (TextView) convertView.findViewById(R.id.car_type_title);
            TextView User_name = (TextView) convertView.findViewById(R.id.user_name);
            TextView Mobile = (TextView) convertView.findViewById(R.id.mobile);
            TextView Is_paid = (TextView) convertView.findViewById(R.id.is_paid);

            item = new MonthView_Item();
            item.carNum = Car_num;
            item.startDate = Start_date;
            item.endDate = End_date;
            item.amounT = Amount;
            item.payAmount = Pay_amount;
            item.outstandingAmount = Outstanding_amount;
            item.carName = Car_name;
            item.carTypeTitle = Car_type_title;
            item.userName = User_name;
            item.mobilE = Mobile;
            item.isPaid = Is_paid;
            convertView.setTag(item);

        } else {

            item = (MonthView_Item) convertView.getTag();

        }

        // 차량번호
        item.carNum.setText((String) list.get(position).get("car_num"));
        // 시작날짜
        String sd = "";
        sd += (Integer) list.get(position).get("start_date_y");
        sd += ".";
        sd += (Integer) list.get(position).get("start_date_m");
        sd += ".";
        sd += (Integer) list.get(position).get("start_date_d");
        item.startDate.setText(sd+"");
        // 종료날짜
        String ed = "";
        ed += (Integer) list.get(position).get("end_date_y");
        ed += ".";
        ed += (Integer) list.get(position).get("end_date_m");
        ed += ".";
        ed += (Integer) list.get(position).get("end_date_d");
        item.endDate.setText(ed+"");
        // 월차금액
        int amount = (Integer) list.get(position).get("amount");
        item.amounT.setText(String.format("%,d원",amount));
        // 결제금액
        int pay_amount = (Integer) list.get(position).get("pay_amount");
        item.payAmount.setText(String.format("%,d원",pay_amount));
        // 미수금액
        int outstandingAmount = (Integer) list.get(position).get("amount")-(Integer) list.get(position).get("pay_amount");
        item.outstandingAmount.setText(String.format("%,d원",outstandingAmount));
        // 차종
        item.carName.setText((String) list.get(position).get("car_name"));
        // 차주명
        item.userName.setText((String) list.get(position).get("user_name"));
        // 결제
        String is_paid = (String) list.get(position).get("is_paid");
        if (is_paid.equals("Y")) {
            item.isPaid.setText("결제완료");
        } if (is_paid.equals("N")) {
            item.isPaid.setText("미결제");
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}

class MonthView_Item {

    TextView carNum;
    TextView startDate;
    TextView endDate;
    TextView amounT;
    TextView payAmount;
    TextView outstandingAmount;
    TextView carName;
    TextView carTypeTitle;
    TextView userName;
    TextView mobilE;
    TextView isPaid;

}