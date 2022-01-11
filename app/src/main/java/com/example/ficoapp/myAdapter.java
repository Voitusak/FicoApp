package com.example.ficoapp;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class myAdapter extends BaseAdapter {
    Context context;
    List<Pair<String, String>> data;
    private static LayoutInflater inflater = null;
    private DecimalFormat formatter = new DecimalFormat("#.####");

    public myAdapter(Context context, List<Pair<String,String>> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.card, null);
        TextView dateView = (TextView) vi.findViewById(R.id.dateView);
        TextView valueView = (TextView) vi.findViewById(R.id.pointsView);
        boolean positive = true;
        try {
            if (Long.parseLong(data.get(position).second) < 0) {
                valueView.setTextColor(context.getResources().getColor(R.color.red));
                positive = false;
            } else {
                valueView.setTextColor(context.getResources().getColor(R.color.green));
            }

            dateView.setText(data.get(position).first);
            valueView.setText((positive ? "+" : "") +
                    convertNumber(Long.parseLong(data.get(position).second)));
        } catch (Exception e){
            // ingored
        }
        return vi;

    }

    private String convertNumber(long number) {

        if (Math.abs(number) > 999 && Math.abs(number) < 1_000_000){
            return formatter.format( (double)number/1000) + "K";
        }
        if (Math.abs(number) >= 1_000_000){
            return formatter.format((double)number/1_000_000) + "M";
        }
        return String.valueOf(number);
    }
}
