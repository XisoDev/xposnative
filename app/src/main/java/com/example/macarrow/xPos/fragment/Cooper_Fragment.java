package com.example.macarrow.xPos.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Cooper_Services;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.adapter.CooperViewAdapter;
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

                    private FragmentManager fm = getFragmentManager();

                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

                        View view = getActivity().getLayoutInflater().inflate(R.layout.cooper_add, null);
                        final TextView Title_cooper = (TextView) view.findViewById(R.id.title_cooper);
                        final EditText Coop_title = (EditText) view.findViewById(R.id.coop_title);
                        final EditText Coop_tel = (EditText) view.findViewById(R.id.coop_tel);
                        final EditText Coop_address = (EditText) view.findViewById(R.id.coop_address);
                        final EditText Coop_user_name = (EditText) view.findViewById(R.id.coop_user_name);
                        final EditText Minute_max = (EditText) view.findViewById(R.id.minute_max);
                        final EditText Amount_unit = (EditText) view.findViewById(R.id.amount_unit);
                        final EditText Minute_unit = (EditText) view.findViewById(R.id.minute_unit);
                        final Button Add_cooper = (Button) view.findViewById(R.id.add_cooper);
                        final Button Close_cooper = (Button) view.findViewById(R.id.close_cooper);

                        adb.setView(view);
                        // 다이얼 로그 크기 조정
                        final Dialog dialog = adb.create();
                        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                        params.copyFrom(dialog.getWindow().getAttributes());
                        params.width = 845;
                        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setAttributes(params);
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Title_cooper.setText("업체 추가");
                        Add_cooper.setVisibility(View.VISIBLE);

                        View.OnClickListener clickListener = new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {

                                    case R.id.add_cooper:

                                        try {

                                            if (Coop_title.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "업체 명을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Coop_tel.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "전화번호를 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Coop_address.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "주소를 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Coop_user_name.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "대표자 명을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Minute_max.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "최대 지원 시간 (분)을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Amount_unit.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "추가요금 (원)을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Minute_unit.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "추가요금 단위(분)을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        String coop_title = Coop_title.getText().toString();
                                        String coop_tel = Coop_tel.getText().toString();
                                        String coop_address = Coop_address.getText().toString();
                                        String coop_user_name = Coop_user_name.getText().toString();
                                        int minute_max = Integer.parseInt(Minute_max.getText().toString());
                                        int amount_unit = Integer.parseInt(Amount_unit.getText().toString());
                                        int minute_unit = Integer.parseInt(Minute_unit.getText().toString());
                                        cooper_services.insert(coop_title, coop_tel, coop_address, coop_user_name, minute_max, amount_unit, minute_unit);
                                        fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment(cooper)).commit();
                                        dialog.dismiss();
                                        break;

                                    case R.id.close_cooper:

                                        fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment(cooper)).commit();
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        };
                        Add_cooper.setOnClickListener(clickListener);
                        Close_cooper.setOnClickListener(clickListener);
                    }
                });

                Cooper_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final int idx = (int) list.get(position).get("idx");
                        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

                        View v = getActivity().getLayoutInflater().inflate(R.layout.cooper_add, null);
                        final TextView Title_cooper = (TextView) v.findViewById(R.id.title_cooper);
                        final EditText Coop_title = (EditText) v.findViewById(R.id.coop_title);
                        final EditText Coop_tel = (EditText) v.findViewById(R.id.coop_tel);
                        final EditText Coop_address = (EditText) v.findViewById(R.id.coop_address);
                        final EditText Coop_user_name = (EditText) v.findViewById(R.id.coop_user_name);
                        final EditText Minute_max = (EditText) v.findViewById(R.id.minute_max);
                        final EditText Amount_unit = (EditText) v.findViewById(R.id.amount_unit);
                        final EditText Minute_unit = (EditText) v.findViewById(R.id.minute_unit);
                        final LinearLayout Is_end_lay = (LinearLayout) v.findViewById(R.id.is_end_lay);
                        final Switch Is_end = (Switch) v.findViewById(R.id.is_end);
                        final Button Update_cooper = (Button) v.findViewById(R.id.update_cooper);
                        final Button Delete_cooper = (Button) v.findViewById(R.id.delete_cooper);
                        final Button Close_cooper = (Button) v.findViewById(R.id.close_cooper);

                        adb.setView(v);
                        // 다이얼 로그 크기 조정
                        final Dialog dialog = adb.create();
                        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                        params.copyFrom(dialog.getWindow().getAttributes());
                        params.width = 845;
                        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setAttributes(params);
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Title_cooper.setText("업체 수정 / 삭제");
                        Is_end_lay.setVisibility(View.VISIBLE);
                        Update_cooper.setVisibility(View.VISIBLE);
                        Delete_cooper.setVisibility(View.VISIBLE);

                        final Map<String, Object> map = cooper_services.getResultForUpdate(idx);
                        Coop_title.setText((String) map.get("coop_title"));
                        Coop_tel.setText((String) map.get("coop_tel"));
                        Coop_address.setText((String) map.get("coop_address"));
                        Coop_user_name.setText((String) map.get("coop_user_name"));
                        Minute_max.setText((Integer) map.get("minute_max") + "");
                        Amount_unit.setText((Integer) map.get("amount_unit") + "");
                        Minute_unit.setText((Integer) map.get("minute_unit") + "");

                        // Switch
                        final String isEnd = (String) map.get("is_end");
                        if (isEnd.equals("N")) {
                            Is_end.setChecked(true);
                            Is_end.setText("활성");
                        } else if (isEnd.equals("Y")) {
                            Is_end.setChecked(false);
                            Is_end.setText("종료");
                        }

                        Is_end.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    Is_end.setText("활성");
                                } else if (!isChecked) {
                                    Is_end.setText("종료");
                                }
                            }
                        });

                        View.OnClickListener clickListener = new View.OnClickListener() {
                            private FragmentManager fm = getFragmentManager();
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {

                                    case R.id.update_cooper:

                                        try {

                                            if (Coop_title.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "업체 명을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Coop_tel.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "전화번호를 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Coop_address.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "주소를 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Coop_user_name.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "대표자 명을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Minute_max.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "최대 지원 시간 (분)을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Amount_unit.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "추가요금 (원)을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;

                                            } else if (Minute_unit.getText().toString().equals("")) {
                                                Toast.makeText(v.getContext(), "추가요금 단위(분)을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        String coop_title = Coop_title.getText().toString();
                                        String coop_tel = Coop_tel.getText().toString();
                                        String coop_address = Coop_address.getText().toString();
                                        String coop_user_name = Coop_user_name.getText().toString();
                                        int minute_max = Integer.parseInt(Minute_max.getText().toString());
                                        int amount_unit = Integer.parseInt(Amount_unit.getText().toString());
                                        int minute_unit = Integer.parseInt(Minute_unit.getText().toString());
                                        String is_end = "";
                                        if (Is_end.getText().equals("활성")) {
                                            is_end = "N";
                                        } else if (Is_end.getText().equals("종료")) {
                                            is_end = "Y";
                                        }

                                        cooper_services.update(coop_title, coop_tel, coop_address, coop_user_name, minute_unit, minute_max, amount_unit, is_end, idx);
                                        fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment(cooper)).commit();
                                        dialog.dismiss();
                                        break;

                                    case R.id.delete_cooper:

                                        cooper_services.delete(idx);
                                        fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment(cooper)).commit();
                                        dialog.dismiss();
                                        break;

                                    case R.id.close_cooper:

                                        fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment(cooper)).commit();
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        };
                        Update_cooper.setOnClickListener(clickListener);
                        Delete_cooper.setOnClickListener(clickListener);
                        Close_cooper.setOnClickListener(clickListener);
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

                            } else {

                                Map<String, Object> map = garage_service.getCooper(coop_title);

                                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                                adb.setTitle(endDate+"개");
                                adb.setNegativeButton("닫기", null);
                                adb.show();

                            }
                        }









//                        CooperViewAdapter P_adapter = new CooperViewAdapter(getActivity(), R.layout.cooper_item, plist);
//                        P_adapter.notifyDataSetChanged();
//                        cooperList.setAdapter(P_adapter);

                    }
                });
                break;

            case "day":

                view = inflater.inflate(R.layout.cooper_period, container, false);
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