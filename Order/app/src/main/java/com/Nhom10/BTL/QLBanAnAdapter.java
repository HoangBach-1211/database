package com.Nhom10.BTL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class QLBanAnAdapter extends BaseAdapter
{


    Context context;
    int layout;
    List<QLBanAn> QLBanAnx;

    public QLBanAnAdapter(Context context, int layout, List<QLBanAn> QLBanAnx) {
        this.context = context;
        this.layout = layout;
        this.QLBanAnx = QLBanAnx;
    }

    @Override
    public int getCount() {

        return QLBanAnx.size();
    }

    public class ViewHolder{

        TextView txtTenBanAn;
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
        ViewHolder holder;
        if (view==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder.txtTenBanAn = (TextView) view.findViewById(R.id.TenBan);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        QLBanAn x = QLBanAnx.get(i);
        holder.txtTenBanAn.setText(x.getTenban());
        return view;
    }
}
