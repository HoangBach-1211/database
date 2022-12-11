package com.Nhom10.BTL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class lsgoimonAdapter extends BaseAdapter {

    Context mycontext;
    int mylayout;
    List<lsgoimon>  arraylsgoimon;
    public lsgoimonAdapter(Context context, int layout, List<lsgoimon> listLsgoimon)
    {
        mycontext=context;
        mylayout= layout;
        arraylsgoimon=listLsgoimon;
    }
    @Override
    public int getCount() {
        return arraylsgoimon.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(mylayout, null);
        TextView tenban= convertView.findViewById(R.id.txttenbanls);
        tenban.setText(arraylsgoimon.get(position).getTenbanls());
        TextView ngaygoi= convertView.findViewById(R.id.txtngaygoils);
        ngaygoi.setText("Thời gian: " + arraylsgoimon.get(position).getNgaygoils());
        //TextView nhanvien= convertView.findViewById(R.id.txtnhanvienls);
        //nhanvien.setText(arraylsgoimon.get(position).getNhanvienls());
        TextView tongtien= convertView.findViewById(R.id.txttongtienls);
        NumberFormat currentLocale = NumberFormat.getInstance();
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        long longNumber = Long.parseLong(arraylsgoimon.get(position).getTongtienls());
        String str1 = en.format(longNumber);

        tongtien.setText("Tổng tiền: " +str1+ " VND");
        return convertView;
    }
}
