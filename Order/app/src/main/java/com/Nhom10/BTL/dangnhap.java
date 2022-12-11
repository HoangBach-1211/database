package com.Nhom10.BTL;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.Nhom10.BTL.DAO.NhanVienDAO;
import com.Nhom10.BTL.FragmentApp.quenmk;

import java.util.HashMap;
import java.util.Map;

public class dangnhap extends AppCompatActivity implements OnClickListener {


    Button btnDongYDN,btnDangKyDN;
    EditText edTenDangNhapDN, edMatKhauDN;
    NhanVienDAO nhanVienDAO;
    Button btnquenmk;
    int maquyen;
    CheckBox cbRemember;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USERNAME = "userNameKey";
    public static final String PASS = "passKey";
    public static final String QUYEN = "Quyen";
    public static final String REMEMBER = "remember";
    SharedPreferences sharedpreferences;
    FloatingActionButton floatingActionButton;
    String url=config.domain + "android/login.php";
    String urlmaquyen=config.domain + "android/getmaquyen.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        btnDangKyDN = (Button) findViewById(R.id.btndk_dn);
        btnDongYDN = (Button) findViewById(R.id.btndn_dn);
        edMatKhauDN = (EditText) findViewById(R.id.edmk_dn);
        edTenDangNhapDN = (EditText) findViewById(R.id.edTendn_dn);
        cbRemember= (CheckBox) findViewById(R.id.ckremember);
        btnquenmk= findViewById(R.id.btnQuenmk);

        nhanVienDAO = new NhanVienDAO(this);
        btnDongYDN.setOnClickListener(this);
        btnDangKyDN.setOnClickListener(this);
        btnquenmk.setOnClickListener(this);
        loadData();
    }

    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void saveData(String username, String Pass,int Quyen) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASS, Pass);
        editor.putInt(QUYEN,Quyen);
        editor.putBoolean(REMEMBER,cbRemember.isChecked());
        editor.commit();
    }

    private void loadData() {
        if(sharedpreferences.getBoolean(REMEMBER,false)) {
            edTenDangNhapDN.setText(sharedpreferences.getString(USERNAME, ""));
            edMatKhauDN.setText(sharedpreferences.getString(PASS, ""));
            cbRemember.setChecked(true);
            login(url);
        }
        else
            cbRemember.setChecked(false);

    }




    private void btnDongY(){
        login(url);
        if (cbRemember.isChecked())
            saveData(edTenDangNhapDN.getText().toString(), edMatKhauDN.getText().toString(),maquyen);
        else
            clearData();

        /*String sTenDangNhap = edTenDangNhapDN.getText().toString();
        String sMatKhau = edMatKhauDN.getText().toString();
        int kiemtra = nhanVienDAO.KiemTraDangNhap(sTenDangNhap, sMatKhau);
        int maquyen = nhanVienDAO.LayQuyenNhanVien(kiemtra);
        if(kiemtra != 0){
            SharedPreferences sharedPreferences = getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("maquyen",maquyen);
            editor.commit();

            Intent iTrangChu = new Intent(dangnhap.this, home.class);
            iTrangChu.putExtra("tendn",edTenDangNhapDN.getText().toString());
            iTrangChu.putExtra("manhanvien",kiemtra);
            startActivity(iTrangChu);
        }else{
            //Toast.makeText(dangnhap.this,"Đăng nhập thất bại !",Toast.LENGTH_SHORT).show();
        }*/
    }
    private void loginok(){

        laymaquyen(urlmaquyen);

    }
    public void laymaquyen(String url) {
      {
            RequestQueue requestQueue = Volley.newRequestQueue(dangnhap.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url+"?tendn="+edTenDangNhapDN.getText().toString().trim(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            maquyen=Integer.parseInt(response);
                            Toast.makeText(dangnhap.this, response, Toast.LENGTH_LONG).show();
                            if(maquyen==1 || maquyen==2)
                            {
                                Intent iTrangChu = new Intent(dangnhap.this, home.class);
                                iTrangChu.putExtra("tendn",edTenDangNhapDN.getText().toString());
                                iTrangChu.putExtra("maquyen",maquyen);
                                startActivity(iTrangChu);
                            }
                            if(maquyen==3)
                            {
                                Intent iTrangChu = new Intent(dangnhap.this, NhaBepNhanDL.class);
                                iTrangChu.putExtra("tendn",edTenDangNhapDN.getText().toString());
                                iTrangChu.putExtra("maquyen",maquyen);
                                startActivity(iTrangChu);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(dangnhap.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("tendn",edTenDangNhapDN.getText().toString().trim());
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }

    }

    private void btnDangKy(){
        Intent iDangKy = new Intent(dangnhap.this, dangky.class);
        iDangKy.putExtra("landautien",1);
        startActivity(iDangKy);
    }
    public void login(String url) {
        final String user=edTenDangNhapDN.getText().toString();
        final String pass=edMatKhauDN.getText().toString();
        if (user.contains("'") || user.contains("`") || pass.contains("'") || pass.contains("`"))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Hack")
                    .setMessage("Return^^")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else
        {
            if(user.length()!=0 && pass.length()!=0){
                RequestQueue requestQueue = Volley.newRequestQueue(dangnhap.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    maquyen=Integer.parseInt(response.toString().trim());
                                    Toast.makeText(dangnhap.this, "maquyen" +maquyen, Toast.LENGTH_SHORT).show();
                                    if(maquyen==1 || maquyen==2)
                                    {
                                        Intent iTrangChu = new Intent(dangnhap.this, home.class);
                                        iTrangChu.putExtra("tendn",edTenDangNhapDN.getText().toString());
                                        iTrangChu.putExtra("maquyen",maquyen);
                                        startActivity(iTrangChu);
                                    }
                                    if(maquyen==3)
                                    {
                                        Intent iTrangChu = new Intent(dangnhap.this, NhaBepNhanDL.class);
                                        iTrangChu.putExtra("tendn",edTenDangNhapDN.getText().toString());
                                        iTrangChu.putExtra("maquyen",maquyen);
                                        startActivity(iTrangChu);
                                    }
                                    else if(maquyen==5){
                                        Toast.makeText(dangnhap.this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                                        edMatKhauDN.setText("");
                                    }

                                }
                                catch (Exception ex)
                                {
                                    Toast.makeText(dangnhap.this, "Tên đăng nhập hoặc mật khẩu không đúng!"
                                            + ex.toString(), Toast.LENGTH_SHORT).show();
                                    edMatKhauDN.setText("");
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(dangnhap.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("tendn",user.trim());
                        params.put("mk",pass.trim());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }else {
                Toast.makeText(dangnhap.this,"Chưa nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
            }
        }



    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){

            case R.id.btndn_dn:
                btnDongY();
                ;break;

            case R.id.btndk_dn:
                btnDangKy();
                ;break;
            case R.id.btnQuenmk:
            {
                Intent iTrangChu = new Intent(dangnhap.this, quenmk.class);
                startActivity(iTrangChu);

            }
        }
    }
}
