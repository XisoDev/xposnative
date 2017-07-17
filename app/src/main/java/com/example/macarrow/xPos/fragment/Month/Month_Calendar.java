package com.example.macarrow.xPos.fragment.Month;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.macarrow.xPos.DayInfo;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Month_Service;
import com.example.macarrow.xPos.fragment.Month_Fragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Month_Calendar extends Fragment implements View.OnClickListener {

    public Month_Calendar() {}
    public static int SUNDAY        = 1;
    public static int MONDAY        = 2;
    public static int TUESDAY       = 3;
    public static int WEDNSESDAY    = 4;
    public static int THURSDAY      = 5;
    public static int FRIDAY        = 6;
    public static int SATURDAY      = 7;

    private ArrayList<DayInfo> mDayList;
    private MonthCalendarViewAdapter mAdapter;
    private TextView DateTextView;
    private GridView CalGrid;

    int year, month, mDay;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.month_calendar, container, false);

        final TextView monthAll = (TextView)view.findViewById(R.id.month_all);
        final TextView monthExpired = (TextView)view.findViewById(R.id.month_expired);
        final TextView monthWait = (TextView)view.findViewById(R.id.month_wait);
        final TextView monthCalendar = (TextView)view.findViewById(R.id.month_calendar);
        DateTextView = (TextView)view.findViewById(R.id.dateTextView);
        CalGrid = (GridView)view.findViewById(R.id.calGrid);
        Button PrevButton = (Button)view.findViewById(R.id.prevButton);
        Button NextButton = (Button)view.findViewById(R.id.nextButton);

        monthCalendar.setBackground(getResources().getDrawable(R.drawable.nav_bg_on));
        monthCalendar.setTextColor(getResources().getColor(R.color.nav_txt_on));
        monthAll.setBackground(getResources().getDrawable(R.drawable.nav_bg_off));
        monthAll.setTextColor(getResources().getColor(R.color.nav_txt_off));
        monthExpired.setBackground(getResources().getDrawable(R.drawable.nav_bg_off));
        monthExpired.setTextColor(getResources().getColor(R.color.nav_txt_off));
        monthWait.setBackground(getResources().getDrawable(R.drawable.nav_bg_off));
        monthWait.setTextColor(getResources().getColor(R.color.nav_txt_off));

        mDayList = new ArrayList<DayInfo>();

        PrevButton.setOnClickListener(this);
        NextButton.setOnClickListener(this);

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

        CalGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DayInfo day = mDayList.get(position);
                year = mThisMonthCalendar.get(Calendar.YEAR);
                month = mThisMonthCalendar.get(Calendar.MONTH)+1;
                mDay = Integer.parseInt(day.getDay());

                Month_inNout month_inNout = new Month_inNout();
                month_inNout.setCancelable(false);
                month_inNout.show(getFragmentManager(), "month_inNout");

            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);
    }

    private void getCalendar(Calendar calendar) {
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();

        // 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);

        if(dayOfMonth == SUNDAY) {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth-1)-1;

        // 캘린더 타이틀(년월 표시)을 세팅한다.
        DateTextView.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 " + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        DayInfo day;

        for(int i=0; i<dayOfMonth-1; i++) {
            int date = lastMonthStartDay+i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);

            mDayList.add(day);
        }
        for(int i=1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);

            mDayList.add(day);
        }
        for(int i=1; i<42-(thisMonthLastDay+dayOfMonth-1)+1; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);

            mDayList.add(day);
        }
        initCalendarAdapter();
    }

    private Calendar getLastMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        DateTextView.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 " + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    private Calendar getNextMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        DateTextView.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 " + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.prevButton:
                mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
            case R.id.nextButton:
                mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
        }
    }

    private void initCalendarAdapter() {
        mAdapter = new MonthCalendarViewAdapter(getActivity(), R.layout.month_calendar_view, mDayList);
        CalGrid.setAdapter(mAdapter);
    }

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
                Integer thisMonth = mCal.get(Calendar.MONTH)+1;
                String sToday = String.valueOf(today);
                String sMonth = String.valueOf(thisMonth);
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

                if (day.getDay() == sToday) {
                    dayViewHolder.Calendar_day.setTextColor(Color.GREEN);
                } else if (day.isInMonth()) {

                     if (position % 7 == 0) {
                        dayViewHolder.Calendar_day.setTextColor(Color.RED);
                    } else if (position % 7 == 6) {
                        dayViewHolder.Calendar_day.setTextColor(Color.BLUE);
                    }

                } else {
                    dayViewHolder.Calendar_day.setText(month+"/"+thisMonth);
                }

            }
            return convertView;
        }
    }

    public class Month_inNout extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final Month_Service month_service = new Month_Service(getActivity(), "month.db", null, 1);
            LayoutInflater inflater = getActivity().getLayoutInflater();

            final View view = inflater.inflate(R.layout.month_innout, null);
            final ListView Month_inNouts = (ListView) view.findViewById(R.id.month_inNout);

            final List<Map<String, Object>> list = month_service.calMonth(year, month, mDay);
            MonthinNoutViewAdapter adapter = new MonthinNoutViewAdapter(getActivity(), R.layout.month_innout_item, list);
            Month_inNouts.setAdapter(adapter);

            Month_inNouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final String car_num = (String) list.get(position).get("car_num");
                    Bundle args = new Bundle();
                    args.putString("status", "modify");
                    args.putString("car_num", car_num);
                    Month_Add month_add = new Month_Add();
                    month_add.setArguments(args);
                    month_add.setCancelable(false);
                    month_add.show(getFragmentManager(), "month_modify");
                    dismiss();

                }
            });

            builder.setNegativeButton("닫기", null);
            builder.setView(view);

            return builder.create();
        }
        @Override
        public void onResume() {
            super.onResume();
            Window window = getDialog().getWindow();
            window.setLayout(435, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

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

            int start_date_y = (int) list.get(position).get("start_date_y");
            int start_date_m = (int) list.get(position).get("start_date_m");
            int start_date_d = (int) list.get(position).get("start_date_d");
            int end_date_y = (int) list.get(position).get("end_date_y");
            int end_date_m = (int) list.get(position).get("end_date_m");
            int end_date_d = (int) list.get(position).get("end_date_d");

            item.monthNum.setText((String) list.get(position).get("car_num"));

            if (start_date_y == year && start_date_m == month &&start_date_d == mDay) {
                item.monthStatus.setText("시작");
            }

            if (end_date_y == year && end_date_m == month &&end_date_d == mDay) {
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

}

class MonthCalendarView_Item {

    TextView Calendar_day;
    TextView Month_in_cnt;
    TextView Month_out_cnt;

}

class MonthinNoutView_Item {

    TextView monthNum;
    TextView monthStatus;

}