package com.thanhnguyen.smartorder.FragmentApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thanhnguyen.smartorder.CustomAdapter.AdapterHienThiNhanVien;
import com.thanhnguyen.smartorder.DAO.NhanVienDAO;
import com.thanhnguyen.smartorder.DTO.NhanVienDTO;
import com.thanhnguyen.smartorder.R;
import com.thanhnguyen.smartorder.config;
import com.thanhnguyen.smartorder.dangky;
import com.thanhnguyen.smartorder.home;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HienThiNhanVienFragment extends Fragment {


    ListView listNhanVien;
    NhanVienDAO nhanVienDAO;
    List<NhanVienDTO> nhanVienDTOList;
    int maquyen;
    int manhanvien;
    String tendn, ns,sdt;
    SharedPreferences sharedPreferences;



    final List<NhanVienDTO> listBA = new ArrayList<NhanVienDTO>();

    String URL_GET_PRODUCT= config.domain +"android/getallnv.php";
    public List<NhanVienDTO> GetNV(){
        listBA.clear();
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
                                NhanVienDTO product = new NhanVienDTO();
                                product.setMANV(item.getInt("manv"));
                                product.setTENDN(item.getString("tendn"));
                                product.setNGAYSINH(item.getString("ngaysinh"));
                                product.setGIOITINH(item.getString("gioitinh"));
                                product.setSDT(item.getString("sdt"));
                                listBA.add(product);

                            }
                            HienThiDanhSachNhanVien();

                            // productAdapter.notifyDataSetChanged();

                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(getActivity(), ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
        return listBA;

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthinhanvien,container,false);
        ((home)getActivity()).getSupportActionBar().setTitle("Nhân viên");
        setHasOptionsMenu(true);

        listNhanVien = (ListView) view.findViewById(R.id.listViewNhanVien);

        nhanVienDAO = new NhanVienDAO(getActivity());
        GetNV();

        HienThiDanhSachNhanVien();

        sharedPreferences = getActivity().getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen",1);

        {
            registerForContextMenu(listNhanVien);
        }


        return view;
    }

    private void HienThiDanhSachNhanVien(){
        nhanVienDTOList = listBA;
        AdapterHienThiNhanVien adapterHienThiNhanVien = new AdapterHienThiNhanVien(getActivity(),R.layout.custom_layout_hienthinhanvien,nhanVienDTOList);
        listNhanVien.setAdapter(adapterHienThiNhanVien);
        adapterHienThiNhanVien.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_nv,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        manhanvien= nhanVienDTOList.get(vitri).getMANV();
        tendn= nhanVienDTOList.get(vitri).getTENDN();
        ns= nhanVienDTOList.get(vitri).getNGAYSINH();
        sdt=nhanVienDTOList.get(vitri).getSDT();
        //Toast.makeText(getContext(), "Ma nhan vien"+ manhanvien, Toast.LENGTH_SHORT).show();

        switch (id){
            case R.id.itSua:
                Intent iDangKy = new Intent(getActivity(),dangky.class);
                iDangKy.putExtra("manv",1);
                iDangKy.putExtra("landautien","1");
                iDangKy.putExtra("tendn",tendn);
                iDangKy.putExtra("ns",ns);
                iDangKy.putExtra("sdt", sdt);
                startActivity(iDangKy);
                ;break;

            case R.id.itXoa:
                xoanv();
                GetNV();
                HienThiDanhSachNhanVien();
                 boolean kiemtra = nhanVienDAO.XoaNhanVien(manhanvien);
                 if (kiemtra){
                     HienThiDanhSachNhanVien();
                 }else{

                 }
                ;break;
        }
        return true;
    }


    public void xoanv()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.domain +"android/xoanv.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("true")){
                            //Toast.makeText(getContext(), "Đã xóa!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                           // Toast.makeText(getActivity(), "Lỗi ", Toast.LENGTH_SHORT).show();
                        }
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
                params.put("manv",String.valueOf(manhanvien));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


            MenuItem itThemBanAn = menu.add(1,R.id.itThemNhanVien,1,R.string.themnhanvien);
            itThemBanAn.setIcon(R.drawable.nhanvien);
            itThemBanAn.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itThemNhanVien:
                 Intent iDangKy = new Intent(getActivity(), dangky.class);
                iDangKy.putExtra("manv",2);
                iDangKy.putExtra("landautien","1");
                startActivity(iDangKy);
                ;break;
        }
        return true;
    }
}
