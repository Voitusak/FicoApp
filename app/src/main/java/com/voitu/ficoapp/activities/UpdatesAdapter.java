package com.voitu.ficoapp.activities;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.voitu.ficoapp.R;
import com.voitu.ficoapp.controller.Common;
import com.voitu.ficoapp.model.UpdateInfo;
import com.voitu.ficoapp.model.User;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UpdatesAdapter extends BaseAdapter {
    private static final String DEFAULT_USER = "EA SPORTS";
    Context context;
    List<UpdateInfo> data;
    private static LayoutInflater inflater = null;
    private DecimalFormat formatter = new DecimalFormat("#.####");
    private Map<String,String> names;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public UpdatesAdapter(Context context, List<UpdateInfo> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.names = new HashMap<>();

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
            vi = inflater.inflate(R.layout.update_card, null);
        TextView dateView = vi.findViewById(R.id.dateView);
        TextView valueView = vi.findViewById(R.id.pointsView);
        TextView user = vi.findViewById(R.id.madeBy);
        boolean positive = true;
        try {
            if (data.get(position).getPoints() < 0) {
                valueView.setTextColor(context.getResources().getColor(R.color.red));
                positive = false;
            } else {
                valueView.setTextColor(context.getResources().getColor(R.color.green));
            }

            dateView.setText(DateFormat.getDateInstance().format(data.get(position).getDate()));
            valueView.setText((positive ? "+" : "").concat(
                    Common.convertNumber(data.get(position).getPoints())));
            String us = this.names.get(data.get(position).getActionUser());
            user.setText(us==null || us.isEmpty() ? DEFAULT_USER : (Common.formatString(us)));
        } catch (Exception e){
            // ingored
        }
        return vi;

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setNames(List<User> users){
        this.names = users.stream()
                .collect(Collectors.toMap(
                        User::getId, User::getAlias));
    }
}
