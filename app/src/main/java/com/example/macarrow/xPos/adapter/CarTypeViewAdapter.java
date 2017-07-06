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
//            TextView Is_daycar = (TextView) convertView.findViewById(R.id.is_daycar);

            item = new CarTypeView_Item();
            item.carTypeTitle = Car_type_title;
//            item.isDaycar = Is_daycar;

            convertView.setTag(item);

        } else {

            item = (CarTypeView_Item) convertView.getTag();

        }

        item.carTypeTitle.setText((String) list.get(position).get("car_type_title"));
//        item.isDaycar.setText((String) list.get(position).get("is_daycar"));

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
//    TextView isDaycar;

}