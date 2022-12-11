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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.Nhom10.BTL.DAO.LoaiMonAnDAO;

import java.util.HashMap;
import java.util.Map;

public class ThemLoaiThucDonActivity extends AppCompatActivity implements View.OnClickListener {


    StorageReference mData;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    Button btnDongYThemLoaiThucDon;
    EditText edTenLoai;
    LoaiMonAnDAO loaiMonAnDAO;
    String url=config.domain +"android/themloai.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themloaithucdon);
        mData=FirebaseStorage.getInstance().getReference();
        //final StorageReference storageReference= storage.getReferenceFromUrl();
        loaiMonAnDAO = new LoaiMonAnDAO(this);
        btnDongYThemLoaiThucDon = (Button) findViewById(R.id.btnDongYThemLoaiThucDon);
        edTenLoai = (EditText) findViewById(R.id.edThemLoaiThucDon);
        btnDongYThemLoaiThucDon.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        String sTenLoaiThucDon = edTenLoai.getText().toString();
        if(sTenLoaiThucDon != null || sTenLoaiThucDon.equals("")){
            themloai(url);
            boolean kiemtra = loaiMonAnDAO.ThemLoaiMonAn(sTenLoaiThucDon);
            Intent iDuLieu = new Intent();
            iDuLieu.putExtra("kiemtraloaithucdon",kiemtra);
            setResult(Activity.RESULT_OK,iDuLieu);
            finish();
        }else{
            Toast.makeText(this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
        }
    }
    public void themloai(String url) {
        if(edTenLoai.length()!=0){
            RequestQueue requestQueue = Volley.newRequestQueue(ThemLoaiThucDonActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("true")){
                            }
                            else{
                                Toast.makeText(ThemLoaiThucDonActivity.this, "Loại món này đã có rồi mà!", Toast.LENGTH_LONG).show();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(ThemLoaiThucDonActivity.this, "Kết nối máy chủ lỗi!" + error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("tenl",edTenLoai.getText().toString().trim());
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }else {
            Toast.makeText(ThemLoaiThucDonActivity.this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
        }

    }
}
