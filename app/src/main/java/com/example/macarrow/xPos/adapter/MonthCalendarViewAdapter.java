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
import com.example.macarrow.xPos.Services.Month_Service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthCalendarViewAdapter extends BaseAdapter {

    private ArrayList<DayInfo> mDayList;
    private Context context;
    private int mResource;
    private LayoutInflater mflater;

    public MonthCalendarViewAdapter (Context context, int textResource, ArrayList<DayInfo> dayList) {
        this.context = context;
        this.mDayList = dayList;
        this.mResource = textResource;
        this.mflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        MonthCalendarView_Item dayViewHolder;

        if (convertView == null) {

            convertView = mflater.inflate(mResource, null);
            dayViewHolder = new MonthCalendarView_Item();
            dayViewHolder.Calendar_day = (TextView) convertView.findViewById(R.id.calendar_day);
            dayViewHolder.Month_in_cnt = (TextView)convertView.findViewById(R.id.month_in_cnt);
            dayViewHolder.Month_out_cnt = (TextView)convertView.findViewById(R.id.month_out_cnt);
            convertView.setTag(dayViewHolder);

        } else {

            dayViewHolder = (MonthCalendarView_Item) convertView.getTag();

        } if (day != null) {

            Calendar mCal = Calendar.getInstance();
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            dayViewHolder.Calendar_day.setText(day.getDay());

            long now = System.currentTimeMillis();
            final Date date = new Date(now);
            final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
            final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
            final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

            Month_Service month_service = new Month_Service(context, "month.db", null, 1);
            int year = Integer.parseInt(curYearFormat.format(date));
            int month = Integer.parseInt(curMonthFormat.format(date));
            int tDay = Integer.parseInt(curDayFormat.format(date));

            int in = month_service.calMonthInCnt(year, month, tDay);
            int out = month_service.calMonthOutCnt(year, month, tDay);

            if (day.getDay() == sToday) {
                dayViewHolder.Month_in_cnt.setText("시작"+in+"대");
                dayViewHolder.Month_out_cnt.setText("종료"+out+"대");
            }

            if (day.isInMonth()) {

                if (day.getDay() == sToday) {
                    dayViewHolder.Calendar_day.setTextColor(Color.GREEN);
                } else if (position % 7 == 0) {
                    dayViewHolder.Calendar_day.setTextColor(Color.RED);
                } else if (position % 7 == 6) {
                    dayViewHolder.Calendar_day.setTextColor(Color.BLUE);
                }

            } else {
                dayViewHolder.Calendar_day.setText("");
            }

        }
        return convertView;
    }
}

class MonthCalendarView_Item {

    TextView Calendar_day;
    TextView Month_in_cnt;
    TextView Month_out_cnt;

}