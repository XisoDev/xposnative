package com.example.macarrow.xPos.fragment.Month;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import com.example.macarrow.xPos.DayInfo;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.adapter.MonthCalendarViewAdapter;
import com.example.macarrow.xPos.fragment.Month_Fragment;
import java.util.ArrayList;
import java.util.Calendar;

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

                Bundle args = new Bundle();
                args.putInt("year", year);
                args.putInt("month", month);
                args.putInt("day", mDay);
                Month_inNout month_inNout = new Month_inNout();
                month_inNout.setArguments(args);
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
}