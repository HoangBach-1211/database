package com.thanhnguyen.smartorder.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thanhnguyen.smartorder.DTO.ThanhToanDTO;
import com.thanhnguyen.smartorder.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterInHoaDon extends BaseAdapter  {

    Context context;
    int layout;
    List<ThanhToanDTO> thanhToanDTOs;
    ViewHolderThanhToan viewHolderThanhToan;



    public AdapterInHoaDon(Context context, int layout, List<ThanhToanDTO> thanhToanDTOs){
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
            view.setTag(viewHolderThanhToan);
        }else{
            viewHolderThanhToan = (ViewHolderThanhToan) view.getTag();
        }

        ThanhToanDTO thanhToanDTO = thanhToanDTOs.get(position);

        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        long longNumber = Long.valueOf(thanhToanDTO.getGiaTien());
        String str1 = en.format(longNumber);
        viewHolderThanhToan.txtTenMonAn.setText(thanhToanDTO.getTenMonAn());
        viewHolderThanhToan.txtSoLuong.setText(" x "+(thanhToanDTO.getSoLuong()));
        viewHolderThanhToan.txtGiaTien.setText(str1 + "");


        return view;
    }

}
