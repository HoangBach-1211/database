package com.thanhnguyen.smartorder.FragmentApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.thanhnguyen.smartorder.R;
import com.thanhnguyen.smartorder.config;
import com.thanhnguyen.smartorder.dangky;
import com.thanhnguyen.smartorder.listthanhtoan;
import com.thanhnguyen.smartorder.nhanvienAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowNhanVien extends AppCompatActivity {

    TextView textView;
    Button button;
    ListView lwNhanVien;
    ArrayList<listthanhtoan> arrayNhanVien;
    nhanvienAdapter adapter;
    String urlGetData= config.domain +"android/demo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_thanhtoan);
        //lwNhanVien=  findViewById(R.id.gvThanhToan);
        arrayNhanVien = new ArrayList<>();
        adapter= new nhanvienAdapter(this, R.layout.htthanhtoan,arrayNhanVien);
        lwNhanVien.setAdapter(adapter);
        GetData(urlGetData);
    }
    private void GetData(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(ShowNhanVien.this, "Complete!", Toast.LENGTH_SHORT).show();
                for (int i=0; i< response.length(); i++)
                {
                    try {
                        JSONObject object=response.getJSONObject(i);
                       /* arrayNhanVien.add(new listthanhtoan(
                                object.getString("tdn"),
                                object.getString("tennv"),"",
                                object.getString("sdt"),""
                        ));*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowNhanVien.this, "Không có kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_nhanvien,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.add_nv)
        {
            startActivity(new Intent(ShowNhanVien.this, dangky.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
