package com.example.macarrow.xPos.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import com.example.macarrow.xPos.DayInfo;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.Services.Month_Service;
import com.example.macarrow.xPos.Services.Payment_Services;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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

    TextView Total_amounts, Nomal_amounts, Month_amounts, Dc_coopers, Pay_amounts, Receivable, In_car_counts, Out_car_counts;
    int year, month, mDay;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calcu, container, false);

        final Payment_Services payment_services = new Payment_Services(getActivity(), "payment.db", null, 1);
        final Garage_Service garage_service = new Garage_Service(getActivity(), "garage.db", null, 1);

        Total_amounts = (TextView)view.findViewById(R.id.total_amounts);
        Nomal_amounts = (TextView)view.findViewById(R.id.nomal_amounts);
        Month_amounts = (TextView)view.findViewById(R.id.month_amounts);
        Dc_coopers = (TextView)view.findViewById(R.id.dc_coopers);
        Pay_amounts = (TextView)view.findViewById(R.id.pay_amounts);
        Receivable = (TextView)view.findViewById(R.id.receivable);
        In_car_counts = (TextView)view.findViewById(R.id.in_car_counts);
        Out_car_counts = (TextView)view.findViewById(R.id.out_car_counts);
        DateTextView = (TextView)view.findViewById(R.id.dateTextView);
        CalGrid = (GridView)view.findViewById(R.id.calGrid);
        Button PrevButton = (Button)view.findViewById(R.id.prevButton);
        Button NextButton = (Button)view.findViewById(R.id.nextButton);

        mDayList = new ArrayList<DayInfo>();

        GregorianCalendar today = new GregorianCalendar();
        int tYear = today.get(Calendar.YEAR);
        int tMonth = today.get(Calendar.MONTH)+1;
        int tDay = today.get(Calendar.DAY_OF_MONTH);

        int total_amounts = payment_services.totalAmountSum(tYear, tMonth);
        int nomal_amounts = payment_services.nomalAmountSum(tYear, tMonth);
        int month_amounts = payment_services.monthAmountSum(tYear, tMonth);
        int cooper_amounts = payment_services.cooperAmountSum(tYear, tMonth);
        int pay_amounts = payment_services.payAmountSum(tYear, tMonth);
        int in_car_counts = garage_service.inCarCount(tYear, tMonth);
        int out_car_counts = garage_service.outCarCount(tYear, tMonth);

        Total_amounts.setText(total_amounts+"원");
        Nomal_amounts.setText(nomal_amounts + "원");
        Month_amounts.setText(month_amounts+"원");
        Dc_coopers.setText(cooper_amounts+"원");
        Pay_amounts.setText(pay_amounts+"원");
        Receivable.setText((total_amounts-pay_amounts-cooper_amounts)+"원");
        In_car_counts.setText(in_car_counts+"대");
        Out_car_counts.setText(out_car_counts+"대");

        PrevButton.setOnClickListener(this);
        NextButton.setOnClickListener(this);

        CalGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DayInfo day = mDayList.get(position);
                year = mThisMonthCalendar.get(Calendar.YEAR);
                month = mThisMonthCalendar.get(Calendar.MONTH)+1;
                mDay = Integer.parseInt(day.getDay());

//                int total_amounts = payment_services.totalAmountSum(year, month, mDay);
//                int nomal_amounts = payment_services.nomalAmountSum(year, month, mDay);
//                int month_amounts = payment_services.monthAmountSum(year, month, mDay);
//                int cooper_amounts = payment_services.cooperAmountSum(year, month, mDay);
//                int pay_amounts = payment_services.payAmountSum(year, month, mDay);
//                int in_car_counts = garage_service.inCarCount(year, month, mDay);
//                int out_car_counts = garage_service.outCarCount(year, month, mDay);
//
//                Total_amounts.setText(total_amounts+"원");
//                Nomal_amounts.setText(nomal_amounts+"원");
//                Month_amounts.setText(month_amounts+"원");
//                Dc_coopers.setText(cooper_amounts+"원");
//                Pay_amounts.setText(pay_amounts+"원");
//                Receivable.setText((total_amounts-pay_amounts-cooper_amounts)+"원");
//                In_car_counts.setText(in_car_counts+"대");
//                Out_car_counts.setText(out_car_counts+"대");

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
        mAdapter = new CalcuAdapter(getActivity(), R.layout.calcu_calendar_view, mDayList);
        CalGrid.setAdapter(mAdapter);
    }

    public class CalcuAdapter extends BaseAdapter {

        private ArrayList<DayInfo> mDayList;
        private Context context;
        private int mResource;
        private LayoutInflater mflater;

        public CalcuAdapter (Context context, int textResource, ArrayList<DayInfo> dayList) {
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
            ViewHolder dayViewHolder;

            if (convertView == null) {

                convertView = mflater.inflate(mResource, null);
                dayViewHolder = new ViewHolder();
                dayViewHolder.calendar_day = (TextView) convertView.findViewById(R.id.calendar_day);
                dayViewHolder.total_amount = (TextView) convertView.findViewById(R.id.total_amount);
                dayViewHolder.receivable = (TextView) convertView.findViewById(R.id.receivable);
                dayViewHolder.pay_amount = (TextView) convertView.findViewById(R.id.pay_amount);
                convertView.setTag(dayViewHolder);

            } else {

                dayViewHolder = (ViewHolder) convertView.getTag();

            } if (day != null) {

                Calendar mCal = Calendar.getInstance();
                Integer today = mCal.get(Calendar.DAY_OF_MONTH);
                String sToday = String.valueOf(today);
                dayViewHolder.calendar_day.setText(day.getDay());

                long now = System.currentTimeMillis();
                final Date date = new Date(now);
                final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
                final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
                final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

                Payment_Services payment_services = new Payment_Services(context, "month.db", null, 1);
                int year = Integer.parseInt(curYearFormat.format(date));
                int month = Integer.parseInt(curMonthFormat.format(date));
                int tDay = Integer.parseInt(curDayFormat.format(date));
                mDay = Integer.parseInt(day.getDay());
                int total_amount = payment_services.totalAmountSumDay(mThisMonthCalendar.get(Calendar.YEAR), (mThisMonthCalendar.get(Calendar.MONTH) + 1), mDay);
                int pay_amount = payment_services.payAmountSumDay(mThisMonthCalendar.get(Calendar.YEAR), (mThisMonthCalendar.get(Calendar.MONTH) + 1), mDay);

                if ((mThisMonthCalendar.get(Calendar.MONTH) + 1) == month) {
                    dayViewHolder.total_amount.setText(total_amount+"원");
                    dayViewHolder.receivable.setText((total_amount-pay_amount)+"원");
                    dayViewHolder.pay_amount.setText(pay_amount+"원");
                }

                if (day.getDay() == sToday && position >= tDay && (mThisMonthCalendar.get(Calendar.MONTH) + 1) == month) {
                    dayViewHolder.calendar_day.setTextColor(Color.GREEN);

                } else if (day.isInMonth()) {

                    if (position % 7 == 0) {
                        dayViewHolder.calendar_day.setTextColor(Color.RED);
                    } else if (position % 7 == 6) {
                        dayViewHolder.calendar_day.setTextColor(Color.BLUE);
                    }

                } else {
                    dayViewHolder.calendar_day.setText("");
                    dayViewHolder.calendar_day.setText("");
                    dayViewHolder.calendar_day.setText("");
                }
            }
            return convertView;
        }
    }
}

class ViewHolder {

    TextView calendar_day;
    TextView total_amount;
    TextView receivable;
    TextView pay_amount;

}