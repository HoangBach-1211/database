package com.Nhom10.BTL.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.Nhom10.BTL.DAO.BanAnDAO;
import com.Nhom10.BTL.DAO.GoiMonDAO;
import com.Nhom10.BTL.DTO.BanAnDTO;
import com.Nhom10.BTL.FragmentApp.HienThiThucDonFragment;
import com.Nhom10.BTL.R;
import com.Nhom10.BTL.ThanhToanActivity;
import com.Nhom10.BTL.config;
import com.Nhom10.BTL.home;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterHienThiBanAn extends BaseAdapter implements View.OnClickListener {

    Context context;
    int layout;
    List<BanAnDTO> banAnDTOList;
    ViewHolderBanAn viewHolderBanAn;
    BanAnDAO banAnDAO;
    String tendn;
    String tenban;
    GoiMonDAO goiMonDAO;
    FragmentManager fragmentManager;
    String urlgoimon= config.domain + "android/goimon.php";
    String urlcnhat=config.domain + "android/capnhattt.php";


    public AdapterHienThiBanAn(Context context,int layout,List<BanAnDTO> banAnDTOList){
        this.context = context;
        this.layout = layout;
        this.banAnDTOList = banAnDTOList;

        banAnDAO = new BanAnDAO(context);
        goiMonDAO = new GoiMonDAO(context);
        fragmentManager = ((home)context).getSupportFragmentManager();
    }


    @Override
    public int getCount() {
        return banAnDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return banAnDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return banAnDTOList.get(position).getMaBan();
    }



    public class ViewHolderBanAn{
        ImageView imBanAn,imGoiMon,imThanhToan,imAnButton;
        TextView txtTenBanAn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolderBanAn = new ViewHolderBanAn();
            view = inflater.inflate(R.layout.custom_layout_hienthibanan,parent,false);
            viewHolderBanAn.imBanAn = (ImageView) view.findViewById(R.id.imBanAn);
            viewHolderBanAn.imGoiMon = (ImageView) view.findViewById(R.id.imGoiMon);
            viewHolderBanAn.imThanhToan = (ImageView) view.findViewById(R.id.imThanhToan);
            viewHolderBanAn.imAnButton = (ImageView) view.findViewById(R.id.imAnButton);
            viewHolderBanAn.txtTenBanAn = (TextView) view.findViewById(R.id.txtTenBanAn);

            view.setTag(viewHolderBanAn);
        }else{
            viewHolderBanAn = (ViewHolderBanAn) view.getTag();
        }

        if(banAnDTOList.get(position).isDuocChon()){
           HienThiButton();
        }else{
            AnButton(false);
        }

        BanAnDTO banAnDTO = banAnDTOList.get(position);
        tenban=banAnDTOList.get(position).getTenBan();
        String kttinhtrang = String.valueOf(banAnDTOList.get(position).isDuocChon());
        if(kttinhtrang.equals("true")){
            viewHolderBanAn.imBanAn.setImageResource(R.drawable.banantrue);
        }else{
            viewHolderBanAn.imBanAn.setImageResource(R.drawable.banant);
        }

        viewHolderBanAn.txtTenBanAn.setText(banAnDTO.getTenBan());
        viewHolderBanAn.imBanAn.setTag(position);

        viewHolderBanAn.imBanAn.setOnClickListener(this);
        viewHolderBanAn.imGoiMon.setOnClickListener(this);
        viewHolderBanAn.imThanhToan.setOnClickListener(this);
        viewHolderBanAn.imAnButton.setOnClickListener(this);

        return view;
    }

    private void HienThiButton(){

        viewHolderBanAn.imGoiMon.setVisibility(View.VISIBLE);
        viewHolderBanAn.imThanhToan.setVisibility(View.VISIBLE);
        viewHolderBanAn.imAnButton.setVisibility(View.VISIBLE);

        Animation animation = AnimationUtils.loadAnimation(context,R.anim.hieuung_hienthi_button_banan);
        viewHolderBanAn.imGoiMon.startAnimation(animation);
        viewHolderBanAn.imThanhToan.startAnimation(animation);
        viewHolderBanAn.imAnButton.startAnimation(animation);

    }

    private void AnButton(boolean hieuung){
        if(hieuung){
            Animation animation = AnimationUtils.loadAnimation(context,R.anim.hieuung_anbutton_banan);
            viewHolderBanAn.imGoiMon.startAnimation(animation);
            viewHolderBanAn.imThanhToan.startAnimation(animation);
            viewHolderBanAn.imAnButton.startAnimation(animation);
        }

        viewHolderBanAn.imGoiMon.setVisibility(View.INVISIBLE);
        viewHolderBanAn.imThanhToan.setVisibility(View.INVISIBLE);
        viewHolderBanAn.imAnButton.setVisibility(View.INVISIBLE);
    }


    public void capnhattinhtrang(final int mb)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlcnhat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("true")){
                            Toast.makeText(context, "update", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(context, "Loi sql", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maban",String.valueOf(mb));

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    public void goimon(final String mb, final String manv, final String ng, final String tt)
    {
        //Toast.makeText(context, "vao ham goi mon", Toast.LENGTH_LONG).show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgoimon,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("true")){
                            Toast.makeText(context, "Them goi mon", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(context, "loi them goi mon"+ response, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maban",mb);
                params.put("manv",manv);
                params.put("ngaygoi",ng);
                params.put("tinhtrang",tt);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        viewHolderBanAn = (ViewHolderBanAn) ((View)v.getParent()).getTag();
        int vitri1 = (int) viewHolderBanAn.imBanAn.getTag();
        int maban = banAnDTOList.get(vitri1).getMaBan();
        String tenban= banAnDTOList.get(vitri1).getTenBan();
        switch (id){
            case R.id.imBanAn:
                int vitri = (int) v.getTag();
                banAnDTOList.get(vitri).setDuocChon(false);
                HienThiButton();
                ;break;

            case R.id.imGoiMon:

                Intent layITrangChu = ((home)context).getIntent();
                int manhanvien = layITrangChu.getIntExtra("manhanvien", 0);




                String tinhtrang = String.valueOf(banAnDTOList.get(vitri1).isDuocChon());
               // Toast.makeText(context, "ma nhan vien"+ tendn, Toast.LENGTH_LONG).show();
                if(tinhtrang.equals("false")){
                    Calendar calendar = Calendar.getInstance();


                    DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    dateFormatter.setLenient(false);
                    Date today = new Date();
                    String ngaygoi = dateFormatter.format(today);

                    /*MonDTO goiMonDTO = new GoiMonDTO();
                    goiMonDTO.setMaBan(maban);
                    goiMonDTO.setMaNV(manhanvien);
                    goiMonDTO.setNgayGoi(ngaygoi);
                    goiMonDTO.setTinhTrang("false");*/

                    Log.d("a", String.valueOf(maban));
                    Log.d("a", String.valueOf(manhanvien));
                    Log.d("a", String.valueOf(ngaygoi));
                    Log.d("a", String.valueOf(tinhtrang));

                    goimon(String.valueOf(maban),String.valueOf(manhanvien),ngaygoi,"false");

                    //long kiemtra = goiMonDAO.ThemGoiMon(goiMonDTO);
                    //banAnDAO.CapNhatLaiTinhTrangBan(maban,"true");
                    capnhattinhtrang(maban);


                }

                FragmentTransaction tranThucDonTransaction = fragmentManager.beginTransaction();
                HienThiThucDonFragment hienThiThucDonFragment = new HienThiThucDonFragment();

                Bundle bDuLieuThucDon = new Bundle();
                bDuLieuThucDon.putInt("maban",maban);

                hienThiThucDonFragment.setArguments(bDuLieuThucDon);

                tranThucDonTransaction.replace(R.id.content,hienThiThucDonFragment).addToBackStack("hienthibanan");
                tranThucDonTransaction.commit();

                ;break;

            case R.id.imThanhToan:
                Intent layITrangChu1 = ((home)context).getIntent();
                tendn= layITrangChu1.getStringExtra("tendn");
                //Toast.makeText(context, "ma nhan vien"+ tendn, Toast.LENGTH_LONG).show();


                Intent iGuiBep = new Intent(context, ThanhToanActivity.class);
                 iGuiBep.putExtra("maban",maban);
                 iGuiBep.putExtra("tenban",tenban);
                 iGuiBep.putExtra("tendn",tendn);
                //String tenbanan = banAnDTOList.get().getTenBan();
                 //iGuiBep.putExtra("tenban",tenbanan);
                 context.startActivity(iGuiBep);
                ;break;

            case R.id.imAnButton:
                AnButton(true);
                ;break;
        }
    }
}
