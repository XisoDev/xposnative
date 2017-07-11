package com.example.macarrow.xPos.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.macarrow.xPos.DayInfo;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.Services.Payment_Services;
import com.example.macarrow.xPos.adapter.CalcuAdapter;
import java.util.ArrayList;
import java.util.Calendar;

public class Calcu_Fragment extends Fragment implements View.OnClickListener {

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
    private TextView DateTextView;
    private GridView CalGrid;

    TextView Total_amounts, Month_amounts, Dc_coopers, Pay_amounts, Receivable, In_car_count, Out_car_count;
    int year, month, sDay;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calcu, container, false);

        final Payment_Services payment_services = new Payment_Services(getActivity(), "payment.db", null, 1);
        final Garage_Service garage_service = new Garage_Service(getActivity(), "garage.db", null, 1);

        Total_amounts = (TextView)view.findViewById(R.id.total_amounts);
        Month_amounts = (TextView)view.findViewById(R.id.month_amounts);
        Dc_coopers = (TextView)view.findViewById(R.id.dc_coopers);
        Pay_amounts = (TextView)view.findViewById(R.id.pay_amounts);
        Receivable = (TextView)view.findViewById(R.id.receivable);
        In_car_count = (TextView)view.findViewById(R.id.in_car_count);
        Out_car_count = (TextView)view.findViewById(R.id.out_car_count);
        DateTextView = (TextView)view.findViewById(R.id.dateTextView);
        CalGrid = (GridView)view.findViewById(R.id.calGrid);
        Button PrevButton = (Button)view.findViewById(R.id.prevButton);
        Button NextButton = (Button)view.findViewById(R.id.nextButton);

        mDayList = new ArrayList<DayInfo>();
        year = mThisMonthCalendar.get(Calendar.YEAR);
        month = mThisMonthCalendar.get(Calendar.MONTH)+1;
        sDay = mThisMonthCalendar.get(Calendar.DAY_OF_MONTH);
        int total_amount = payment_services.totalAmountSum(year, month, sDay);
        int month_amount = payment_services.monthAmountSum(year, month, sDay);
        int cooper_amount = payment_services.cooperAmountSum(year, month, sDay);
        int pay_amount = payment_services.payAmountSum(year, month, sDay);

        Total_amounts.setText(total_amount+"");
        Month_amounts.setText(month_amount+"");
        Dc_coopers.setText(cooper_amount+"");
        Pay_amounts.setText(pay_amount+"");
        Receivable.setText((total_amount-month_amount-cooper_amount-pay_amount)+"");

        PrevButton.setOnClickListener(this);
        NextButton.setOnClickListener(this);

        CalGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DayInfo day = mDayList.get(position);
                sDay = Integer.parseInt(day.getDay());
//        Toast.makeText(getActivity(), mThisMonthCalendar.get(Calendar.YEAR) + "/" + (mThisMonthCalendar.get(Calendar.MONTH) +1) + "/" + Integer.parseInt(day.getDay()), Toast.LENGTH_SHORT).show();

                int total_amount = payment_services.totalAmountSum(year, month, sDay);
                int month_amount = payment_services.monthAmountSum(year, month, sDay);
                int cooper_amount = payment_services.cooperAmountSum(year, month, sDay);
                int pay_amount = payment_services.payAmountSum(year, month, sDay);

                Toast.makeText(getActivity(), 0+"", Toast.LENGTH_SHORT).show();
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
        DateTextView.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
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
        mAdapter = new CalcuAdapter(getActivity(), R.layout.month_calendar_view, mDayList);
        CalGrid.setAdapter(mAdapter);
    }
}