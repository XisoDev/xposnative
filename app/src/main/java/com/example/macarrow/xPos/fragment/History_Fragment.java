package com.example.macarrow.xPos.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.adapter.HistoryViewAdapter;
import com.example.macarrow.xPos.fragment.Garage.Garage_View;
import java.util.List;
import java.util.Map;

public class History_Fragment extends Fragment {

    private String history;
    String car_num;
    String status;

    @SuppressLint("ValidFragment")
    public History_Fragment(String history) { this.history = history; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history, container, false);
        final Garage_Service garageService = new Garage_Service(getActivity(), "garage.db", null, 1);
        final TextView historyAll = (TextView)view.findViewById(R.id.history_all);
        final TextView historyIn = (TextView)view.findViewById(R.id.history_in);
        final TextView historyOut = (TextView)view.findViewById(R.id.history_out);
        final TextView historyNoPay = (TextView)view.findViewById(R.id.history_no_pay);
        final TextView historyCancel = (TextView)view.findViewById(R.id.history_cancel);
        final ListView History_list = (ListView)view.findViewById(R.id.history_list);
        final EditText Search_car_num = (EditText)view.findViewById(R.id.search_car_num);

        switch (history) {

            case "all":
                status = "all";

                historyAll.setBackground(getResources().getDrawable(R.drawable.nav_bg_on));
                historyAll.setTextColor(getResources().getColor(R.color.nav_txt_on));

                break;

            case "in":
                status = "in";

                historyIn.setBackground(getResources().getDrawable(R.drawable.nav_bg_on));
                historyIn.setTextColor(getResources().getColor(R.color.nav_txt_on));

                break;

            case "out":
                status = "out";

                historyOut.setBackground(getResources().getDrawable(R.drawable.nav_bg_on));
                historyOut.setTextColor(getResources().getColor(R.color.nav_txt_on));

                break;

            case "no_pay":
                status = "no_pay";

                historyNoPay.setBackground(getResources().getDrawable(R.drawable.nav_bg_on));
                historyNoPay.setTextColor(getResources().getColor(R.color.nav_txt_on));

                break;

            case "cancel":
                status = "cancel";

                historyCancel.setBackground(getResources().getDrawable(R.drawable.nav_bg_on));
                historyCancel.setTextColor(getResources().getColor(R.color.nav_txt_on));

                break;
        }

        car_num = "";

        final List<Map<String, Object>> list = garageService.getResult(status, car_num);
        final HistoryViewAdapter adapter = new HistoryViewAdapter(getActivity(), R.layout.history_item, list);
        History_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        History_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view1, int position, long id) {

                final int idx = (int) list.get(position).get("idx");

                TextView result_charge = (TextView) view1.findViewById(R.id.total_amount);
                int ResultCharge = 0;
                if (result_charge.getText().equals("월차")) {
                    ResultCharge = 0;
                } else {
                    ResultCharge = Integer.parseInt(result_charge.getText().toString());
                }

                Bundle args = new Bundle();
                args.putString("status", history);
                args.putInt("idx", idx);
                args.putInt("result_charge", ResultCharge);
                Garage_View garage_view = new Garage_View();
                garage_view.setArguments(args);
                garage_view.setCancelable(false);
                garage_view.show(getFragmentManager(), "garage_view");
            }
        });

        // EidtText가 눌릴때마다 감지하는 부분
        TextWatcher textWatcher = (new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Text가 바뀌기 전 동작할 코드
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                car_num = s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Text가 바뀌고 동작할 코드
                final List<Map<String, Object>> list = garageService.getResult(status, car_num);
                final HistoryViewAdapter adapter = new HistoryViewAdapter(getActivity(), R.layout.history_item, list);
                History_list.getAdapter();
                adapter.notifyDataSetChanged();
                History_list.setAdapter(adapter);

                History_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view1, int position, long id) {

                        final int idx = (int) list.get(position).get("idx");

                        TextView result_charge = (TextView) view1.findViewById(R.id.total_amount);
                        int ResultCharge = 0;
                        if (result_charge.getText().equals("월차")) {
                            ResultCharge = 0;
                        } else {
                            ResultCharge = Integer.parseInt(result_charge.getText().toString());
                        }

                        Bundle args = new Bundle();
                        args.putString("status", history);
                        args.putInt("idx", idx);
                        args.putInt("result_charge", ResultCharge);
                        Garage_View garage_view = new Garage_View();
                        garage_view.setArguments(args);
                        garage_view.setCancelable(false);
                        garage_view.show(getFragmentManager(), "garage_view");
                    }
                });
            }
        });
        Search_car_num.addTextChangedListener(textWatcher);

        // Button case 처리
        View.OnClickListener clickListener = new View.OnClickListener() {

            private FragmentManager fm = getFragmentManager();

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.history_all:

                        history = "all";
                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(history)).commit();
                        break;

                    case R.id.history_in:

                        history = "in";
                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(history)).commit();
                        break;

                    case R.id.history_out:

                        history = "out";
                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(history)).commit();
                        break;

                    case R.id.history_no_pay:

                        history = "no_pay";
                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(history)).commit();
                        break;

                    case R.id.history_cancel:

                        history = "cancel";
                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment(history)).commit();
                        break;

                }
            }
        };

        historyAll.setOnClickListener(clickListener);
        historyIn.setOnClickListener(clickListener);
        historyOut.setOnClickListener(clickListener);
        historyNoPay.setOnClickListener(clickListener);
        historyCancel.setOnClickListener(clickListener);

        return view;

    }
}