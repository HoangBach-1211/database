package com.Nhom10.BTL;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.Nhom10.BTL.DAO.GoiMonDAO;
import com.Nhom10.BTL.DTO.ChiTietGoiMonDTO;

import java.util.HashMap;
import java.util.Map;

public class SoLuongActivity extends AppCompatActivity implements View.OnClickListener {


    int maban,mamonan;
    Button btnDongYThemSoLuong, bttang, btgiam;
    EditText edSoLuong;
    GoiMonDAO goiMonDAO;
    String mgm;
    int magoimon;
    int soluong;
    boolean kt= false;
    String url2=config.domain +"android/ktctgoimon.php";
    String urlgetsoluong= config.domain +"android/laysoluong.php";
    String urlctgoimon=config.domain +"android/chitietgoimon.php";
    String urlupdatectgoimon=config.domain +"android/updatechitietgoimon.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themsoluong);

        btnDongYThemSoLuong = (Button) findViewById(R.id.btnDongYThemSoLuong);
        bttang = (Button) findViewById(R.id.tang);
        btgiam = (Button) findViewById(R.id.giam);
        bttang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sl= Integer.parseInt(edSoLuong.getText().toString());
                sl++;
                String sll= String.valueOf(sl);
                edSoLuong.setText(sll);
            }
        });
        btgiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sl= Integer.parseInt(edSoLuong.getText().toString());
                if (sl>=2)
                {
                    sl--;
                    String sll= String.valueOf(sl);
                    edSoLuong.setText(sll);

                }


            }
        });
        edSoLuong = (EditText) findViewById(R.id.edSoLuongMonAn);

        goiMonDAO = new GoiMonDAO(this);

        Intent intent = getIntent();
        maban = intent.getIntExtra("maban",0);
        mamonan = intent.getIntExtra("mamonan",0);


        btnDongYThemSoLuong.setOnClickListener(this);

    }

    public void laymagoimon(String u)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(SoLuongActivity.this);
        mgm="";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(SoLuongActivity.this, "xxx"+ response, Toast.LENGTH_SHORT).show();
                        mgm= response;
                        magoimon =Integer.parseInt(mgm);
                        int soluonggoi = Integer.parseInt(edSoLuong.getText().toString());

                        ChiTietGoiMon(String.valueOf(magoimon),String.valueOf(mamonan),String.valueOf(soluonggoi));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SoLuongActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
        };

        requestQueue.add(stringRequest);
    }

    public void KiemTraDaTonTai(final int a, final int b){
        RequestQueue requestQueue = Volley.newRequestQueue(SoLuongActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Toast.makeText(SoLuongActivity.this, "Datontai+ " + kt, Toast.LENGTH_SHORT).show();
                            kt = Boolean.parseBoolean(response);
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(SoLuongActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SoLuongActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("magoimon",String.valueOf(a));
                params.put("mamonan",String.valueOf(b));
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }


    public void LaySoLuong(final int a, final int b){
        RequestQueue requestQueue = Volley.newRequestQueue(SoLuongActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetsoluong,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(goiMonDAO, "Soluong lay dc"+ soluong, Toast.LENGTH_SHORT).show();
                        soluong=Integer.parseInt(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SoLuongActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("magoimon",String.valueOf(a));
                params.put("mamonan",String.valueOf(b));
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

    public void ChiTietGoiMon(final String mgm, final String mamonan, final String soluong)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(SoLuongActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlctgoimon,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(SoLuongActivity.this, mgm +" -" + mamonan+ "- "+ soluong, Toast.LENGTH_LONG).show();
                        if (response.trim().equals("true")){
                            Toast.makeText(SoLuongActivity.this, "Đã thêm vào danh sách gọi món", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SoLuongActivity.this, "Loi" + response, Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SoLuongActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("magoimon",mgm);
                params.put("mamonan",mamonan);
                params.put("soluong",soluong);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    public void UpdateChiTietGoiMon(final String mgm, final String mamonan, final String soluong)
    {
        //Toast.makeText(context, "vao ham goi mon", Toast.LENGTH_LONG).show();
        RequestQueue requestQueue = Volley.newRequestQueue(SoLuongActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdatectgoimon,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("true")){
                            Toast.makeText(SoLuongActivity.this, "update!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SoLuongActivity.this, "Loi", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SoLuongActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("magoimon",mgm);
                params.put("mamonan",mamonan);
                params.put("soluong",soluong);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }



    @Override
    public void onClick(View v) {

        //Toast.makeText(SoLuongActivity.this, "mabna"+ maban, Toast.LENGTH_SHORT).show();
        laymagoimon(config.domain +"android/laymagoimontb.php?maban="+maban);
        Toast.makeText(SoLuongActivity.this, "mabna"+ maban+ "-"+" magoi"+ mgm, Toast.LENGTH_SHORT).show();

        //Integer.parseInt(mgm); //Integer.parseInt(magoi); //goiMonDAO.LayMaGoiMonTheoMaBan(maban,"false");
       // KiemTraDaTonTai(magoimon,mamonan);
        boolean kiemtra = kt;//goiMonDAO.KiemTraMonAnDaTonTai(magoimon,mamonan);
        if(kiemtra){
            LaySoLuong(magoimon,mamonan);
            int soluongcu = soluong; //goiMonDAO.LaySoLuongMonAnTheoMaGoiMon(magoimon,mamonan);
            int soluongmoi = Integer.parseInt(edSoLuong.getText().toString());

            int tongsoluong = soluongcu + soluongmoi;


            UpdateChiTietGoiMon(String.valueOf(magoimon),String.valueOf(mamonan),String.valueOf(soluong));

            ChiTietGoiMonDTO chiTietGoiMonDTO = new ChiTietGoiMonDTO();
            chiTietGoiMonDTO.setMaGoiMon(magoimon);
            chiTietGoiMonDTO.setMaMonAn(mamonan);
            chiTietGoiMonDTO.setSoLuong(tongsoluong);

            boolean kiemtracapnhat =goiMonDAO.CapNhatSoLuong(chiTietGoiMonDTO);
            if(kiemtracapnhat){
                Toast.makeText(this, getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
            }

        }else{




            /*ChiTietGoiMonDTO chiTietGoiMonDTO = new ChiTietGoiMonDTO();
            chiTietGoiMonDTO.setMaGoiMon(magoimon);
            chiTietGoiMonDTO.setMaMonAn(mamonan);
            chiTietGoiMonDTO.setSoLuong(soluonggoi);

            boolean kiemtracapnhat = goiMonDAO.ThemChiTietGoiMon(chiTietGoiMonDTO);
            if(kiemtracapnhat){
                Toast.makeText(this, getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
            }*/
        }

        finish();
    }
}
