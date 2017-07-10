package com.example.macarrow.xPos.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Cooper_Services;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.adapter.CooperPeriodAdapter;
import com.example.macarrow.xPos.adapter.CooperViewAdapter;
import com.example.macarrow.xPos.fragment.Garage.Cooper_Add;
import com.example.macarrow.xPos.fragment.Garage.Garage_View;
import com.melnykov.fab.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class Cooper_Fragment extends Fragment {

    private FloatingActionButton fab;
    private String cooper;
    int year, month, day;
    int startDate, endDate;
    TextView Search_start, Search_end;

    @SuppressLint("ValidFragment")
    public Cooper_Fragment(String cooper) { this.cooper = cooper; }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Cooper_Services cooper_services = new Cooper_Services(getActivity(), "cooper.db", null, 1);
        final Garage_Service garage_service = new Garage_Service(getActivity(), "garage.db", null, 1);

        View view = null;
        switch (cooper) {

            case "cooper":

                view = inflater.inflate(R.layout.cooper, container, false);

                ListView Cooper_list = (ListView) view.findViewById(R.id.cooper_list);
                final List<Map<String, Object>> list = cooper_services.getResult();
                CooperViewAdapter adapter = new CooperViewAdapter(getActivity(), R.layout.cooper_item, list);
                adapter.notifyDataSetChanged();
                Cooper_list.setAdapter(adapter);

                fab = (FloatingActionButton) view.findViewById(R.id.fab);
                fab.attachToListView(Cooper_list);
                fab.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Bundle args = new Bundle();
                        args.putString("status", "new");
                        Cooper_Add cooper_add = new Cooper_Add();
                        cooper_add.setArguments(args);
                        cooper_add.setCancelable(false);
                        cooper_add.show(getFragmentManager(), "cooper_add");

                    }
                });

                Cooper_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final int idx = (int) list.get(position).get("idx");
                        Bundle args = new Bundle();
                        args.putString("status", "modify");
                        args.putInt("idx", idx);
                        Cooper_Add cooper_add = new Cooper_Add();
                        cooper_add.setArguments(args);
                        cooper_add.setCancelable(false);
                        cooper_add.show(getFragmentManager(), "cooper_add");

                    }
                });
                break;

            case "period":

                view = inflater.inflate(R.layout.cooper_period, container, false);

                final ListView cooperList = (ListView) view.findViewById(R.id.cooper_list);
                final EditText Search = (EditText) view.findViewById(R.id.search);
                Search_start = (TextView) view.findViewById(R.id.search_start);
                Search_end = (TextView) view.findViewById(R.id.search_end);
                final Button Search_btn = (Button) view.findViewById(R.id.search_btn);

                GregorianCalendar calendar = new GregorianCalendar();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                final int maxday = calendar.getActualMaximum((calendar.DAY_OF_MONTH));

                Search_start.setText(year+"."+(month+1)+"."+1+"");
                Search_end.setText(year+"."+(month+1)+"."+maxday+"");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                int yearMonth = Integer.parseInt(sdf.format(calendar.getTime()));
                String str1 = yearMonth+"01";
                String str2 = yearMonth+""+maxday;

                startDate = Integer.parseInt(str1.toString());
                endDate = Integer.parseInt(str2.toString());

                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (v.getId()) {

                            case R.id.search_start:

                                new DatePickerDialog(getActivity(), start, year, month, 1).show();
                                break;

                            case R.id.search_end:

                                new DatePickerDialog(getActivity(), end, year, month, maxday).show();
                                break;
                        }
                    }
                };
                Search_start.setOnClickListener(onClickListener);
                Search_end.setOnClickListener(onClickListener);

                Search_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String coop_title = Search.getText().toString();

                        if (coop_title.equals("")) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setTitle("업체명을 입력해주세요");
                            adb.setNegativeButton("닫기", null);
                            adb.show();

                        } else {

                            if (cooper_services.coopTitle(coop_title) <= 0) {

                                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                                adb.setTitle("없는 업체명입니다");
                                adb.setNegativeButton("닫기", null);
                                adb.show();
                                Search.setText("");

                            } else {

                                final List<Map<String, Object>> plist = garage_service.getPeriod(coop_title, startDate, endDate);
                                CooperPeriodAdapter P_adapter = new CooperPeriodAdapter(getActivity(), R.layout.cooper_period_item, plist);
                                cooperList.setAdapter(P_adapter);
                                Search.setText("");

                                cooperList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        int idx = (int) plist.get(position).get("idx");
                                        int total_amount = (int) plist.get(position).get("total_amount");

                                        Bundle args = new Bundle();
                                        args.putInt("idx", idx);
                                        args.putInt("result_charge", total_amount);
                                        Garage_View garage_view = new Garage_View();
                                        garage_view.setArguments(args);
                                        garage_view.setCancelable(false);
                                        garage_view.show(getFragmentManager(), "garage_view");

                                    }
                                });
                            }
                        }
                    }
                });
                break;

            case "day":

                view = inflater.inflate(R.layout.cooper_period, container, false);

                final ListView cooperDayList = (ListView) view.findViewById(R.id.cooper_list);
                final EditText SearchDay = (EditText) view.findViewById(R.id.search);
                final Button Search_Daybtn = (Button) view.findViewById(R.id.search_btn);
                final TextView At = (TextView) view.findViewById(R.id.at);
                Search_start = (TextView) view.findViewById(R.id.search_start);
                Search_end = (TextView) view.findViewById(R.id.search_end);

                Search_start.setVisibility(View.GONE);
                At.setVisibility(View.GONE);

                GregorianCalendar today = new GregorianCalendar();
                year = today.get(Calendar.YEAR);
                month = today.get(Calendar.MONTH);
                day = today.get(Calendar.DAY_OF_MONTH);

                Search_end.setText(year+"."+(month+1)+"."+day+"");
                GregorianCalendar cal = new GregorianCalendar();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                endDate = Integer.parseInt(simpleDateFormat.format(cal.getTime()).toString());

                View.OnClickListener onClickListenerDay = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (v.getId()) {

                            case R.id.search_end:

                                new DatePickerDialog(getActivity(), end, year, month, day).show();
                                break;
                        }
                    }
                };
                Search_end.setOnClickListener(onClickListenerDay);

                Search_Daybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String coop_title = SearchDay.getText().toString();

                        if (coop_title.equals("")) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setTitle("업체명을 입력해주세요");
                            adb.setNegativeButton("닫기", null);
                            adb.show();

                        } else {

                            if (cooper_services.coopTitle(coop_title) <= 0) {

                                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                                adb.setTitle("없는 업체명입니다");
                                adb.setNegativeButton("닫기", null);
                                adb.show();
                                SearchDay.setText("");

                            } else {

                                final List<Map<String, Object>> dlist = garage_service.getDay(coop_title,  endDate);
                                CooperPeriodAdapter D_adapter = new CooperPeriodAdapter(getActivity(), R.layout.cooper_period_item, dlist);
                                cooperDayList.setAdapter(D_adapter);
                                SearchDay.setText("");

                                cooperDayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        int idx = (int) dlist.get(position).get("idx");
                                        int total_amount = (int) dlist.get(position).get("total_amount");

                                        Bundle args = new Bundle();
                                        args.putInt("idx", idx);
                                        args.putInt("result_charge", total_amount);
                                        Garage_View garage_view = new Garage_View();
                                        garage_view.setArguments(args);
                                        garage_view.setCancelable(false);
                                        garage_view.show(getFragmentManager(), "garage_view");

                                    }
                                });
                            }
                        }
                    }
                });
                break;
        }

        final TextView Cooper = (TextView) view.findViewById(R.id.cooper);
        final TextView Period = (TextView) view.findViewById(R.id.period);
        final TextView Day = (TextView) view.findViewById(R.id.day);

        // Button case 처리
        View.OnClickListener clickListener = new View.OnClickListener() {
            private FragmentManager fm = getFragmentManager();
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.cooper:

                        cooper = "cooper";
                        fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment(cooper)).commit();
                        break;

                    case R.id.period:

                        cooper = "period";
                        fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment(cooper)).commit();
                        break;

                    case R.id.day:

                        cooper = "day";
                        fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment(cooper)).commit();
                        break;
                }
            }
        };
        Cooper.setOnClickListener(clickListener);
        Period.setOnClickListener(clickListener);
        Day.setOnClickListener(clickListener);

        return view;
    }

    private DatePickerDialog.OnDateSetListener start = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Search_start.setText(String.format("%d. %d. %d", year, monthOfYear+1, dayOfMonth));
            Calendar startd = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            startDate = Integer.parseInt(sdf.format(startd.getTime()));
            //Toast.makeText(getActivity(), startDate+"", Toast.LENGTH_SHORT).show();
        }
    };
    private DatePickerDialog.OnDateSetListener end = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Search_end.setText(String.format("%d. %d. %d", year, monthOfYear+1, dayOfMonth));
            Calendar endd = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            endDate = Integer.parseInt(sdf.format(endd.getTime()));
        }
    };
}