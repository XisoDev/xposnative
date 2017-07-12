package com.example.macarrow.xPos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.macarrow.xPos.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MonthinNoutViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public MonthinNoutViewAdapter(Context context, int layout, List<Map<String, Object>> list) {

        this.context = context;
        this.layout = layout;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MonthinNoutView_Item item = null;

        if (convertView == null) {
            convertView = inflater.inflate(layout, null);
            TextView Month_num = (TextView) convertView.findViewById(R.id.month_num);
            TextView Month_status = (TextView) convertView.findViewById(R.id.month_status);

            item = new MonthinNoutView_Item();
            item.monthNum = Month_num;
            item.monthStatus = Month_status;

            convertView.setTag(item);

        } else {
            item = (MonthinNoutView_Item) convertView.getTag();
        }

        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        int tDay = Integer.parseInt(curDayFormat.format(date));

        int start_date_d = (int) list.get(position).get("start_date_d");
        int end_date_d = (int) list.get(position).get("end_date_d");

        item.monthNum.setText((String) list.get(position).get("car_num"));

        if (start_date_d <= tDay) {
            item.monthStatus.setText("시작");
        }

        if (end_date_d >= tDay) {
            item.monthStatus.setText("종료");
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

class MonthinNoutView_Item {

    TextView monthNum;
    TextView monthStatus;

}