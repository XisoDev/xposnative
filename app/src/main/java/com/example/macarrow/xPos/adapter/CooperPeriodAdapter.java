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

public class CooperPeriodAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public CooperPeriodAdapter(Context context, int layout, List<Map<String, Object>> list) {

        this.context = context;
        this.layout = layout;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CooperPeriod_Item item = null;

        if(convertView == null) {

            convertView = inflater.inflate(layout, null);
            TextView Cooper_title = (TextView) convertView.findViewById(R.id.cooper_title);
            TextView Cooper_start = (TextView) convertView.findViewById(R.id.cooper_start);
            TextView Cooper_end = (TextView) convertView.findViewById(R.id.cooper_end);
            TextView Cooper_pt = (TextView) convertView.findViewById(R.id.cooper_pt);
            TextView Discount_cooper = (TextView) convertView.findViewById(R.id.discount_cooper);

            item = new CooperPeriod_Item();
            item.cooperTitle = Cooper_title;
            item.cooperStart = Cooper_start;
            item.cooperEnd = Cooper_end;
            item.cooperPt = Cooper_pt;
            item.discountCooper = Discount_cooper;
            convertView.setTag(item);

        } else {

            item = (CooperPeriod_Item) convertView.getTag();

        }

        double startDate = (long) list.get(position).get("start_date");
        double endDate = (long) list.get(position).get("end_date");
        double gap = (endDate - startDate);
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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");

        item.cooperTitle.setText((String) list.get(position).get("cooper_title"));
        item.cooperStart.setText(simpleDateFormat.format((long) list.get(position).get("start_date")) + "");
        item.cooperEnd.setText(simpleDateFormat.format((long) list.get(position).get("end_date")) + "");
        item.cooperPt.setText(pt + "");
        item.discountCooper.setText((Integer) list.get(position).get("discount_cooper") + "");

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

class CooperPeriod_Item {

    TextView cooperTitle;
    TextView cooperStart;
    TextView cooperEnd;
    TextView cooperPt;
    TextView discountCooper;

}