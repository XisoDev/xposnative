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

public class CarTypeViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public CarTypeViewAdapter(Context context, int layout, List<Map<String, Object>> list) {

        this.context = context;
        this.layout = layout;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CarTypeView_Item item = null;

        if(convertView == null) {
            convertView = inflater.inflate(layout, null);
            TextView Car_type_title = (TextView) convertView.findViewById(R.id.car_type_title);
            TextView Minute_free = (TextView) convertView.findViewById(R.id.minute_free);
            TextView Basic_amount = (TextView) convertView.findViewById(R.id.basic_amount);
            TextView Basic_minute = (TextView) convertView.findViewById(R.id.basic_minute);
            TextView Amount_unit = (TextView) convertView.findViewById(R.id.amount_unit);
            TextView Minute_unit = (TextView) convertView.findViewById(R.id.minute_unit);

            item = new CarTypeView_Item();
            item.carTypeTitle = Car_type_title;
            item.minuteFree = Minute_free;
            item.basicAmount = Basic_amount;
            item.basicMinute = Basic_minute;
            item.amountUnit = Amount_unit;
            item.minuteUnit = Minute_unit;

            convertView.setTag(item);

        } else {

            item = (CarTypeView_Item) convertView.getTag();

        }

        item.carTypeTitle.setText((String) list.get(position).get("car_type_title"));
        item.minuteFree.setText((int) list.get(position).get("minute_free")+"분");
        int basic_amount = (int) list.get(position).get("basic_amount");
        item.basicAmount.setText(String.format("%,d원",basic_amount));
        item.basicMinute.setText((int) list.get(position).get("basic_minute")+"분");
        int amount_unit = (int) list.get(position).get("amount_unit");
        item.amountUnit.setText(String.format("%,d원",amount_unit));
        item.minuteUnit.setText((int) list.get(position).get("minute_unit")+"분");

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

class CarTypeView_Item {

    TextView carTypeTitle;
    TextView minuteFree;
    TextView basicAmount;
    TextView basicMinute;
    TextView amountUnit;
    TextView minuteUnit;

}