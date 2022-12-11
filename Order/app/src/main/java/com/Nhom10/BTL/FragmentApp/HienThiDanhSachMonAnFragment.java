package com.Nhom10.BTL.FragmentApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.Nhom10.BTL.CustomAdapter.AdapterHienThiDanhSachMonAn;
import com.Nhom10.BTL.DAO.MonAnDAO;
import com.Nhom10.BTL.DTO.MonAnDTO;
import com.Nhom10.BTL.R;
import com.Nhom10.BTL.SoLuongActivity;
import com.Nhom10.BTL.config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HienThiDanhSachMonAnFragment extends Fragment {

    GridView gridView;
    MonAnDAO monAnDAO;
    List<MonAnDTO> monAnDTOList;
    AdapterHienThiDanhSachMonAn adapterHienThiDanhSachMonAn;
    int maban;
    int mamon;
    int maloai;
    int x;
    List<MonAnDTO> listBA = new ArrayList<MonAnDTO>();
    String URL_GET_PRODUCT= config.domain +"android/getmonan.php";


    public void getMon(){

        JsonArrayRequest jsArrRequest = new JsonArrayRequest
                (Request.Method.POST, URL_GET_PRODUCT, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                           // Toast.makeText(getActivity(), "get string: " +response.toString(), Toast.LENGTH_LONG).show();
                            // Convert json array to jsonobject
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                MonAnDTO product = new MonAnDTO();
                                product.setMaMonAn(item.getInt("mamon"));
                                product.setTenMonAn(item.getString("tenmon"));
                                product.setMaLoai(item.getInt("maloai"));
                                product.setGiaTien(item.getString("giatien"));
                                product.setHinhAnh(item.getString("hinhanh"));

                                listBA.add(product);
                            }
                            HienThiMonAn(listBA);
                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }

                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("maloai", "147");
                        return params;
                    }

                });

        RequestQueue  requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(jsArrRequest);

    }


    public void LayTatCaMonAn(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT+"?maloai="+maloai, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                          //  Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            // Convert json array to jsonobject
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                MonAnDTO product = new MonAnDTO();
                                product.setMaMonAn(item.getInt("mamon"));
                                product.setTenMonAn(item.getString("tenmon"));
                                product.setMaLoai(item.getInt("maloai"));
                                product.setGiaTien(item.getString("giatien"));
                                product.setHinhAnh(item.getString("hinhanh"));

                                listBA.add(product);
                            }
                            HienThiMonAn(listBA);
                            //HienThiDanhSachBanAn();

                            // productAdapter.notifyDataSetChanged();

                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                        //                  loading.dismiss();

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

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthithucdon, container, false);

        gridView = (GridView) view.findViewById(R.id.gvHienThiThucDon);

        monAnDAO = new MonAnDAO(getActivity());


        Bundle bundle = getArguments();
        if(bundle !=  null){
            maloai = bundle.getInt("maloai");
            maban = bundle.getInt("maban");
            mamon=bundle.getInt("mamon");
            //Toast.makeText(getActivity(), "mamon1"+ mamon, Toast.LENGTH_LONG).show();

            LayTatCaMonAn();
            monAnDTOList = listBA;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(maban !=0 ){
                        Intent iSoLuong = new Intent(getActivity(), SoLuongActivity.class);
                        iSoLuong.putExtra("maban",maban);
                        iSoLuong.putExtra("mamonan",monAnDTOList.get(position).getMaMonAn());

                        startActivity(iSoLuong);
                    }

                }
            });

        }

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    getFragmentManager().popBackStack("hienthiloai", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return false;
            }
        });
        registerForContextMenu(gridView);
        return view;
    }
    public  void HienThiMonAn(List<MonAnDTO> a)
    {
        adapterHienThiDanhSachMonAn = new AdapterHienThiDanhSachMonAn(getActivity(),R.layout.custom_layout_hienthidanhsachmonan,a);
        gridView.setAdapter(adapterHienThiDanhSachMonAn);
        adapterHienThiDanhSachMonAn.notifyDataSetChanged();
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.xoamonan, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        mamon = monAnDTOList.get(vitri).getMaMonAn();
        //Toast.makeText(getActivity(), "mamon"+  mamon, Toast.LENGTH_SHORT).show();


        switch (id){

            case R.id.itXoa:
                xoa();
        }
        return super.onContextItemSelected(item);

       /* return super.onContextItemSelected(item);
        if (item.getItemId()==R.id.itXoa)
        {
            boolean kiemtra = monAnDAO.XoaMonAnTheoMa(maban);
            if(kiemtra){
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.xoathanhcong),Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.loi) + maban,Toast.LENGTH_SHORT).show();
            }

        }*/

    }

    public void xoa()
    {

        Toast.makeText(getActivity(), "mamon"+ mamon, Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, config.domain +"android/xoamonan.php?mamon="+mamon,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        listBA.clear();
                        LayTatCaMonAn();
                        HienThiMonAn(listBA);
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
                params.put("mamon",String.valueOf(mamon));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
