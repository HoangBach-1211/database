package com.Nhom10.BTL;

import android.app.Activity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.Nhom10.BTL.DAO.BanAnDAO;

import java.util.HashMap;
import java.util.Map;

public class ThemBanAnActivity extends AppCompatActivity implements View.OnClickListener {


    EditText edTenThemBanAn;
    Button btnDongYThemBanAn;
    BanAnDAO banAnDAO;
    DatabaseReference mData = FirebaseDatabase.getInstance("https://smartorder-13eb1.firebaseio.com/").getReference();
    String url=config.domain +"android/themban.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_thembanan);

        edTenThemBanAn = (EditText) findViewById(R.id.edThemTenBanAn);
        btnDongYThemBanAn = (Button) findViewById(R.id.btnDongYThemBanAn);

        banAnDAO = new BanAnDAO(this);
        btnDongYThemBanAn.setOnClickListener(this);

    }
    public void thembanan(String url) {
        if(edTenThemBanAn.length()!=0){
            RequestQueue requestQueue = Volley.newRequestQueue(ThemBanAnActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("true")){
                                Intent intent = new Intent(ThemBanAnActivity.this,home.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(ThemBanAnActivity.this, "Bàn này đã có rồi! Đặt tên khác !", Toast.LENGTH_LONG).show();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(ThemBanAnActivity.this, "Kết nối máy chủ thất bại!", Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("tenban",edTenThemBanAn.getText().toString().trim());
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }else {
            Toast.makeText(ThemBanAnActivity.this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
        }

    }
    void insertFireBase(String tenban){
        mData.child("BanAn").child(tenban).child("TinhTrang").setValue(false);
        //mData.child("BanAn").child(tenban).child("maban").setValue()
    }
    @Override
    public void onClick(View v) {
        String sTenBanAn = edTenThemBanAn.getText().toString();
        if(sTenBanAn != null || sTenBanAn.equals("")){
            boolean kiemtra = banAnDAO.ThemBanAn(sTenBanAn);
            thembanan(url);
            insertFireBase(sTenBanAn);
            Intent intent = new Intent();
            intent.putExtra("ketquathem",kiemtra);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
        else {
            Toast.makeText(this,getResources().getString(R.string.vuilongnhapdulieu),Toast.LENGTH_SHORT).show();

        }
    }
}
