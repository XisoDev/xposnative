package com.example.macarrow.xPos.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import com.example.macarrow.xPos.DayInfo;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.adapter.CalcuAdapter;
import java.util.ArrayList;
import java.util.Calendar;

public class Calcu_Fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    public Calcu_Fragment(){}

    public static int SUNDAY        = 1;
    public static int MONDAY        = 2;
    public static int TUESDAY       = 3;
    public static int WEDNSESDAY    = 4;
    public static int THURSDAY      = 5;
    public static int FRIDAY        = 6;
    public static int SATURDAY      = 7;

    private ArrayList<DayInfo> mDayList;
    private CalcuAdapter mAdapter;
    private TextView Calendar_date;
    private GridView Calendar_view;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calcu, container, false);

        final TextView Total_amounts = (TextView)view.findViewById(R.id.total_amounts);
        final TextView Month_amounts = (TextView)view.findViewById(R.id.month_amounts);
        final TextView Dc_coopers = (TextView)view.findViewById(R.id.dc_coopers);
        final TextView Pay_amounts = (TextView)view.findViewById(R.id.pay_amounts);
        final TextView Receivable = (TextView)view.findViewById(R.id.receivable);
        final TextView In_car_count = (TextView)view.findViewById(R.id.in_car_count);
        final TextView Out_car_count = (TextView)view.findViewById(R.id.out_car_count);
        Calendar_date = (TextView)view.findViewById(R.id.calendar_date);
        Calendar_view = (GridView)view.findViewById(R.id.calendar_view);
        Button Last = (Button)view.findViewById(R.id.last);
        Button Next = (Button)view.findViewById(R.id.next);

        mDayList = new ArrayList<DayInfo>();

        Last.setOnClickListener(this);
        Next.setOnClickListener(this);
        Calendar_view.setOnItemClickListener(this);

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
        Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH)+"");

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);
        Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH)+"");

        if(dayOfMonth == SUNDAY) {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth-1)-1;

        // 캘린더 타이틀(년월 표시)을 세팅한다.
        Calendar_date.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 " + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        DayInfo day;

        Log.e("DayOfMOnth", dayOfMonth+"");

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

    /**
     * 지난달의 Calendar 객체를 반환합니다.
     *
     * @param calendar
     * @return LastMonthCalendar
     */
    private Calendar getLastMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        Calendar_date.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    /**
     * 다음달의 Calendar 객체를 반환합니다.
     *
     * @param calendar
     * @return NextMonthCalendar
     */
    private Calendar getNextMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        Calendar_date.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.last:
                mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
            case R.id.next:
                mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
        }
    }

    private void initCalendarAdapter() {
        mAdapter = new CalcuAdapter(getActivity(), R.layout.month_calendar_view, mDayList);
        Calendar_view.setAdapter(mAdapter);
    }

}