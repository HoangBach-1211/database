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

import com.squareup.picasso.Picasso;
import com.thanhnguyen.smartorder.DTO.LoaiHinhDTO;
import com.thanhnguyen.smartorder.R;

import java.util.ArrayList;


public class Custom_Adapter_LH extends ArrayAdapter<LoaiHinhDTO> {


    public Custom_Adapter_LH(@NonNull Context context, ArrayList<LoaiHinhDTO> customList) {
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
        LoaiHinhDTO items = getItem(position);
        ImageView spinnerImage = convertView.findViewById(R.id.ivCustomSpinner);
        TextView spinnerName = convertView.findViewById(R.id.tvCustomSpinner);
        if (spinnerImage==null || spinnerImage.equals(""))
        {
            spinnerImage.setImageResource(R.drawable.apple);
        }
        String hha= items.getHinhLoai();
        if (items != null) {
            spinnerName.setText(items.getTenloai());
            loadimageinternet(hha,spinnerImage);
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