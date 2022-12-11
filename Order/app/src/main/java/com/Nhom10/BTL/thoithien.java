package com.Nhom10.BTL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.Nhom10.BTL.DAO.BanAnDAO;

import java.text.NumberFormat;
import java.util.Locale;

public class thoithien extends AppCompatActivity{


    Button btntinh, btnxong;
    TextView tong, tienthoi;
    EditText edkhachdua;
    BanAnDAO banAnDAO;
    String tienbd;
    long kq,kd;
    String tkhachdua, tthoilai;
    String tenban;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thoitien);

        btntinh = findViewById(R.id.btntinh);
        btnxong= findViewById(R.id.xong);
        tong =  findViewById(R.id.txtTongTien);
        tienthoi= findViewById(R.id.txtthoilai);
        edkhachdua= findViewById(R.id.edtienkhachdua);

        banAnDAO = new BanAnDAO(this);

        Intent intent = getIntent();
        tienbd =intent.getStringExtra("tongtien");

        NumberFormat currentLocale = NumberFormat.getInstance();
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        try{
            long longNumber = Long.parseLong(tienbd);
            String str1 = en.format(longNumber);
            tong.setText(getResources().getString(R.string.tongcong) + str1 + " VND");
        }
        catch (Exception ex)
        {

        }



        btntinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    kd= Long.parseLong(edkhachdua.getText().toString()+"000");
                    kq=  kd- Long.parseLong(tienbd);
                    Locale localeEN = new Locale("en", "EN");
                    NumberFormat en = NumberFormat.getInstance(localeEN);

                    String str1 = en.format(kq);
                    tienthoi.setText("Thối lại: "+ str1 + " VND");
                }
                catch (Exception ex)
                {

                }

            }
        });
        btnxong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                tthoilai= tienthoi.getText().toString();
                tkhachdua=edkhachdua.getText().toString()+"000";
                intent.putExtra("tienthoi",String.valueOf(tthoilai));
                intent.putExtra("khachdua",tkhachdua);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }





}
