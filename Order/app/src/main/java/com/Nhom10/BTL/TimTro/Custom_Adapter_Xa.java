package com.Nhom10.BTL.TimTro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Nhom10.BTL.DTO.XaDTO;
import com.Nhom10.BTL.R;

import java.util.ArrayList;


public class Custom_Adapter_Xa extends ArrayAdapter<XaDTO> {


    public Custom_Adapter_Xa(@NonNull Context context, ArrayList<XaDTO> customList) {
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinnerxa, parent, false);
        }
        XaDTO items = getItem(position);
        ImageView spinnerImage = convertView.findViewById(R.id.ivCustomSpinner);
        TextView spinnerName = convertView.findViewById(R.id.tvCustomSpinner);
        if (spinnerImage==null || spinnerImage.equals(""))
        {
            spinnerImage.setImageResource(R.drawable.apple);
        }
        if (items != null) {
            spinnerName.setText(items.getTenxa());
        }
        return convertView;
    }
}