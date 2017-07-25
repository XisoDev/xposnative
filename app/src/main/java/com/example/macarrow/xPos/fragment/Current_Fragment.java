package com.example.macarrow.xPos.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.adapter.CurrentViewAdapter;
import com.example.macarrow.xPos.fragment.Garage.Garage_View;
import java.util.List;
import java.util.Map;

public class Current_Fragment extends Fragment {

    public Current_Fragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.current, container, false);

        final Garage_Service garageService = new Garage_Service(getActivity(), "garage.db", null, 1);
        final GridView currentView = (GridView) view.findViewById(R.id.currentView);

        final List<Map<String, Object>> list = garageService.getResult("in", "");
        final CurrentViewAdapter adapter = new CurrentViewAdapter(getActivity(), R.layout.current_item, list);
        currentView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // 당겨서 새로고침
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        currentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view1, int position, long id) {

                final int idx = (int) list.get(position).get("idx");

                TextView result_charge = (TextView) view1.findViewById(R.id.result_charge);
                int ResultCharge = 0;
                if (result_charge.getText().equals("월차")) {
                    ResultCharge = 0;
                } else if (result_charge.getText().equals("일차")) {
                    ResultCharge = (int) list.get(position).get("total_amount");
                } else {
                    ResultCharge = Integer.parseInt(result_charge.getText().toString());
                }

                Bundle args = new Bundle();
                args.putString("status", "current");
                args.putInt("idx", idx);
                args.putInt("result_charge", ResultCharge);
                Garage_View garage_view = new Garage_View();
                garage_view.setArguments(args);
                garage_view.setCancelable(false);
                garage_view.show(getFragmentManager(), "garage_view");
            }
        });
        return view;
    }
}