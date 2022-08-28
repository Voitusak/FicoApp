package com.voitu.ficoapp.activities;

import android.content.Context;
import android.os.Build;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.voitu.ficoapp.R;
import com.voitu.ficoapp.controller.Common;
import com.voitu.ficoapp.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RanksAdapter extends BaseAdapter {
    private static final String DEFAULT_USER = "EA SPORTS";
    Context context;
    private Map<String,String> names;
    List<Pair<String, Integer>> winnersIds;
    private static LayoutInflater inflater = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public RanksAdapter(Context context,List<Pair<String, Integer>> ids) {
        this.context = context;
        this.names = new HashMap<>();
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.winnersIds = ids;

    }

    @Override
    public int getCount() {
        return winnersIds.size();
    }

    @Override
    public Object getItem(int position) {
        
        return winnersIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.rank_card, null);
        TextView valueView = vi.findViewById(R.id.rankPoints);
        TextView user = vi.findViewById(R.id.rankCardUser);
        try {
            if (position == 2) {
                valueView.setTextColor(context.getResources().getColor(R.color.red));
            } else if(position == 1){
                valueView.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
            } else if(position == 0){
                valueView.setTextColor(context.getResources().getColor(R.color.green));
            }

            valueView.setText(String.valueOf(winnersIds.get(position).second));
            String us = this.names.get(winnersIds.get(position).first);
            user.setText(us==null || us.isEmpty() ? DEFAULT_USER : (Common.formatString(us)));
        } catch (Exception e){
            System.out.println(e);
        }
        return vi;

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setNames(List<User> users){
        this.names = users.stream()
                .collect(Collectors.toMap(
                        User::getId, User::getAlias));
    }
    public void setTag(List<Pair<String,Integer>> v){
        this.winnersIds = v;
    }
}
