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
import com.Nhom10.BTL.DAO.BanAnDAO;

import java.util.HashMap;
import java.util.Map;

public class SuaBanAnActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnDongYSua;
    EditText edSuaTenBan;
    BanAnDAO banAnDAO;
    int maban;
    String tenban;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_suabanan);

        btnDongYSua = (Button) findViewById(R.id.btnDongYSuaBanAn);
        edSuaTenBan = (EditText) findViewById(R.id.edSuaTenBanAn);

        banAnDAO = new BanAnDAO(this);

        maban = getIntent().getIntExtra("maban",0);

        btnDongYSua.setOnClickListener(this);
    }


    public void suatenban()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.domain +"android/suatenban.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("true")){
                            Intent intent = new Intent();
                            intent.putExtra("kiemtra",true);
                            setResult(Activity.RESULT_OK,intent);
                            finish();

                        }
                        else{

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SuaBanAnActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

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
    public void onClick(View v) {

        tenban = edSuaTenBan.getText().toString();
        if(tenban.trim().equals("") || tenban.trim() != null){
            suatenban();
        }else{
            Toast.makeText(SuaBanAnActivity.this, getResources().getString(R.string.vuilongnhapdulieu), Toast.LENGTH_SHORT).show();
        }
    }
}
