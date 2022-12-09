package com.thanhnguyen.smartorder.TimTro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thanhnguyen.smartorder.DTO.HuyenDTO;
import com.thanhnguyen.smartorder.R;

import java.util.ArrayList;


public class Custom_Adapter_Huyen extends ArrayAdapter<HuyenDTO> {


    public Custom_Adapter_Huyen(@NonNull Context context, ArrayList<HuyenDTO> customList) {
        super(context, 0, customList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return customView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return customView(position, convertView, parent);
    }

    public View customView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_layout_huyen, parent, false);
        }
        HuyenDTO items = getItem(position);
        ImageView spinnerImage = convertView.findViewById(R.id.ivCustomSpinner);
        TextView spinnerName = convertView.findViewById(R.id.tvCustomSpinner);
        if (spinnerImage==null || spinnerImage.equals(""))
        {
            spinnerImage.setImageResource(R.drawable.apple);
        }
        if (items != null) {
            spinnerName.setText(items.getTenhuyen());
        }
        return convertView;
    }
}