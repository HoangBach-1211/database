package com.thanhnguyen.smartorder.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thanhnguyen.smartorder.DTO.ThanhToanDTO;
import com.thanhnguyen.smartorder.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterHienThiThanhToan extends BaseAdapter  {

    Context context;
    int layout;
    List<ThanhToanDTO> thanhToanDTOs;
    ViewHolderThanhToan viewHolderThanhToan;



    public AdapterHienThiThanhToan(Context context, int layout, List<ThanhToanDTO> thanhToanDTOs){
        this.context = context;
        this.layout = layout;
        this.thanhToanDTOs = thanhToanDTOs;

    }

    @Override
    public int getCount() {
        return thanhToanDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        return thanhToanDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolderThanhToan{
        TextView txtTenMonAn,txtSoLuong,txtGiaTien;
        ImageView img;
        Button up, down, del;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            viewHolderThanhToan = new ViewHolderThanhToan();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,parent,false);

            viewHolderThanhToan.txtTenMonAn = (TextView) view.findViewById(R.id.txtTenMonAnThanhToan);
            viewHolderThanhToan.txtGiaTien = (TextView) view.findViewById(R.id.txtGiaTienThanhToan);
            viewHolderThanhToan.txtSoLuong = (TextView) view.findViewById(R.id.txtSoLuongThanhToan);
            viewHolderThanhToan.img = (ImageView) view.findViewById(R.id.hinhloadve);



            view.setTag(viewHolderThanhToan);
        }else{
            viewHolderThanhToan = (ViewHolderThanhToan) view.getTag();
        }

        ThanhToanDTO thanhToanDTO = thanhToanDTOs.get(position);

        viewHolderThanhToan.txtTenMonAn.setText(thanhToanDTO.getTenMonAn());
        viewHolderThanhToan.txtSoLuong.setText(String.valueOf(thanhToanDTO.getSoLuong()));

        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        long longNumber = Long.valueOf(thanhToanDTO.getGiaTien());
        String str1 = en.format(longNumber);
        viewHolderThanhToan.txtGiaTien.setText( str1 + " VND");
        try {
            loadimageinternet(thanhToanDTO.getHinhAnh(), viewHolderThanhToan.img);
        }
        catch (Exception ex)
        {

        }

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
