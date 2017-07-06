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

public class PanelDayCarTypeViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public PanelDayCarTypeViewAdapter(Context context, int layout, List<Map<String, Object>> list) {

        this.context = context;
        this.layout = layout;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PanelDayCarTypeView_Item item = null;

        if(convertView == null) {

            convertView = inflater.inflate(layout, null);
            TextView Car_type_title = (TextView) convertView.findViewById(R.id.car_type_title);
            TextView Basic_minute = (TextView) convertView.findViewById(R.id.basic_minute);
            TextView Basic_amount = (TextView) convertView.findViewById(R.id.basic_amount);
            TextView Minute_unit = (TextView) convertView.findViewById(R.id.minute_unit);
            TextView Amount_unit = (TextView) convertView.findViewById(R.id.amount_unit);

            item = new PanelDayCarTypeView_Item();
            item.carTypeTitle = Car_type_title;
            item.basicMinute = Basic_minute;
            item.basicAmount = Basic_amount;
            item.minuteUnit = Minute_unit;
            item.amountUnit = Amount_unit;
            convertView.setTag(item);

        } else {

            item = (PanelDayCarTypeView_Item) convertView.getTag();

        }

        item.carTypeTitle.setText((String) list.get(position).get("car_type_title"));
        item.basicMinute.setText((Integer) list.get(position).get("basic_minute") + "");
        item.basicAmount.setText((Integer) list.get(position).get("basic_amount") + "");
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

class PanelDayCarTypeView_Item {

    TextView carTypeTitle;
    TextView basicMinute;
    TextView basicAmount;
    TextView minuteUnit;
    TextView amountUnit;

}