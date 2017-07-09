package com.example.macarrow.xPos.fragment.Month;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.fragment.Month_Fragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Month_Calendar extends Fragment {

    public Month_Calendar(){}
    private MonthCalendarAdapter adapter;
    private ArrayList<String> dayList;
    private Calendar mCal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.month_calendar, container, false);

        final TextView monthAll = (TextView)view.findViewById(R.id.month_all);
        final TextView monthExpired = (TextView)view.findViewById(R.id.month_expired);
        final TextView monthWait = (TextView)view.findViewById(R.id.month_wait);
        final TextView monthCalendar = (TextView)view.findViewById(R.id.month_calendar);
        final TextView Calendar_date = (TextView)view.findViewById(R.id.calendar_date);
        final GridView Calendar_view = (GridView)view.findViewById(R.id.calendar_view);

        // 오늘 날짜
        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        // 년, 월, 일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        // 현재 년 / 월
        Calendar_date.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));

        // gridview 요일 표시
        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");
        mCal = Calendar.getInstance();

        // 이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        // 1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }

        setCalendarDate(mCal.get(Calendar.MONTH) + 1);
        adapter = new MonthCalendarAdapter(getActivity(), dayList);
        Calendar_view.setAdapter(adapter);

        Calendar_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), curYearFormat.format(date) + "/" + curMonthFormat.format(date) +  "/" +dayList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            private FragmentManager fm = getFragmentManager();
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.month_all :
                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment("possibility")).commit();
                        break;

                    case R.id.month_expired :
                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment("expired")).commit();
                        break;

                    case R.id.month_wait :
                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment("wait")).commit();
                        break;

                    case R.id.month_calendar :
                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Calendar()).commit();
                        break;
                }
            }
        };
        monthAll.setOnClickListener(clickListener);
        monthExpired.setOnClickListener(clickListener);
        monthWait.setOnClickListener(clickListener);
        monthCalendar.setOnClickListener(clickListener);

        return view;
    }

    private void setCalendarDate (int month) {
        mCal.set(Calendar.MONTH, month - 1);
        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }
    }

    public class MonthCalendarAdapter extends BaseAdapter {

        private final ArrayList<String> list;
        private final LayoutInflater inflater;

        public MonthCalendarAdapter(Context context, ArrayList<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public String getItem(int position) {
            return list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.month_calendar_view, parent, false);
                holder = new ViewHolder();
                holder.calendar_day = (TextView)convertView.findViewById(R.id.calendar_day);
                holder.calendar_month_in = (TextView)convertView.findViewById(R.id.calendar_month_in);
                holder.calendar_month_out = (TextView)convertView.findViewById(R.id.calendar_month_out);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder)convertView.getTag();

            }
            holder.calendar_day.setText("" + getItem(position));
            //holder.calendar_month_in.setText("" + getItem(position));
            //holder.calendar_month_out.setText("" + getItem(position));

            // 해당 날짜 텍스트 컬러, 배경 변경
            mCal = Calendar.getInstance();

            // 오늘 day 가져옴
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            if (sToday.equals(getItem(position))) {
                // 오늘 day 텍스트 컬러 변경
                holder.calendar_day.setTextColor(Color.GREEN);
            }
            return convertView;
        }
    }
}

class ViewHolder {

    TextView calendar_day;
    TextView calendar_month_in;
    TextView calendar_month_out;

}