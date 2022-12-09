package com.thanhnguyen.smartorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HTThongKe extends AppCompatActivity {

    ListView lwLS;
    List<lsgoimon> mangLS;
    long doanhthu;
    Button btnhomnay, btntoanbo, btnthangnay, btnnamnay,btnloc;
    EditText ednam, edthang;
    TextView eddoanhthu;
    final List<lsgoimon> listBA = new ArrayList<lsgoimon>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listBA.clear();
        setContentView(R.layout.ht_thongke);
        lwLS= findViewById(R.id.listviewLSthanhtoan);
        eddoanhthu= findViewById(R.id.txtdoanhthu);
        btnhomnay= findViewById(R.id.bthomnay);
        btnthangnay= findViewById(R.id.btnthangnay);
        btntoanbo= findViewById(R.id.btnall);
        btnnamnay= findViewById(R.id.btnnamnay);
        btnloc= findViewById(R.id.btnloc);
        edthang=findViewById(R.id.chonthang);
        ednam=findViewById(R.id.chonnam);
        mangLS= new ArrayList<lsgoimon>();
        laythang();
        laynam();
        btntoanbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mangLS.clear();
                mangLS=getLSTT(config.domain +"android/getlstt.php");
            }
        });
        btnhomnay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                dateFormatter.setLenient(false);
                Date today = new Date();
                String ngaygoi = dateFormatter.format(today);
                mangLS.clear();
                mangLS=getLSTT(config.domain +"android/getlsttHN.php?ngaygoi="+ngaygoi);
            }
        });
        btnthangnay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat dateFormatter = new SimpleDateFormat("/MM/yyyy");
                dateFormatter.setLenient(false);
                Date today = new Date();
                String thanggoi = dateFormatter.format(today);
                mangLS.clear();
                mangLS=getLSTT(config.domain +"android/getlsttTN.php?thanggoi="+thanggoi);
            }
        });
        btnnamnay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat dateFormatter = new SimpleDateFormat("/yyyy");
                dateFormatter.setLenient(false);
                Date today = new Date();
                String thanggoi = dateFormatter.format(today);
                mangLS.clear();
                mangLS=getLSTT(config.domain +"android/getlsttNN.php?thanggoi="+thanggoi);
            }
        });
        btnloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thanggoi = edthang.getText().toString()+ "/"+ednam.getText().toString();
                mangLS.clear();
                mangLS=getLSTT(config.domain +"android/getlsttTN.php?thanggoi="+thanggoi);
            }
        });


    }

    public void laythang()
    {
        DateFormat dateFormatter = new SimpleDateFormat("MM");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String ngaygoi = dateFormatter.format(today);
        edthang.setText(ngaygoi);

    }
    public void laynam()
    {
        DateFormat dateFormatter = new SimpleDateFormat("YYYY");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String ngaygoi = dateFormatter.format(today);
        ednam.setText(ngaygoi);

    }

    private void HienThiDanhSachBanAn(){
        lsgoimonAdapter lsgoimonAdapter= new lsgoimonAdapter(HTThongKe.this, R.layout.lsthongke, mangLS);
        lwLS.setAdapter(lsgoimonAdapter);

        NumberFormat currentLocale = NumberFormat.getInstance();
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        long longNumber = doanhthu;
        String str1 = en.format(longNumber);
        eddoanhthu.setText("Doanh thu: " + str1 + " VND");


        lsgoimonAdapter.notifyDataSetChanged();
    }


    public List<lsgoimon> getLSTT(String url){
        listBA.clear();
        doanhthu=0;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                          //Toast.makeText(HTLichSu.this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            // Convert json array to jsonobject
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                lsgoimon product = new lsgoimon();
                                product.setTenbanls(item.getString("bansd"));
                                product.setNgaygoils(item.getString("ngaygoi"));
                                product.setTongtienls(item.getString("tongtien"));
                                doanhthu+=Long.parseLong(item.getString("tongtien"));
                                listBA.add(product);
                            }

                            HienThiDanhSachBanAn();
                            //Toast.makeText(HTLichSu.this, listBA.size(), Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Toast.makeText(HTThongKe.this, ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HTThongKe.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
        return listBA;

    }

}
