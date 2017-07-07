package com.example.macarrow.xPos.fragment.Cooper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.macarrow.xPos.R;
import com.example.macarrow.xPos.Services.Cooper_Services;
import com.example.macarrow.xPos.fragment.Cooper_Fragment;

import java.util.Map;

public class Cooper_Add extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        final String status = mArgs.getString("status");
        final int idx = mArgs.getInt("idx");

        final FragmentManager fm = getFragmentManager();
        final Cooper_Services cooper_services = new Cooper_Services(getActivity(), "cooper.db", null, 1);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.cooper_add, null);
        final TextView Title_cooper = (TextView) view.findViewById(R.id.title_cooper);
        final EditText Coop_title = (EditText) view.findViewById(R.id.coop_title);
        final EditText Coop_tel = (EditText) view.findViewById(R.id.coop_tel);
        final EditText Coop_address = (EditText) view.findViewById(R.id.coop_address);
        final EditText Coop_user_name = (EditText) view.findViewById(R.id.coop_user_name);
        final EditText Minute_max = (EditText) view.findViewById(R.id.minute_max);
        final Button Add_cooper = (Button) view.findViewById(R.id.add_cooper);
        final Button Close_cooper = (Button) view.findViewById(R.id.close_cooper);
        final LinearLayout Is_end_lay = (LinearLayout) view.findViewById(R.id.is_end_lay);
        final Switch Is_end = (Switch) view.findViewById(R.id.is_end);
        final Button Update_cooper = (Button) view.findViewById(R.id.update_cooper);
        final Button Delete_cooper = (Button) view.findViewById(R.id.delete_cooper);

        builder.setView(view);

        if (status.equals("new")) {

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
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String coop_title = Coop_title.getText().toString();
                            String coop_tel = Coop_tel.getText().toString();
                            String coop_address = Coop_address.getText().toString();
                            String coop_user_name = Coop_user_name.getText().toString();
                            int minute_max = Integer.parseInt(Minute_max.getText().toString());
                            cooper_services.insert(coop_title, coop_tel, coop_address, coop_user_name, minute_max);
                            fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment("cooper")).commit();
                            dismiss();
                            break;

                        case R.id.close_cooper:

                            fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment("cooper")).commit();
                            dismiss();
                            break;
                    }
                }
            };
            Add_cooper.setOnClickListener(clickListener);
            Close_cooper.setOnClickListener(clickListener);
        }

        if (status.equals("modify")) {

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
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String coop_title = Coop_title.getText().toString();
                            String coop_tel = Coop_tel.getText().toString();
                            String coop_address = Coop_address.getText().toString();
                            String coop_user_name = Coop_user_name.getText().toString();
                            int minute_max = Integer.parseInt(Minute_max.getText().toString());
                            String is_end = "";
                            if (Is_end.getText().equals("활성")) {
                                is_end = "N";
                            } else if (Is_end.getText().equals("종료")) {
                                is_end = "Y";
                            }

                            cooper_services.update(coop_title, coop_tel, coop_address, coop_user_name, minute_max, is_end, idx);
                            fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment("cooper")).commit();
                            dismiss();
                            break;

                        case R.id.delete_cooper:

                            cooper_services.delete(idx);
                            fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment("cooper")).commit();
                            dismiss();
                            break;

                        case R.id.close_cooper:

                            fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment("cooper")).commit();
                            dismiss();
                            break;
                    }
                }
            };
            Update_cooper.setOnClickListener(clickListener);
            Delete_cooper.setOnClickListener(clickListener);
            Close_cooper.setOnClickListener(clickListener);
        }

        return builder.create();
    }
    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(835, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}