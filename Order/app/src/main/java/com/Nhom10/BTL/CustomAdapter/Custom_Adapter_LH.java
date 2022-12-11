package com.Nhom10.BTL.CustomAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.Nhom10.BTL.DTO.LoaiMonAnDTO;
import com.Nhom10.BTL.R;

import java.util.ArrayList;


public class Custom_Adapter_LH extends ArrayAdapter<LoaiMonAnDTO> {

    public Custom_Adapter_LH(@NonNull Context context, ArrayList<LoaiMonAnDTO> customList) {
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_layout, parent, false);
        }
        LoaiMonAnDTO items = getItem(position);
        ImageView spinnerImage = convertView.findViewById(R.id.ivCustomSpinner);
        TextView spinnerName = convertView.findViewById(R.id.tvCustomSpinner);
        if (spinnerImage==null || spinnerImage.equals(""))
        {
            spinnerImage.setImageResource(R.drawable.apple);
        }

        {
            spinnerName.setText(items.getTenLoai());
            //loadimageinternet(hha,spinnerImage);
        }
        return convertView;
    }
    private void loadimageinternet(String url ,ImageView x){
        Picasso.get().load(url).placeholder(R.drawable.bin)
                .fit()
                .into(x, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }
}