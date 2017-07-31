package com.example.macarrow.xPos.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import com.example.macarrow.xPos.DayInfo;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.Services.Payment_Services;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Payment_Services payment_services;
    private Garage_Service garage_service;

    TextView Total_amounts, Nomal_amounts, Month_amounts, Dc_coopers, Pay_amounts, Receivable, In_car_counts, Out_car_counts;
    int year, month, mDay;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calcu, container, false);

        payment_services = new Payment_Services(getActivity(), "payment.db", null, 1);
        garage_service = new Garage_Service(getActivity(), "garage.db", null, 1);

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

        PrevButton.setOnClickListener(this);
        NextButton.setOnClickListener(this);

        CalGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DayInfo day = mDayList.get(position);
                year = mThisMonthCalendar.get(Calendar.YEAR);
                month = mThisMonthCalendar.get(Calendar.MONTH)+1;
                mDay = Integer.parseInt(day.getDay());

                Calcu_Fragment.Calcu_daily calcu_daily = new Calcu_Fragment.Calcu_daily();
                calcu_daily.setCancelable(false);
                calcu_daily.show(getFragmentManager(), "calcu_daily");

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

        int total_amounts = payment_services.totalAmountSum(mThisMonthCalendar.get(Calendar.YEAR), (mThisMonthCalendar.get(Calendar.MONTH) + 1));
        int nomal_amounts = payment_services.nomalAmountSum(mThisMonthCalendar.get(Calendar.YEAR), (mThisMonthCalendar.get(Calendar.MONTH) + 1));
        int month_amounts = payment_services.monthAmountSum(mThisMonthCalendar.get(Calendar.YEAR), (mThisMonthCalendar.get(Calendar.MONTH) + 1));
        int cooper_amounts = payment_services.cooperAmountSum(mThisMonthCalendar.get(Calendar.YEAR), (mThisMonthCalendar.get(Calendar.MONTH) + 1));
        int pay_amounts = payment_services.payAmountSum(mThisMonthCalendar.get(Calendar.YEAR), (mThisMonthCalendar.get(Calendar.MONTH) + 1));
        int in_car_counts = garage_service.inCarCount(mThisMonthCalendar.get(Calendar.YEAR), (mThisMonthCalendar.get(Calendar.MONTH) + 1));
        int out_car_counts = garage_service.outCarCount(mThisMonthCalendar.get(Calendar.YEAR), (mThisMonthCalendar.get(Calendar.MONTH) + 1));

        Total_amounts.setText(String.format("%,d원", total_amounts));
        Nomal_amounts.setText(String.format("%,d원", nomal_amounts));
        Month_amounts.setText(String.format("%,d원", month_amounts));
        Dc_coopers.setText(String.format("%,d원", cooper_amounts));
        Pay_amounts.setText(String.format("%,d원", pay_amounts));
        Receivable.setText(String.format("%,d원", total_amounts-pay_amounts-cooper_amounts));
        In_car_counts.setText(in_car_counts+"대");
        Out_car_counts.setText(out_car_counts+"대");

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
                dayViewHolder.Calendar_day = (TextView) convertView.findViewById(R.id.calendar_day);
                dayViewHolder.Total_amount = (TextView) convertView.findViewById(R.id.total_amount);
                dayViewHolder.Receivable = (TextView) convertView.findViewById(R.id.receivable);
                dayViewHolder.Pay_amount = (TextView) convertView.findViewById(R.id.pay_amount);
                convertView.setTag(dayViewHolder);

            } else {

                dayViewHolder = (ViewHolder) convertView.getTag();

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

                int year = Integer.parseInt(curYearFormat.format(date));
                int month = Integer.parseInt(curMonthFormat.format(date));
                int tDay = Integer.parseInt(curDayFormat.format(date));
                mDay = Integer.parseInt(day.getDay());
                int total_amount = payment_services.totalAmountSumDay(mThisMonthCalendar.get(Calendar.YEAR), (mThisMonthCalendar.get(Calendar.MONTH) + 1), mDay);
                int pay_amount = payment_services.payAmountSumDay(mThisMonthCalendar.get(Calendar.YEAR), (mThisMonthCalendar.get(Calendar.MONTH) + 1), mDay);

                if (day.isInMonth() || (mThisMonthCalendar.get(Calendar.MONTH) + 1) == month) {
                    dayViewHolder.Total_amount.setText(String.format("매출 %,d원", total_amount));
                    dayViewHolder.Receivable.setText(String.format("미수 %,d원", total_amount-pay_amount));
                    dayViewHolder.Pay_amount.setText(String.format("결제 %,d원", pay_amount));
                } if (total_amount <= 0) {
                    dayViewHolder.Total_amount.setText("");
                    dayViewHolder.Receivable.setText("");
                    dayViewHolder.Pay_amount.setText("");
                }

                if (day.getDay() == sToday && position >= tDay && (mThisMonthCalendar.get(Calendar.MONTH) + 1) == month) {
                    dayViewHolder.Calendar_day.setTextColor(Color.GREEN);

                } else if (day.isInMonth()) {

                    if (position % 7 == 0) {
                        dayViewHolder.Calendar_day.setTextColor(Color.RED);
                    } else if (position % 7 == 6) {
                        dayViewHolder.Calendar_day.setTextColor(Color.BLUE);
                    }

                } else {
                    dayViewHolder.Calendar_day.setText("");
                    dayViewHolder.Total_amount.setText("");
                    dayViewHolder.Receivable.setText("");
                    dayViewHolder.Pay_amount.setText("");
                }
            }
            return convertView;
        }
    }

    public class Calcu_daily extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            final View view = inflater.inflate(R.layout.calcu_daily, null);
            final TextView Title = (TextView) view.findViewById(R.id.title);

            Title.setText(year+"년"+month+"월"+mDay+"일"+"  "+"정산");

            Total_amounts = (TextView)view.findViewById(R.id.total_amounts);
            Nomal_amounts = (TextView)view.findViewById(R.id.nomal_amounts);
            Month_amounts = (TextView)view.findViewById(R.id.month_amounts);
            Dc_coopers = (TextView)view.findViewById(R.id.dc_coopers);
            Pay_amounts = (TextView)view.findViewById(R.id.pay_amounts);
            Receivable = (TextView)view.findViewById(R.id.receivable);
            In_car_counts = (TextView)view.findViewById(R.id.in_car_counts);
            Out_car_counts = (TextView)view.findViewById(R.id.out_car_counts);

            int total_amounts = payment_services.totalAmountSumDay(year, month, mDay);
            int nomal_amounts = payment_services.nomalAmountSumDay(year, month, mDay);
            int month_amounts = payment_services.monthAmountSumDay(year, month, mDay);
            int cooper_amounts = payment_services.cooperAmountSumDay(year, month, mDay);
            int pay_amounts = payment_services.payAmountSumDay(year, month, mDay);
            int in_car_counts = garage_service.inCarCountDay(year, month, mDay);
            int out_car_counts = garage_service.outCarCountDay(year, month, mDay);

            Total_amounts.setText(String.format("%,d원", total_amounts));
            Nomal_amounts.setText(String.format("%,d원", nomal_amounts));
            Month_amounts.setText(String.format("%,d원", month_amounts));
            Dc_coopers.setText(String.format("%,d원", cooper_amounts));
            Pay_amounts.setText(String.format("%,d원", pay_amounts));
            Receivable.setText(String.format("%,d원", total_amounts-pay_amounts-cooper_amounts));
            In_car_counts.setText(in_car_counts+"대");
            Out_car_counts.setText(out_car_counts+"대");

            builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.content_fragment, new Calcu_Fragment()).commit();
                }
            });
            builder.setView(view);

            return builder.create();
        }
        @Override
        public void onResume() {
            super.onResume();
            Window window = getDialog().getWindow();
            window.setLayout(535, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }
}

class ViewHolder {

    TextView Calendar_day;
    TextView Total_amount;
    TextView Receivable;
    TextView Pay_amount;

}