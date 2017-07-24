package com.example.macarrow.xPos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.macarrow.xPos.R;
import java.util.List;
import java.util.Map;

public class CooperViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public CooperViewAdapter(Context context, int layout, List<Map<String, Object>> list) {

        this.context = context;
        this.layout = layout;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CooperView_Item item = null;

        if(convertView == null) {

            convertView = inflater.inflate(layout, null);
            TextView Coop_title = (TextView) convertView.findViewById(R.id.coop_title);
            TextView Coop_tel = (TextView) convertView.findViewById(R.id.coop_tel);
            TextView Coop_user_name = (TextView) convertView.findViewById(R.id.coop_user_name);
            TextView Minute_max = (TextView) convertView.findViewById(R.id.minute_max);
            TextView Is_end = (TextView) convertView.findViewById(R.id.is_end);

            item = new CooperView_Item();
            item.coopTitle = Coop_title;
            item.coopTel = Coop_tel;
            item.coopUserName = Coop_user_name;
            item.minuteMax = Minute_max;
            item.isEnd = Is_end;
            convertView.setTag(item);

        } else {

            item = (CooperView_Item) convertView.getTag();

        }
        item.coopTitle.setText((String) list.get(position).get("coop_title"));
        item.coopTel.setText((String) list.get(position).get("coop_tel"));
        item.coopUserName.setText((String) list.get(position).get("coop_user_name"));
        item.minuteMax.setText((Integer) list.get(position).get("minute_max") + "분");
        item.isEnd.setText((String) list.get(position).get("is_end"));

        String isEnd = (String) list.get(position).get("is_end");

        if (isEnd.equals("Y")) {
            item.isEnd.setText("중단");
        } if (isEnd.equals("N")) {
            item.isEnd.setText("활성");
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}

class CooperView_Item {

    TextView coopTitle;
    TextView coopTel;
    TextView coopUserName;
    TextView minuteMax;
    TextView isEnd;

}