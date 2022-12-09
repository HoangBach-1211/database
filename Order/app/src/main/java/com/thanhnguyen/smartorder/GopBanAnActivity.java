package com.thanhnguyen.smartorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thanhnguyen.smartorder.DAO.BanAnDAO;
import com.thanhnguyen.smartorder.DTO.BanAnDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GopBanAnActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnDongYSua;
    Spinner spin;
    BanAnDAO banAnDAO;
    int mabanht;
    String mban, mgoimon, mgoimon1;
    final List<String> list = new ArrayList<String>();
    String tenban;
    public int n;
    String getgoimon= config.domain + "android/getmgm.php";
    String getmaban= config.domain + "android/getidban.php";
    String URL_GET_PRODUCT=config.domain +"android/getalltableplus.php";
    String urlupdateban = config.domain + "android/updateban.php";
    final List<BanAnDTO> listBA = new ArrayList<BanAnDTO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gopban);

        btnDongYSua = findViewById(R.id.btnDongYSuaBanAn);
        spin = findViewById(R.id.spintenban);

        banAnDAO = new BanAnDAO(this);

        mabanht = getIntent().getIntExtra("maban",0);

        btnDongYSua.setOnClickListener(this);
        LayTatCaBanAn();
        //Toast.makeText(GopBanAnActivity.this, mabanht, Toast.LENGTH_SHORT).show();
    }

    public void get(String url, final String tban) {
        if(tenban.length()!=0){
            RequestQueue requestQueue = Volley.newRequestQueue(GopBanAnActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mban="";
                            Toast.makeText(GopBanAnActivity.this, " lay ma ve:"+response, Toast.LENGTH_SHORT).show();
                            mban=response;
                            getmgmon(getgoimon,mban); // lay ma go mon  mban moi


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(GopBanAnActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("tenban",tban);
                    //params.put("maban",String.valueOf(mabanht));
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }else {
            Toast.makeText(GopBanAnActivity.this,"Chưa lấy được mã bàn",Toast.LENGTH_SHORT).show();
        }

    }


    public void getmgmon(String url, final String mbann) {

            RequestQueue requestQueue = Volley.newRequestQueue(GopBanAnActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mgoimon="";
                            //Toast.makeText(GopBanAnActivity.this, " lay ma ve:"+response, Toast.LENGTH_SHORT).show();
                            mgoimon=response;
                            getmgmon1(getgoimon,String.valueOf(mabanht)); // mgm hien tai

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(GopBanAnActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("maban",mbann);
                    return params;
                }
            };

            requestQueue.add(stringRequest);

    }

    public void getmgmon1(String url, final String mbann) {
        if(tenban.length()!=0){
            RequestQueue requestQueue = Volley.newRequestQueue(GopBanAnActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mgoimon1="";
                            //Toast.makeText(GopBanAnActivity.this, " lay ma ve:"+response, Toast.LENGTH_SHORT).show();
                            mgoimon1=response;

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(GopBanAnActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("maban",mbann);
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }else {
            Toast.makeText(GopBanAnActivity.this,"Chưa lấy được mã bàn",Toast.LENGTH_SHORT).show();
        }

    }

    public List<BanAnDTO> LayTatCaBanAn(){
        //list.clear();


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT+"?mabanht="+mabanht, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Toast.makeText(GopBanAnActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            // Convert json array to jsonobject
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                BanAnDTO product = new BanAnDTO();
                                product.setMaBan(item.getInt("maban"));
                                product.setTenBan(item.getString("tenban"));
                                listBA.add(product);
                            }

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                list.add(item.getString("tenban"));
                            }
                            //get(getmaban, tenban);
                            n=list.size();
                            show();
                        } catch (Exception ex) {
                            Toast.makeText(GopBanAnActivity.this, "catch xxx:"+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GopBanAnActivity.this, "catch"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(GopBanAnActivity.this);
        requestQueue.add(request);
        return listBA;

    }

    public void updateban(final String ma, final String mb, final String mc)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdateban,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("true")) {
                            Toast.makeText(GopBanAnActivity.this, "update!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GopBanAnActivity.this, "Loi  update" + response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GopBanAnActivity.this, "loi update ban" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("magoimoncu",ma);
                params.put("magoimonmoi", mb);
                params.put("mabancu", mc);

                return params;
            }
        };

        requestQueue.add(stringRequest);

    }


    @Override
    public void onClick(View v) {

        Toast.makeText(GopBanAnActivity.this, "Hientai "+ mgoimon1 +"mgmMoi" + mgoimon + "bbanht"+ mabanht, Toast.LENGTH_LONG).show();
        updateban(String.valueOf(mgoimon1), mgoimon, String.valueOf(mabanht));
        Intent iTrangChu = new Intent(GopBanAnActivity.this, home.class);
        startActivity(iTrangChu);

    }

    public void show()
    {
        String[] mang= new String[n];
        for( int i=0; i<n; i++)
        {
            mang[i]=list.get(i);
        }

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, mang);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tenban=parent.getItemAtPosition(position).toString();
                get(getmaban, tenban);  //lay ma ban



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Toast.makeText(ThemThucDonActivity.this, "da vao day", Toast.LENGTH_SHORT).show();
                show();

            }
        });
    }


}
