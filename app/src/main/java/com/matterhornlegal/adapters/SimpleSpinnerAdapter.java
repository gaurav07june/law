package com.matterhornlegal.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.matterhornlegal.R;
import com.matterhornlegal.databinding.SimpleSpinnerRowBinding;


public class SimpleSpinnerAdapter extends ArrayAdapter<String> {
    private  Context context;
    private String[] objects;
    public SimpleSpinnerAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        SimpleSpinnerRowBinding binding;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.simple_spinner_row, parent, false);
        binding = DataBindingUtil.bind(row);
        binding.txtSpinnerText.setText(objects[position]);
        if (position == objects.length - 1){
            binding.viewSeparator.setVisibility(View.GONE);
        }else{
            binding.viewSeparator.setVisibility(View.VISIBLE);
        }
        return row;
    }

}
