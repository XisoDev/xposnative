package com.example.macarrow.xPos.fragment.Month;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Month_Service;
import com.example.macarrow.xPos.adapter.MonthinNoutViewAdapter;
import java.util.List;
import java.util.Map;

public class Month_inNout extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle mArgs = getArguments();
        final int year = mArgs.getInt("year");
        final int month = mArgs.getInt("month");
        final int day = mArgs.getInt("day");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final Month_Service month_service = new Month_Service(getActivity(), "month.db", null, 1);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.month_innout, null);
        final ListView Month_inNouts = (ListView) view.findViewById(R.id.month_inNout);

        final List<Map<String, Object>> list = month_service.calMonth(year, month, day);
        MonthinNoutViewAdapter adapter = new MonthinNoutViewAdapter(getActivity(), R.layout.month_innout_item, list);
        Month_inNouts.setAdapter(adapter);

        builder.setNegativeButton("닫기", null);
        builder.setView(view);

        return builder.create();
    }
    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(835, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}