package com.Nhom10.BTL;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NhaBepHienMon extends AppCompatActivity {


    DatabaseReference mData = FirebaseDatabase.getInstance("https://smartorder-13eb1.firebaseio.com/").getReference();
    ArrayList<QLMonAnBep> arrayList;
    ListView listViewdsMon;
    QLmonAnBepAdapter qLmonAnBepAdapter;
    ArrayAdapter adapter;
    TextView txtghichu,edchat,txtchat2;
    String tendn ="";
    String tm,sl;
    Button btnBack,btnXacnhan, btnhoanthanh;
    ImageButton btngoi;
    boolean check = false;
    // SharedPreferences
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nha_bep_hien_mon);
        listViewdsMon = (ListView) findViewById(R.id.lvMonAn);
        btnBack= (Button) findViewById(R.id.goback);
        btnXacnhan=findViewById(R.id.btnxacnhan);
        txtchat2=findViewById(R.id.txtchat2);
        btnhoanthanh= findViewById(R.id.btnhoanthanh);
        btngoi= findViewById(R.id.btngoii);
        txtghichu= findViewById(R.id.txthienghichu);
        edchat=findViewById(R.id.edchat);
        arrayList = new ArrayList<QLMonAnBep>();
        btnhoanthanh.setEnabled(true);
        Intent intent = getIntent();
        tendn = intent.getStringExtra("tenmon");
        HienMon();
        qLmonAnBepAdapter= new QLmonAnBepAdapter(NhaBepHienMon.this, R.layout.nhabephienmon, arrayList);
        qLmonAnBepAdapter.notifyDataSetChanged();
        listViewdsMon.setAdapter(qLmonAnBepAdapter);
        btnXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child("BanAn").child(tendn).child("TinhTrang").setValue("cho");
                btnXacnhan.setEnabled(false);
                btnhoanthanh.setEnabled(true);
            }
        });
        btnhoanthanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child("BanAn").child(tendn).child("TinhTrang").setValue("hoanthanh");
                Intent intent= new Intent(NhaBepHienMon.this, NhaBepNhanDL.class);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();

            }
        });
        btngoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child("BanAn").child(tendn).child("Chat1").setValue(edchat.getText().toString());
                edchat.setText("");
            }
        });



    }
    public void back()
    {
        Intent intent = new Intent(NhaBepHienMon.this, NhaBepNhanDL.class);
        intent.putExtra("b1","lol");
        startActivity(intent);
    }

    private void HienMon() {
        try {
            mData.child("BanAn").child(tendn.trim()).child("Chat").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Toast.makeText(NhaBepHienMon.this, "vao", Toast.LENGTH_SHORT).show();
                    try {
                        if(!dataSnapshot.getValue().toString().trim().equals("")) {
                            txtchat2.setText("NV: "+ dataSnapshot.getValue().toString());
                        }
                    }


                    catch (Exception ex)
                    {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception ex)
        {

        }


        mData.child("BanAn").child(tendn.trim()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey().trim();

                if(!key.equals("TinhTrang")){
                    Log.d("AAA","x - " + key);

                    try{
                        tm = dataSnapshot.child("tenmon").getValue().toString();
                        sl = dataSnapshot.child("soluong").getValue().toString();
                        String a=dataSnapshot.child("ghichu").getValue().toString();
                        if( a!="")
                        {
                            txtghichu.setText(a);
                        }
                        txtghichu.setText("Không có yêu cầu đặc biệt!");

                    }
                    catch (Exception ex)
                    {

                    }
                    arrayList.clear();
                    arrayList.add(new QLMonAnBep(tm, sl));
                    //Toast.makeText(NhaBepHienMon.this, "sl+"+ sl+" -"+ tm, Toast.LENGTH_SHORT).show();
                    qLmonAnBepAdapter.notifyDataSetChanged();
                }

                else {
                    if(dataSnapshot.getValue().toString().trim().equals("true")){
                        btnhoanthanh.setEnabled(false);
                    }
                    else if(dataSnapshot.getValue().toString().trim().equals("cho")){
                        btnXacnhan.setEnabled(false);
                        btnhoanthanh.setEnabled(true);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}
