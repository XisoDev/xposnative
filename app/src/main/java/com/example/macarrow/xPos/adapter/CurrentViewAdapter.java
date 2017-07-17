package com.example.macarrow.xPos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import java.util.List;
import java.util.Map;

public class CurrentViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public CurrentViewAdapter(Context context, int layout, List<Map<String, Object>> list) {

        this.context = context;
        this.layout = layout;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CurrentView_Item item = null;

        if(convertView == null) {

            convertView = inflater.inflate(layout, null);
            TextView Car_type_title = (TextView) convertView.findViewById(R.id.car_type_title);
            TextView Result_charge = (TextView) convertView.findViewById(R.id.result_charge);
            TextView Car_num = (TextView) convertView.findViewById(R.id.car_num);
            TextView Pass_time = (TextView) convertView.findViewById(R.id.pass_time);

            item = new CurrentView_Item();
            item.carTypeTitle = Car_type_title;
            item.resultCharge = Result_charge;
            item.carNum = Car_num;
            item.passTime = Pass_time;
            convertView.setTag(item);

        } else {

            item = (CurrentView_Item) convertView.getTag();

        }

        double startDate = (long) list.get(position).get("start_date");
        double gap = ((double) System.currentTimeMillis() - startDate);
        double oneSecond = 1000;
        double oneMinute = oneSecond * 60;
        double oneHour = oneMinute * 60;
        double oneDay = oneHour * 24;
        int minutes = (int) Math.floor((gap % oneHour) / oneMinute);
        int hours = (int) Math.floor((gap % oneDay) / oneHour);
        int days = (int) Math.floor(gap / oneDay);
        String pt = "";
        if (days != 0) {
            pt += days + "일";
        } if (hours != 0) {
            pt += hours + "시간";
        } if (minutes != 0) {
            pt += minutes + "분";
        } if(pt == "") {
            pt = "0분";
        }

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

        item.carTypeTitle.setText((String) list.get(position).get("car_type_title"));
        item.carNum.setText((String) list.get(position).get("car_num"));
        int month_idx = (int) list.get(position).get("month_idx");
        if (month_idx > 0) {
            item.resultCharge.setText("월차");
            item.carTypeTitle.setBackground(convertView.getResources().getDrawable(R.drawable.current_month));
        } else {
            item.resultCharge.setText((result_charge) +"");
        }
        item.passTime.setText(pt);

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

class CurrentView_Item {

    TextView carTypeTitle;
    TextView resultCharge;
    TextView carNum;
    TextView passTime;

}