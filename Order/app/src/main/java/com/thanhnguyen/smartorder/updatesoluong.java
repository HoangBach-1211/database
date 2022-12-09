package com.thanhnguyen.smartorder;

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
import com.thanhnguyen.smartorder.DAO.GoiMonDAO;

import java.util.HashMap;
import java.util.Map;

public class updatesoluong extends AppCompatActivity implements View.OnClickListener {


    int maban,mamonan;
    Button btnDongYThemSoLuong, bttang, btgiam;
    EditText edSoLuong;
    GoiMonDAO goiMonDAO;
    String mgm;
    int magoimon;
    String tenban;
    int soluong;
    String url=config.domain +"android/updatesoluong.php";
    public void update(final String a, final int mgm, final int mma)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(updatesoluong.this, "sl" +a + "="+ magoimon+"="+  maban, Toast.LENGTH_SHORT).show();

                        if (response.trim().equals("true")){
                            Intent iTrangChu = new Intent(updatesoluong.this, ThanhToanActivity.class);
                            iTrangChu.putExtra("maban", maban);
                            iTrangChu.putExtra("tenban", tenban);
                            startActivity(iTrangChu);
                        }
                        else{
                            Toast.makeText(updatesoluong.this, "Loi!", Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(updatesoluong.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("soluong",String.valueOf(a));
                params.put("mamonan",String.valueOf(mgm));
                params.put("magoimon",String.valueOf(mma));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themsoluong);
        Intent intent = getIntent();
        magoimon = intent.getIntExtra("magoimon",0);
        mamonan = intent.getIntExtra("mamonan",0);
        soluong=intent.getIntExtra("soluong",0);
        maban=intent.getIntExtra("maban",0);
        tenban=intent.getStringExtra("tenban");



        btnDongYThemSoLuong = (Button) findViewById(R.id.btnDongYThemSoLuong);
        edSoLuong= findViewById(R.id.edSoLuongMonAn);
        bttang = (Button) findViewById(R.id.tang);
        btgiam = (Button) findViewById(R.id.giam);
        String sll= String.valueOf(soluong);
        edSoLuong.setText(sll);
        bttang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    int sl= Integer.parseInt(edSoLuong.getText().toString());
                    sl++;
                    String sll= String.valueOf(sl);
                    edSoLuong.setText(sll);


                } catch (Exception ex)
                {
                    Toast.makeText(updatesoluong.this, ex.toString(), Toast.LENGTH_SHORT).show();
                }


            }
        });
        btgiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int sl= Integer.parseInt(edSoLuong.getText().toString());
                    if (sl>=2)
                    {
                        sl--;
                        String sll= String.valueOf(sl);
                        edSoLuong.setText(sll);

                    }


                } catch (Exception ex)
                {
                    Toast.makeText(updatesoluong.this, ex.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        edSoLuong = (EditText) findViewById(R.id.edSoLuongMonAn);

        goiMonDAO = new GoiMonDAO(this);



        btnDongYThemSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sl= Integer.parseInt(edSoLuong.getText().toString());
                //Toast.makeText(updatesoluong.this, "soluongx"+soluong, Toast.LENGTH_SHORT).show();
                update(String.valueOf(sl), magoimon, mamonan);
            }
        });

    }

    @Override
    public void onClick(View v) {
}
}
