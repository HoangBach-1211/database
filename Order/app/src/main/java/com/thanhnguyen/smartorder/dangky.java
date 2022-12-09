package com.thanhnguyen.smartorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thanhnguyen.smartorder.DAO.NhanVienDAO;
import com.thanhnguyen.smartorder.DAO.QuyenDAO;
import com.thanhnguyen.smartorder.DTO.NhanVienDTO;
import com.thanhnguyen.smartorder.DTO.QuyenDTO;
import com.thanhnguyen.smartorder.FragmentApp.DatePickerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dangky extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {


    EditText edTenDangNhapDK, edMatKhauDK, edNgaySinhDK,edSDTDK,edreMK;
    Button btnDongYDK, btnThoatDK;
    RadioGroup rgGioiTinh;
    RadioButton rdNam,rdNu;
    TextView txtTieuDeDangKy;
    String sGioiTinh;
    Spinner spinQuyen;
    NhanVienDAO nhanVienDAO;
    QuyenDAO quyenDAO;
    int maquyen=1;
    int manv = 1;
    int landautien=0;
    String tendn, ns,sdt;
    List<QuyenDTO> quyenDTOList;
    List<String> dataAdapter;
    String url=config.domain + "android/dk.php";
    String urlsua=config.domain + "android/suanv.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);
        edTenDangNhapDK = (EditText) findViewById(R.id.edTenDangNhapDK);
        edMatKhauDK = (EditText) findViewById(R.id.edMatKhauDK);
        edreMK=(EditText) findViewById(R.id.edreMatKhauDK);
        edNgaySinhDK = (EditText) findViewById(R.id.edNgaySinhDK);
        txtTieuDeDangKy = (TextView) findViewById(R.id.txtTieuDeDangKy);
        rdNam = (RadioButton) findViewById(R.id.rdNam);
        rdNu = (RadioButton) findViewById(R.id.rdNu);
        edSDTDK = (EditText) findViewById(R.id.edSDTDK);
        btnDongYDK = (Button) findViewById(R.id.btnDongYDK);
        btnThoatDK = (Button) findViewById(R.id.btnThoatDK);
        rgGioiTinh = (RadioGroup) findViewById(R.id.rgGioiTinh);
        spinQuyen = (Spinner) findViewById(R.id.spinQuyen);
        spinQuyen.setVisibility(View.GONE);

        btnDongYDK.setOnClickListener(this);
        btnThoatDK.setOnClickListener(this);
        edNgaySinhDK.setOnFocusChangeListener(this);

        nhanVienDAO = new NhanVienDAO(this);
        quyenDAO = new QuyenDAO(this);

        quyenDTOList = quyenDAO.LayDanhSachQuyen();
        dataAdapter = new ArrayList<String>();

        for (int i = 0; i < quyenDTOList.size(); i ++){
            String tenquyen = quyenDTOList.get(i).getTenQuyen();
            dataAdapter.add(tenquyen);
        }




        manv = getIntent().getIntExtra("manv",0);
        landautien = getIntent().getIntExtra("landautien",0);
        tendn=getIntent().getStringExtra("tendn");
        ns= getIntent().getStringExtra("ns");
        sdt= getIntent().getStringExtra("sdt");
        if (quyenDTOList.size()<3)
        {
            quyenDAO.ThemQuyen("Quản lý(tôi)");
            quyenDAO.ThemQuyen("Nhân viên");
            quyenDAO.ThemQuyen("Nhà bếp");
            //landautien =1;
        }
        if(landautien == 0){
            spinQuyen.setVisibility(View.GONE);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataAdapter);
            spinQuyen.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        List<NhanVienDTO> nhanVienDTOList;

        if (manv==2)
        {
            spinQuyen.setVisibility(View.VISIBLE);
        }
        if(manv == 1){
            txtTieuDeDangKy.setText("Cập nhật TT");
            btnDongYDK.setText("Cập nhật");
            edTenDangNhapDK.setText(tendn);
            edNgaySinhDK.setText(ns);
            edSDTDK.setText(sdt);
            rdNam.setChecked(true);
            spinQuyen.setVisibility(View.VISIBLE);
            /*if(gioitinh.equals("Nam")){
                rdNam.setChecked(true);
            }else{
                rdNu.setChecked(true);
            }*/

        }

    }
    /*List<NhanVienDTO> listBA = new ArrayList<NhanVienDTO>();
    String URL_GET_PRODUCT="https://goimon.000webhostapp.com/android/getnvtheoma.php?manv="+manv;
    public List<NhanVienDTO> GetNV(){
        listBA.clear();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //  Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
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


                            // productAdapter.notifyDataSetChanged();

                        } catch (Exception ex) {

                        }
                        //                  loading.dismiss();

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(getActivity(), ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(dangky.this);
        requestQueue.add(request);
        return listBA;

    }*/

    public void dangky(String url, final String maquyen) {
        if(edTenDangNhapDK.length()!=0 && edMatKhauDK.length()!=0){
            RequestQueue requestQueue = Volley.newRequestQueue(dangky.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("true")){
                                Intent intent = new Intent(dangky.this,dangnhap.class);
                                //intent.putExtra("tendn",edTenDangNhapDK.getText().toString().trim());
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(dangky.this, "Tên đăng nhập đã tồn tại trên hệ thống! hãy đăng nhập hoặc đặt tên khác!", Toast.LENGTH_SHORT).show();
                                edMatKhauDK.setText("");
                                edreMK.setText("");

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(dangky.this, "Kết nối máy chủ thất bại! " + error.toString() , Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("tendn",edTenDangNhapDK.getText().toString().trim());
                    params.put("mk",edMatKhauDK.getText().toString().trim());
                    params.put("gioitinh",sGioiTinh.trim());
                    params.put("ngaysinh",edNgaySinhDK.getText().toString().trim());
                    params.put("sdt",edSDTDK.getText().toString().trim());
                    params.put("maquyen",maquyen.trim());
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }else {
            Toast.makeText(dangky.this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
        }

    }
    private void DongYThemNhanVien(){
        String sTenDangNhap = edTenDangNhapDK.getText().toString();
        String sMatKhau = edMatKhauDK.getText().toString();
        String SreMatKhau=edreMK.getText().toString();
        switch (rgGioiTinh.getCheckedRadioButtonId()){
            case R.id.rdNam:
                sGioiTinh = "Nam";
                break;

            case R.id.rdNu:
                sGioiTinh = "Nữ";
                break;
        }
        String sNgaySinh = edNgaySinhDK.getText().toString();
        String sSDT = (edSDTDK.getText().toString());
        if(sMatKhau.equals(SreMatKhau)==false)
        {
            Toast.makeText(dangky.this,"Nhập lại mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
        }
        else if(sTenDangNhap == null || sTenDangNhap.equals("")){
            Toast.makeText(dangky.this,getResources().getString(R.string.loinhaptendangnhap), Toast.LENGTH_SHORT).show();
        }else if(sMatKhau == null || sMatKhau.equals("")){
            Toast.makeText(dangky.this,getResources().getString(R.string.loinhapmatkhau), Toast.LENGTH_SHORT).show();
        }else if(sSDT == null ){
            Toast.makeText(dangky.this,"Chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
        }else{
            NhanVienDTO nhanVienDTO = new NhanVienDTO();
            nhanVienDTO.setTENDN(sTenDangNhap);
            nhanVienDTO.setMATKHAU(sMatKhau);
            nhanVienDTO.setSDT(sSDT);
            nhanVienDTO.setNGAYSINH(sNgaySinh);
            nhanVienDTO.setGIOITINH(sGioiTinh);
            Log.d("dt", String.valueOf(landautien));
            if (landautien != 0){
                nhanVienDTO.setMAQUYEN(1);
                dangky(url, "1");
            }else {
                //gán quyền bằng quyền mà admin khi chọn tạo nhân viên
                int vitri = spinQuyen.getSelectedItemPosition();
                //Toast.makeText(this, "vitri"+vitri, Toast.LENGTH_LONG).show();
                int maquyen = vitri+1;
                nhanVienDTO.setMAQUYEN(maquyen);
                dangky(url,String.valueOf(maquyen));
            }

            long kiemtra = nhanVienDAO.ThemNhanVien(nhanVienDTO);
            if(kiemtra != 0){
                //Toast.makeText(dangky.this,getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
                //Intent login = new Intent(dangky.this, dangnhap.class);
                //startActivity(login);
            }else{
                //Toast.makeText(dangky.this,getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void suanv(String urlsua) {
        int vitri = spinQuyen.getSelectedItemPosition();
        maquyen = vitri+1;
        if(edTenDangNhapDK.length()!=0 && edMatKhauDK.length()!=0){
            RequestQueue requestQueue = Volley.newRequestQueue(dangky.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsua,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("true")){
                                Intent intent = new Intent(dangky.this,dangnhap.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(dangky.this, "Thêm nhân viên thất bại!", Toast.LENGTH_SHORT).show();
                                edMatKhauDK.setText("");
                                edreMK.setText("");

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(dangky.this, "Kết nối máy chủ thất bại!", Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("tendn",edTenDangNhapDK.getText().toString().trim());
                    params.put("mk",edMatKhauDK.getText().toString().trim());
                    params.put("gioitinh",sGioiTinh.trim());
                    params.put("ngaysinh",edNgaySinhDK.getText().toString().trim());
                    params.put("sdt",edSDTDK.getText().toString().trim());
                    params.put("maquyen",String.valueOf(maquyen).trim());
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }else {
            Toast.makeText(dangky.this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
        }

    }
    private void SuaNhanVien(){
        String sTenDangNhap = edTenDangNhapDK.getText().toString();
        String sMatKhau = edMatKhauDK.getText().toString();
        String sNgaySinh = edNgaySinhDK.getText().toString();
        String sSDT =edSDTDK.getText().toString();
        switch (rgGioiTinh.getCheckedRadioButtonId()){
            case R.id.rdNam:
                sGioiTinh = "Nam";
                break;

            case R.id.rdNu:
                sGioiTinh = "Nữ";
                break;
        }

        NhanVienDTO nhanVienDTO = new NhanVienDTO();
        nhanVienDTO.setMANV(manv);
        nhanVienDTO.setTENDN(sTenDangNhap);
        nhanVienDTO.setMATKHAU(sMatKhau);
        nhanVienDTO.setSDT(sSDT);
        nhanVienDTO.setNGAYSINH(sNgaySinh);
        nhanVienDTO.setGIOITINH(sGioiTinh);
        suanv(urlsua);
        /*boolean kiemtra = nhanVienDAO.SuaNhanVien(nhanVienDTO);
        if(kiemtra){
            suanv(urlsua);
            Toast.makeText(dangky.this,"Sửa thành công!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(dangky.this,getResources().getString(R.string.loi),Toast.LENGTH_SHORT).show();
        }*/

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnDongYDK:
                if(manv == 1){
                    SuaNhanVien();

                }else if(manv == 2){
                    DongYThemNhanVien();
                }
                else{
                    DongYThemNhanVien();
                }


                ;break;

            case R.id.btnThoatDK:
                finish();break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        switch (id){
            case R.id.edNgaySinhDK:
                if(hasFocus){
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.show(getSupportFragmentManager(),"Ngày Sinh");
                    String sNgaySinh = edNgaySinhDK.getText().toString();

                }
                ;break;
        }
    }

}
