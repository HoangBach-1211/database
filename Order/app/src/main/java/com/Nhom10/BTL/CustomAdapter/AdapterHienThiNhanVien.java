package com.Nhom10.BTL.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Nhom10.BTL.DTO.NhanVienDTO;
import com.Nhom10.BTL.R;

import java.util.List;

public class AdapterHienThiNhanVien extends BaseAdapter {

    Context context;
    int layout;
    List<NhanVienDTO> nhanVienDTOList;
    ViewHolderNhanVien viewHolderNhanVien;

    public AdapterHienThiNhanVien(Context context, int layout,List<NhanVienDTO> nhanVienDTOList){
        this.context = context;
        this.layout = layout;
        this.nhanVienDTOList = nhanVienDTOList;

    }

    @Override
    public int getCount() {
        return nhanVienDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return nhanVienDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return nhanVienDTOList.get(position).getMANV();
    }

    public class ViewHolderNhanVien{
        ImageView imHinhNhanVien;
        TextView txtTenNhanVien,txtCMND;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            viewHolderNhanVien = new ViewHolderNhanVien();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,parent,false);

            viewHolderNhanVien.imHinhNhanVien = (ImageView) view.findViewById(R.id.imHinhNhanVien);
            viewHolderNhanVien.txtTenNhanVien = (TextView) view.findViewById(R.id.txtTenNhanVien);
            viewHolderNhanVien.txtCMND = (TextView) view.findViewById(R.id.txtSDT);

            view.setTag(viewHolderNhanVien);
        }else{
            viewHolderNhanVien = (ViewHolderNhanVien) view.getTag();
        }

        NhanVienDTO nhanVienDTO = nhanVienDTOList.get(position);

        viewHolderNhanVien.txtTenNhanVien.setText(nhanVienDTO.getTENDN());
        viewHolderNhanVien.txtCMND.setText(String.valueOf(nhanVienDTO.getSDT()));
        return view;
    }
}
