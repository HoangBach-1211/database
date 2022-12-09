package com.thanhnguyen.smartorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class QLmonAnBepAdapter extends BaseAdapter {

    Context context;
    int layout;
    List<QLMonAnBep> QLmonAnBep;
    public QLmonAnBepAdapter(Context context, int layout, List<QLMonAnBep> arrayList) {
        this.context = context;
        this.layout = layout;
        this.QLmonAnBep = arrayList;
    }
    public class ViewHolder{

        TextView txtSoLuong;
        TextView txtTenmon;
    }
    @Override
    public int getCount() {
        return QLmonAnBep.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view= inflater.inflate(layout, null);
        TextView twtenmon= view.findViewById(R.id.tenmonnb);
        twtenmon.setText(QLmonAnBep.get(i).tenmon);
        TextView twsl= view.findViewById(R.id.soluongnb);
        twsl.setText(QLmonAnBep.get(i).soluong);


        return view;
    }
}
