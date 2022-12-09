package com.thanhnguyen.smartorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HTLichSu  extends AppCompatActivity {

    ListView lwLS;
    List<lsgoimon> mangLS;
    final List<lsgoimon> listBA = new ArrayList<lsgoimon>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hienthilsgoimon);
        lwLS= findViewById(R.id.listviewLSthanhtoan);
        mangLS= new ArrayList<lsgoimon>();
        mangLS=getLSTT();
    }

    private void HienThiDanhSachBanAn(){
        lsgoimonAdapter lsgoimonAdapter= new lsgoimonAdapter(HTLichSu.this, R.layout.lsthanhtoan, mangLS);
        lwLS.setAdapter(lsgoimonAdapter);
        lsgoimonAdapter.notifyDataSetChanged();
    }
    String URL_GET_PRODUCT=config.domain +"android/getlstt.php";

    public List<lsgoimon> getLSTT(){
        listBA.clear();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT, null,
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
                                listBA.add(product);
                            }

                            HienThiDanhSachBanAn();
                            //Toast.makeText(HTLichSu.this, listBA.size(), Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Toast.makeText(HTLichSu.this, ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HTLichSu.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
        return listBA;

    }

}
