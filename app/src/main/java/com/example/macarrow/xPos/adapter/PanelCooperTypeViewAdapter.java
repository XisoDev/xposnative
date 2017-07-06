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

public class PanelCooperTypeViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public PanelCooperTypeViewAdapter(Context context, int layout, List<Map<String, Object>> list) {

        this.context = context;
        this.layout = layout;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PanelCooperTypeView_Item item = null;

        if(convertView == null) {

            convertView = inflater.inflate(layout, null);
            TextView Coop_title = (TextView) convertView.findViewById(R.id.coop_title);
            TextView Minute_max = (TextView) convertView.findViewById(R.id.minute_max);
            TextView Minute_unit = (TextView) convertView.findViewById(R.id.minute_unit);
            TextView Amount_unit = (TextView) convertView.findViewById(R.id.amount_unit);

            item = new PanelCooperTypeView_Item();
            item.coopTitle = Coop_title;
            item.minuteMax = Minute_max;
            item.minuteUnit = Minute_unit;
            item.amountUnit = Amount_unit;
            convertView.setTag(item);

        } else {

            item = (PanelCooperTypeView_Item) convertView.getTag();

        }

        item.coopTitle.setText((String) list.get(position).get("coop_title"));
        item.minuteMax.setText((Integer) list.get(position).get("minute_max") + "");
        item.minuteUnit.setText((Integer) list.get(position).get("minute_unit") + "");
        item.amountUnit.setText((Integer) list.get(position).get("amount_unit") + "");

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

class PanelCooperTypeView_Item {

    TextView coopTitle;
    TextView minuteMax;
    TextView minuteUnit;
    TextView amountUnit;

}