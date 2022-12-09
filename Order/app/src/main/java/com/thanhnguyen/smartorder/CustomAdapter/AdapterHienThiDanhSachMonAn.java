package com.thanhnguyen.smartorder.CustomAdapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thanhnguyen.smartorder.DTO.MonAnDTO;
import com.thanhnguyen.smartorder.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterHienThiDanhSachMonAn extends BaseAdapter{

    Context context;
    int layout;
    List<MonAnDTO> monAnDTOList;
    ViewHolderHienThiDanhSachMonAn viewHolderHienThiDanhSachMonAn;

    public AdapterHienThiDanhSachMonAn(Context context, int layout, List<MonAnDTO> monAnDTOList){
        this.context = context;
        this.layout = layout;
        this.monAnDTOList = monAnDTOList;

    }

    @Override
    public int getCount() {
        return monAnDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return monAnDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return monAnDTOList.get(position).getMaMonAn();
    }

    public class ViewHolderHienThiDanhSachMonAn{
        ImageView imHinhMonAn;
        TextView txtTenMonAn, txtGiaTien;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolderHienThiDanhSachMonAn = new ViewHolderHienThiDanhSachMonAn();
            view = inflater.inflate(layout, parent, false);

            viewHolderHienThiDanhSachMonAn.imHinhMonAn = (ImageView) view.findViewById(R.id.imHienThiDSMonAn);
            viewHolderHienThiDanhSachMonAn.txtTenMonAn = (TextView) view.findViewById(R.id.txtTenDSMonAn);
            viewHolderHienThiDanhSachMonAn.txtGiaTien = (TextView) view.findViewById(R.id.txtGiaTienDSMonAn);

            view.setTag(viewHolderHienThiDanhSachMonAn);
        }else {
            viewHolderHienThiDanhSachMonAn = (ViewHolderHienThiDanhSachMonAn) view.getTag();
        }

        MonAnDTO monAnDTO = monAnDTOList.get(position);
        String hinhanh = monAnDTO.getHinhAnh().toString();
        if(hinhanh == null || hinhanh.equals("")){
            viewHolderHienThiDanhSachMonAn.imHinhMonAn.setImageResource(R.drawable.food);
        }else{
            Uri uri = Uri.parse(hinhanh);
            Log.d("dsma",hinhanh.toString());
            loadimageinternet(hinhanh,viewHolderHienThiDanhSachMonAn.imHinhMonAn);
            //Toast.makeText(context, hinhanh, Toast.LENGTH_SHORT).show();
            //viewHolderHienThiDanhSachMonAn.imHinhMonAn.setImageURI(uri);
           // Toast.makeText(context, uri.toString(),
            // .LENGTH_SHORT).show();
        }

        viewHolderHienThiDanhSachMonAn.txtTenMonAn.setText(monAnDTO.getTenMonAn());
        NumberFormat currentLocale = NumberFormat.getInstance();
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        String gia=monAnDTO.getGiaTien();
        long longNumber = Long.parseLong(gia);
        String str1 = en.format(longNumber);
        viewHolderHienThiDanhSachMonAn.txtGiaTien.setText(context.getResources().getString(R.string.gia) + " " +str1);

        return view;
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
