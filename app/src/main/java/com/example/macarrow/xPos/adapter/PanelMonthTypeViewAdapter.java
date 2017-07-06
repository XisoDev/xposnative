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

public class PanelMonthTypeViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public PanelMonthTypeViewAdapter(Context context, int layout, List<Map<String, Object>> list) {

        this.context = context;
        this.layout = layout;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PanelMonthTypeView_Item item = null;

        if(convertView == null) {

            convertView = inflater.inflate(layout, null);
            TextView Car_num = (TextView) convertView.findViewById(R.id.car_num);
            TextView Car_name = (TextView) convertView.findViewById(R.id.car_name);
            TextView Car_type_title = (TextView) convertView.findViewById(R.id.car_type_title);
            TextView Start_date = (TextView) convertView.findViewById(R.id.start_date);
            TextView End_date = (TextView) convertView.findViewById(R.id.end_date);
            TextView Amount = (TextView) convertView.findViewById(R.id.amount);
            TextView User_name = (TextView) convertView.findViewById(R.id.user_name);
            TextView Mobile = (TextView) convertView.findViewById(R.id.mobile);

            item = new PanelMonthTypeView_Item();
            item.carNum = Car_num;
            item.carName = Car_name;
            item.carTypeTitle = Car_type_title;
            item.startDate = Start_date;
            item.endDate = End_date;
            item.amounT = Amount;
            item.userName = User_name;
            item.mobilE = Mobile;
            convertView.setTag(item);

        } else {

            item = (PanelMonthTypeView_Item) convertView.getTag();

        }

        // 시작날짜
        String sd = "";
        sd += (Integer) list.get(position).get("start_date_y");
        sd += ".";
        sd += (Integer) list.get(position).get("start_date_m");
        sd += ".";
        sd += (Integer) list.get(position).get("start_date_d");
        item.startDate.setText(sd+"부터 ~");
        // 종료날짜
        String ed = "";
        ed += (Integer) list.get(position).get("end_date_y");
        ed += ".";
        ed += (Integer) list.get(position).get("end_date_m");
        ed += ".";
        ed += (Integer) list.get(position).get("end_date_d");
        item.endDate.setText(ed+"까지");

        item.carNum.setText((String) list.get(position).get("car_num"));
        item.carName.setText("(" + (String) list.get(position).get("car_name") +"/");
        item.carTypeTitle.setText((String) list.get(position).get("car_type_title") + ")");
        item.amounT.setText((Integer) list.get(position).get("amount") + "원");
        item.userName.setText((String) list.get(position).get("user_name"));
        item.mobilE.setText((String) list.get(position).get("mobile"));

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

class PanelMonthTypeView_Item {

    TextView carNum;
    TextView carName;
    TextView carTypeTitle;
    TextView startDate;
    TextView endDate;
    TextView amounT;
    TextView userName;
    TextView mobilE;

}