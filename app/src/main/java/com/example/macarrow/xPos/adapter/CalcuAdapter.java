package com.example.macarrow.xPos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.macarrow.xPos.DayInfo;
import com.example.macarrow.xPos.R;
import java.util.ArrayList;
import java.util.Calendar;

public class CalcuAdapter extends BaseAdapter {

    // 안드로이드 gridview 달력 스케줄
    //http://blog.naver.com/PostView.nhn?blogId=onblack_&logNo=220934784083&beginTime=0&jumpingVid=&from=search&redirect=Log&widgetTypeCall=true

    private ArrayList<DayInfo> mDayList;
    private Context mContext;
    private int mResource;
    private LayoutInflater mflater;

    public CalcuAdapter (Context context, int textResource, ArrayList<DayInfo> dayList) {
        this.mContext = context;
        this.mDayList = dayList;
        this.mResource = textResource;
        this.mflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        DayInfo day = mDayList.get(position);
        ViewHolder dayViewHolder;

        if (convertView == null) {
            convertView = mflater.inflate(mResource, null);
            dayViewHolder = new ViewHolder();
            dayViewHolder.day = (TextView) convertView.findViewById(R.id.calendar_day);
            dayViewHolder.in = (TextView) convertView.findViewById(R.id.calendar_month_in);
            dayViewHolder.out = (TextView) convertView.findViewById(R.id.calendar_month_out);
            convertView.setTag(dayViewHolder);
        } else {
            dayViewHolder = (ViewHolder) convertView.getTag();
        } if (day != null) {
            Calendar mCal = Calendar.getInstance();
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            dayViewHolder.day.setText(day.getDay());
            if (day.isInMonth()) {
                if (position % 7 == 0) {
                    dayViewHolder.day.setTextColor(Color.RED);
                } else if (position % 7 == 6) {
                    dayViewHolder.day.setTextColor(Color.BLUE);
                } else {
                    dayViewHolder.day.setTextColor(Color.BLACK);
                }
            } else {
                dayViewHolder.day.setTextColor(Color.GRAY);
            }
        }
        return convertView;
    }
}

class ViewHolder {
    public TextView day;
    public TextView in;
    public TextView out;
}