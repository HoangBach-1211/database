package com.thanhnguyen.smartorder.FragmentApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thanhnguyen.smartorder.CustomAdapter.AdapterHienThiBanAn;
import com.thanhnguyen.smartorder.DAO.BanAnDAO;
import com.thanhnguyen.smartorder.DTO.BanAnDTO;
import com.thanhnguyen.smartorder.GopBanAnActivity;
import com.thanhnguyen.smartorder.R;
import com.thanhnguyen.smartorder.SuaBanAnActivity;
import com.thanhnguyen.smartorder.ThemBanAnActivity;
import com.thanhnguyen.smartorder.config;
import com.thanhnguyen.smartorder.home;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowTable extends Fragment{


    public static int RESQUEST_CODE_THEM = 111;
    int maban;
    String tenban;
    public static int RESQUEST_CODE_GOP = 69;
    public static int RESQUEST_CODE_SUA = 16;
    final List<BanAnDTO> listBA = new ArrayList<BanAnDTO>();
    String urlgoimon=config.domain +"android/goimon.php";
    String URL_GET_PRODUCT=config.domain +"android/getalltable.php";
    DatabaseReference mData = FirebaseDatabase.getInstance("https://smartorder-13eb1.firebaseio.com/").getReference();

    public List<BanAnDTO> LayTatCaBanAn(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                      //  Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            // Convert json array to jsonobject
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                BanAnDTO product = new BanAnDTO();
                                product.setMaBan(item.getInt("maban"));
                                product.setTenBan(item.getString("tenban"));
                                product.setDuocChon(item.getBoolean("tinhtrang"));
                                listBA.add(product);
                            }
                            HienThiDanhSachBanAn();
                        } catch (Exception ex) {
                            //Toast.makeText(getActivity(), ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
        return listBA;

    }

    GridView gvHienThiBanAn;
    List<BanAnDTO> banAnDTOList;
    BanAnDAO banAnDAO;
    AdapterHienThiBanAn adapterHienThiBanAn;
    int maquyen = 0;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listBA.clear();
        View view = inflater.inflate(R.layout.layout_hienthibanan,container,false);
        setHasOptionsMenu(true);
        ((home)getActivity()).getSupportActionBar().setTitle("Bàn ăn");

        gvHienThiBanAn = (GridView) view.findViewById(R.id.gvHienBanAn);

        sharedPreferences = getActivity().getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen",0);

        banAnDAO = new BanAnDAO(getActivity());


        LayTatCaBanAn();
        banAnDTOList = listBA;

        //HienThiDanhSachBanAn();
        //registerForContextMenu(gvHienThiBanAn);
        {
            registerForContextMenu(gvHienThiBanAn);
        }


        return view;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu, menu);
    }




    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        maban = banAnDTOList.get(vitri).getMaBan();
        tenban = banAnDTOList.get(vitri).getTenBan();
        //Toast.makeText(getActivity(), "nhan giu", Toast.LENGTH_SHORT).show();

        switch (id){
            case R.id.itSua:
                Intent intent = new Intent(getActivity(), SuaBanAnActivity.class);
                intent.putExtra("tenban",tenban);
                intent.putExtra("maban",maban);
                startActivityForResult(intent,RESQUEST_CODE_SUA);
                ;break;

            case R.id.itXoa:
                xoa();
                ;break;
            case R.id.itGop:
                Intent intent1 = new Intent(getActivity(), GopBanAnActivity.class);
                intent1.putExtra("tenban",tenban);
                intent1.putExtra("maban",maban);
                startActivityForResult(intent1,RESQUEST_CODE_GOP);
                ;break;


        }
        return super.onContextItemSelected(item);
    }


    public void xoa()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.domain +"android/xoabanan.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            listBA.clear();
                            LayTatCaBanAn();
                            HienThiDanhSachBanAn();

                        mData.child("BanAn").child(tenban).removeValue();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tenban",tenban);
                params.put("maban",String.valueOf(maban));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        {
            Log.d("maq", String.valueOf(maquyen));
            // if (maquyen == 0)
            {
                MenuItem itThemBanAn = menu.add(1, R.id.itThemBanAn, 1, R.string.thembanan);
                itThemBanAn.setIcon(R.drawable.dinner1);
                itThemBanAn.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }
        }

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){

            case R.id.itThemBanAn:
                Intent iThemBanAn = new Intent(getActivity(), ThemBanAnActivity.class);
                startActivityForResult(iThemBanAn,RESQUEST_CODE_THEM);
                ;break;
        }

        return true;
    }

    private void HienThiDanhSachBanAn(){
        banAnDTOList = listBA;
        adapterHienThiBanAn = new AdapterHienThiBanAn(getActivity(),R.layout.custom_layout_hienthibanan,banAnDTOList);
        gvHienThiBanAn.setAdapter(adapterHienThiBanAn);
        adapterHienThiBanAn.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESQUEST_CODE_THEM){
            if(resultCode == Activity.RESULT_OK){
                Intent intent = data;
                boolean kiemtra = intent.getBooleanExtra("ketquathem",false);
                if(kiemtra){
                    HienThiDanhSachBanAn();
                }else{
                }
            }
        }else if(requestCode == RESQUEST_CODE_SUA){
            if(resultCode == Activity.RESULT_OK){
                Intent intent = data;
                boolean kiemtra = intent.getBooleanExtra("kiemtra",false);
                HienThiDanhSachBanAn();
                if(kiemtra){
                    Toast.makeText(getActivity(), "Sua thanh cong!", Toast.LENGTH_SHORT).show();
                    listBA.clear();
                    LayTatCaBanAn();
                    HienThiDanhSachBanAn();
                }else{
                    Toast.makeText(getActivity(), getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
