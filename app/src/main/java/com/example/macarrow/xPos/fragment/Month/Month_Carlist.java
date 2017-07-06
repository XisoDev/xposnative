package com.example.macarrow.xPos.fragment.Month;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.Services.Month_Service;
import com.example.macarrow.xPos.adapter.PanelMonthTypeViewAdapter;
import com.example.macarrow.xPos.fragment.Current_Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class Month_Carlist extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        String carNum = mArgs.getString("car_num");

        final Month_Service monthService = new Month_Service(getActivity(), "month.db", null, 1);
        final Garage_Service garageService = new Garage_Service(getActivity(), "garage.db", null, 1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.panel_cartypelist, null);

        Calendar calendar = Calendar.getInstance();
        Calendar today = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int toDay = Integer.parseInt(sdf.format(today.getTime()));

        GridView gridView = (GridView)view.findViewById(R.id.panel_cartypeList);
        final List<Map<String, Object>> list = monthService.getByCarNum(toDay, carNum);
        TextView Title = (TextView)view.findViewById(R.id.title);
        PanelMonthTypeViewAdapter adapter = new PanelMonthTypeViewAdapter(getActivity(), R.layout.panel_monthlist_item, list);
        gridView.setAdapter(adapter);

        builder.setNegativeButton("닫기", null);
        builder.setView(view);

        // 타이틀
        Title.setText("월차 선택");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            FragmentManager fm = getFragmentManager();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int idx = (int) list.get(position).get("idx");
                String car_num = (String) list.get(position).get("car_num");
                long start_date = System.currentTimeMillis();
                String car_type_title = (String) list.get(position).get("car_type_title");

                if (garageService.doubleCarNum(car_num) > 0) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setTitle("입차된 월차입니다");
                    adb.setNegativeButton("닫기",null);
                    adb.show();
                    return;
                } else {
                    garageService.insert(start_date, car_num, car_type_title, 0, 0, 0, 0, 0, idx, 0, 0, 0);
                    fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                }
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(845, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}