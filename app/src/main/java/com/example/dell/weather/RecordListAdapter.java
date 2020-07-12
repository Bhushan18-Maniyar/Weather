package com.example.dell.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BBM on 31-05-2020.
 */

public class RecordListAdapter extends ArrayAdapter<Model> {

    private Context context;
    private List<Model> Model;
    public RecordListAdapter(Context context, List<Model> model) {
        super(context, R.layout.rowofcity,model);
        this.context = context;
        this.Model = model;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowofcity,null,true);

        TextView city = view.findViewById(R.id.cityname);
        TextView forcast = view.findViewById(R.id.forcast);
        TextView temp = view.findViewById(R.id.temprature);

        city.setText(Model.get(position).getCityname());
        forcast.setText(Model.get(position).getForcast());
        temp.setText(Model.get(position).getTemp()+"°c");

        return view;
    }
}
