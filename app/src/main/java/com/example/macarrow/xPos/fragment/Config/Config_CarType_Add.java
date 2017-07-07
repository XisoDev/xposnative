package com.example.macarrow.xPos.fragment.Config;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.CarType_Services;

public class Config_CarType_Add extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        final int idx = mArgs.getInt("idx");
        final int totalAmount = mArgs.getInt("total_amount");
        final long endDate = mArgs.getLong("end_date");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.config_addcartype, null);

        final CarType_Services carType_services = new CarType_Services(getActivity(), "car_type.db", null, 1);

        return super.onCreateDialog(savedInstanceState);
    }
}