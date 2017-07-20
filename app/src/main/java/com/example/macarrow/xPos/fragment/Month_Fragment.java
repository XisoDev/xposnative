package com.example.macarrow.xPos.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Month_Service;
import com.example.macarrow.xPos.adapter.MonthViewAdapter;
import com.example.macarrow.xPos.fragment.Month.Month_Add;
import com.example.macarrow.xPos.fragment.Month.Month_Calendar;
import com.melnykov.fab.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class Month_Fragment extends Fragment {

    private FloatingActionButton fab;
    private String month;
    @SuppressLint("ValidFragment")
    public Month_Fragment(String month) { this.month = month; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Month_Service month_service = new Month_Service(getActivity(), "month.db", null, 1);

        View view = inflater.inflate(R.layout.month, container, false);
        final TextView Month_all = (TextView)view.findViewById(R.id.month_all);
        final TextView Month_expired = (TextView)view.findViewById(R.id.month_expired);
        final TextView Month_wait = (TextView)view.findViewById(R.id.month_wait);
        final TextView Month_calendar = (TextView)view.findViewById(R.id.month_calendar);
        final ListView monthView = (ListView)view.findViewById(R.id.month_view);

        String status = null;

        switch (month) {

            case "possibility":
                status = "possibility";

                Month_all.setBackground(getResources().getDrawable(R.drawable.nav_bg_on));
                Month_all.setTextColor(getResources().getColor(R.color.nav_txt_on));

                break;

            case "expired":
                status = "expired";

                Month_expired.setBackground(getResources().getDrawable(R.drawable.nav_bg_on));
                Month_expired.setTextColor(getResources().getColor(R.color.nav_txt_on));

                break;

            case "wait":
                status = "wait";

                Month_wait.setBackground(getResources().getDrawable(R.drawable.nav_bg_on));
                Month_wait.setTextColor(getResources().getColor(R.color.nav_txt_on));

                break;
        }

        Calendar calendar = Calendar.getInstance();
        Calendar today = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int toDay = Integer.parseInt(sdf.format(today.getTime()));

        final List<Map<String, Object>> list = month_service.getResult(toDay, status);
        final MonthViewAdapter adapter = new MonthViewAdapter(getActivity(), R.layout.month_item, list);
        monthView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToListView(monthView);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString("status", "new");
                args.putString("car_num", "");
                Month_Add month_add = new Month_Add();
                month_add.setArguments(args);
                month_add.setCancelable(false);
                month_add.show(getFragmentManager(), "month_add");
            }
        });

        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            }
        });

        // Button case 처리
        View.OnClickListener clickListener = new View.OnClickListener() {
            private FragmentManager fm = getFragmentManager();
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.month_all :

                        month = "possibility";
                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment(month)).commit();
                        break;

                    case R.id.month_expired :

                        month = "expired";
                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment(month)).commit();
                        break;

                    case R.id.month_wait :

                        month = "wait";
                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment(month)).commit();
                        break;

                    case R.id.month_calendar :

                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Calendar()).commit();
                        break;
                }
            }
        };

        Month_all.setOnClickListener(clickListener);
        Month_expired.setOnClickListener(clickListener);
        Month_wait.setOnClickListener(clickListener);
        Month_calendar.setOnClickListener(clickListener);

        return view;

    }
}