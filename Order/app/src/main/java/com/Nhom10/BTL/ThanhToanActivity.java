package com.Nhom10.BTL;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.Nhom10.BTL.CustomAdapter.AdapterHienThiThanhToan;
import com.Nhom10.BTL.DAO.BanAnDAO;
import com.Nhom10.BTL.DAO.GoiMonDAO;
import com.Nhom10.BTL.DTO.ThanhToanDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ThanhToanActivity extends AppCompatActivity{


    GridView gridView;
    ImageView imgView;
    int kt=0;
    private NotificationCompat.Builder notBuilder;

    private static final int MY_NOTIFICATION_ID = 123459;

    private static final int MY_REQUEST_CODE = 1009;
    String tkhachdua, tthoilai;
    public static int RESQUEST_CODE_TT = 999;
    int magoimon;
    Button btnGuiBep,btnThanhToan,goback,btninhoadon;
    ImageButton btnchat;
    TextView txtTongTien,txtGhiChu;
    TextView txtchat1;
    GoiMonDAO goiMonDAO;
    List<ThanhToanDTO> listBA = new ArrayList<ThanhToanDTO>();
    String mgm, tennv;
    TextView tinhtrang;
    List<ThanhToanDTO> thanhToanDTOList;
    AdapterHienThiThanhToan adapterHienThiThanhToan;
    long tongtien = 0;
    String hinha,tendn;
    int mamonan, magoi;
    BanAnDAO banAnDAO;
    String ttgui;
    int maban=0;
    String tenban;
    String urlupdategoimon= config.domain+ "android/updategoimon.php";
    String urllaymagoimon= config.domain +"android/laymagoimontb.php";
    DatabaseReference mData = FirebaseDatabase.getInstance("https://smartorder-13eb1.firebaseio.com/").getReference();
    FragmentManager fragmentManager;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_thanhtoan);
        imgView = findViewById(R.id.imHinhThucDon);
        btnchat= findViewById(R.id.chatx);
        tinhtrang = findViewById(R.id.txtTinhTrang);
        gridView =  findViewById(R.id.gvThanhToan);
        txtchat1=findViewById(R.id.txtchat1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent iSoLuong = new Intent(ThanhToanActivity.this, updatesoluong.class);
                iSoLuong.putExtra("magoimon",thanhToanDTOList.get(position).getMaGoiMon());
                iSoLuong.putExtra("mamonan",thanhToanDTOList.get(position).getMaMonAn());
                iSoLuong.putExtra("soluong",thanhToanDTOList.get(position).getSoLuong());
                iSoLuong.putExtra("maban",maban);
                iSoLuong.putExtra("tenban",tenban);
                startActivity(iSoLuong);



            }
        });
        goback= findViewById(R.id.goback);
        btnThanhToan = (Button) findViewById(R.id.btnThanhToan);
        btnGuiBep = (Button) findViewById(R.id.btnGuiBep);
        txtTongTien = (TextView) findViewById(R.id.txtTongTien);
        txtGhiChu=findViewById(R.id.ghichubep);
        btninhoadon= findViewById(R.id.inhoadon);
        goiMonDAO = new GoiMonDAO(this);
        banAnDAO = new BanAnDAO(this);
        registerForContextMenu(gridView);

        Intent intent = getIntent();
        fragmentManager = getSupportFragmentManager();
        maban = getIntent().getIntExtra("maban",0);
        tenban = getIntent().getStringExtra("tenban");
        tendn=getIntent().getStringExtra("tendn");
        tennv=tendn;
        //Toast.makeText(ThanhToanActivity.this, "tdn: "+ tennv, Toast.LENGTH_SHORT).show();
        laymagoimon(String.valueOf(maban), "false");
        //getthanhtoan(20);


        btninhoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ThanhToanActivity.this, "magoimon"+ magoimon, Toast.LENGTH_SHORT).show();

                Intent xx = new Intent(ThanhToanActivity.this, inHoaDon.class);
                xx.putExtra("mgmon",String.valueOf(magoimon));
                startActivity(xx);
                ThanhToanActivity.this.overridePendingTransition(R.anim.hieuung_activity_vao,R.anim.hieuung_activity_ra);


            }

        });

        btnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String gc=txtGhiChu.getText().toString()+ " ";
                    mData.child("BanAn").child(tenban).child("Chat").setValue(gc);
                    txtGhiChu.setText("");
                    Toast.makeText(ThanhToanActivity.this, "Đã gửi", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex)
                {
                    Toast.makeText(ThanhToanActivity.this, "loi"+ ex, Toast.LENGTH_SHORT).show();

                }
            }
        });
       // ggg

       // getthanhtoan(mgm);
        try {
            mData.child("BanAn").child(tenban.trim()).child("TinhTrang").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if(dataSnapshot.getValue().toString().trim().equals("false")){
                            tinhtrang.setText("Tình trạng: Chưa gửi nhà bếp");
                            btnchat.setEnabled(false);
                        }
                        else if (dataSnapshot.getValue().toString().trim().equals("true")){
                            tinhtrang.setText("Tình trạng: Đã gửi yêu cầu");
                            btnGuiBep.setEnabled(false);
                            btnchat.setEnabled(true);
                        }
                        else if (dataSnapshot.getValue().toString().trim().equals("cho")){
                            tinhtrang.setText("Tình trạng: Đang chế biến");
                            btnGuiBep.setEnabled(false);
                        }
                        else
                        if(dataSnapshot.getValue().toString().trim().equals("hoanthanh")){
                            tinhtrang.setText("Tình trạng: Món ăn đã hoàn thành");
                            btnGuiBep.setEnabled(false);
                            createNotification("Món ăn đã chế biến xong");
                        }

                    }



                    //Log.d("AAA",dataSnapshot.getKey().toString() + " - " + dataSnapshot.getValue().toString());
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


        try {
            mData.child("BanAn").child(tenban.trim()).child("Chat1").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if(!dataSnapshot.getValue().toString().trim().equals("")) {
                            txtchat1.setText("Bếp: "+dataSnapshot.getValue().toString());
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
         btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {

                    Intent intent = new Intent(ThanhToanActivity.this, thoithien.class);
                    intent.putExtra("tongtien",ttgui);
                    startActivityForResult(intent,RESQUEST_CODE_TT);




                    //Toast.makeText(ThanhToanActivity.this, "magoi"+ magoi+ "magoimon"+ magoimon, Toast.LENGTH_SHORT).show();
/*
                    update(String.valueOf(magoimon), String.valueOf(tongtien), tenban);
                    try{
                        mData.child("BanAn").child(tenban).removeValue();
                    }
                    catch (Exception ex)
                    {

                    }
                    xoaban();

                    Intent iTrangChu = new Intent(ThanhToanActivity.this, home.class);
                    startActivity(iTrangChu);

 */

                }

            }


        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iTrangChu = new Intent(ThanhToanActivity.this, home.class);
                startActivity(iTrangChu);

            }
        });
        btnGuiBep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mData.child("BanAn").child(tenban).child("TinhTrang").setValue("true");
                    //mData.child("BanAn").child(tenban).child("GhiChu").setValue(gc);
                    for (int i = 0; i < thanhToanDTOList.size(); i++) {
                        String tm = thanhToanDTOList.get(i).getTenMonAn().trim();
                        String sl = String.valueOf(thanhToanDTOList.get(i).getSoLuong()).trim();
                        String gc=" ";
                        if(!txtGhiChu.getText().toString().equals(""))
                        {
                            gc=txtGhiChu.getText().toString();
                        }
                        mon x = new mon(tm, sl, gc);
                        mData.child("BanAn").child(tenban).push().setValue(x);
                    }
                    Toast.makeText(ThanhToanActivity.this, "Gửi yêu cầu thành công", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex)
                {
                    Toast.makeText(ThanhToanActivity.this, "loi"+ ex, Toast.LENGTH_SHORT).show();

                }
            }
        });




    }



    private String CHANNEL_ID="App gọi món";

    private void createNotificationChannel() {
        CharSequence channelName = CHANNEL_ID;
        String channelDesc = "Thanh Nguyen";
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDesc);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            NotificationChannel currChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (currChannel == null)
                notificationManager.createNotificationChannel(channel);
        }
    }




    public void createNotification(String message) {

        if (message != null ) {
            createNotificationChannel();

            Intent intent = new Intent(this, home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle("Phản hồi từ nhà bếp")
                    .setContentText(message)
                    .setPriority(NotificationCompat.DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);


            MediaPlayer mp= MediaPlayer.create(this, R.raw.ht);
            mp.start();

            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(uri);


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            int notificationId = (int) (System.currentTimeMillis()/4);
            notificationManager.notify(notificationId, mBuilder.build());
        }
    }

   /* private void setTinhTrang(String x) {
        Log.d("AAA","ten ban: " + x);
        mData.child("XXX").setValue("XXX");
        mData.child("BanAn").child(x).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("AAA",dataSnapshot.getKey().toString() + " - " + dataSnapshot.getValue().toString());
                if(dataSnapshot.child("TinhTrang").getValue().toString().trim().equals("false")){
                    tinhtrang.setText("Tình trạng: Chưa gửi nhà bếp");
                }
                if (dataSnapshot.getValue().toString().trim().equals("true")){
                    tinhtrang.setText("Tình trạng: Chờ xác nhận");
                }
                if(dataSnapshot.getValue().toString().trim().equals("cho")){
                    tinhtrang.setText("Tình trạng: Đãn nhận yêu cầu");
                }
                if(dataSnapshot.getValue().toString().trim().equals("hoanthanh")){
                    tinhtrang.setText("Tình trạng: Món ăn đã hoàn thành");
                }

                else {
                    tinhtrang.setText("Tình trạng: Đã chấp nhận");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
*/
    public void update(final String ma, final String mb,  final String bansd)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlupdategoimon+"?magoimon="+ma+"&tongtien="+mb+"&bansd="+bansd,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("true")) {
                            Toast.makeText(ThanhToanActivity.this, "update!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ThanhToanActivity.this, "Loi" + response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThanhToanActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tongtien",mb);
                params.put("magoimon", ma);
                params.put("bansd", bansd);
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }



    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ThanhToanActivity.this.getMenuInflater().inflate(R.menu.xoamonan, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        hinha = thanhToanDTOList.get(vitri).getHinhAnh();
        mamonan = thanhToanDTOList.get(vitri).getMaMonAn();
        magoi = thanhToanDTOList.get(vitri).getMaGoiMon();


        switch (id){

            case R.id.itXoa:
                xoa();
                ;break;

        }
        return super.onContextItemSelected(item);
    }

    public void xoa()
    {

        RequestQueue requestQueue = Volley.newRequestQueue(ThanhToanActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.domain +"android/xoamonthanhtoan.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(ThanhToanActivity.this, "mamonan"+ mamonan+ "-magoi"+ magoi+ response, Toast.LENGTH_LONG).show();
                        listBA.clear();
                        getthanhtoan(magoimon);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThanhToanActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("magoimon",String.valueOf(mamonan));
                params.put("mamonan",String.valueOf(magoi));

                return params;
            }
        };

        requestQueue.add(stringRequest);
        tinhtrang.setText("Tình trạng: Chưa gửi nhà bếp");
    }

    public void laymagoimon(final String a, final String b)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(ThanhToanActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urllaymagoimon+"?maban="+maban,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mgm= response;
                        try{
                            magoimon = Integer.parseInt(mgm);
                            //Toast.makeText(ThanhToanActivity.this, "magoimon"+ magoimon, Toast.LENGTH_SHORT).show();
                            getthanhtoan(magoimon);
                        }
                        catch (Exception ex)
                        {
                            btnGuiBep.setEnabled(false);
                            btnchat.setEnabled(false);
                            btninhoadon.setEnabled(false);
                            btnThanhToan.setEnabled(false);
                            Toast.makeText(ThanhToanActivity.this, "Bàn này chưa gọi món!", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ThanhToanActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("magoimon",a);
                params.put("tinhtrang",b);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    public void getthanhtoan(int magm){
        listBA.clear();
        String URL_GET_PRODUCT=config.domain +"android/getthanhtoan.php?magoimon="+magm;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            //Toast.makeText(ThanhToanActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                ThanhToanDTO product = new ThanhToanDTO();
                                product.setHinhAnh(item.getString("hinhanh"));
                                product.setMaGoiMon(item.getInt("magoimon"));
                                product.setMaMonAn(item.getInt("mamonan"));
                                product.setTenMonAn(item.getString("tenmon"));
                                product.setSoLuong(item.getInt("soluong"));
                                product.setGiaTien(item.getInt("giatien"));
                                listBA.add(product);
                            }
                            thanhToanDTOList=listBA;
                            HienThiThanhToan();


                        } catch (Exception ex) {
                            Toast.makeText(ThanhToanActivity.this, " x"+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                        //                  loading.dismiss();

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThanhToanActivity.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
    public void xoaban()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(ThanhToanActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.domain +"android/xoabanan.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(ThanhToanActivity.this, "Del"+ response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThanhToanActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESQUEST_CODE_TT){
            if(resultCode == Activity.RESULT_OK){
                Intent intent = data;
                tkhachdua = intent.getStringExtra("khachdua");
                tthoilai=intent.getStringExtra("tienthoi");
                update(String.valueOf(magoimon), String.valueOf(tongtien), tenban);
                try{
                    mData.child("BanAn").child(tenban).removeValue();
                }
                catch (Exception ex)
                {

                }
                xoaban();

                Intent iTrangChu = new Intent(ThanhToanActivity.this, home.class);
                startActivity(iTrangChu);
                //Toast.makeText(ThanhToanActivity.this, "kq"+ tkhachdua+ " tt"+tthoilai, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void HienThiThanhToan(){
        tongtien=0;
        //Toast.makeText(ThanhToanActivity.this, "magoimon+ "+ magoimon + "list"+ listBA.size(), Toast.LENGTH_SHORT).show();
                //goiMonDAO.LayMaGoiMonTheoMaBan(maban,"false");

                //goiMonDAO.LayDanhSachMonAnTheoMaGoiMon(magoimon);
        {


            for (int i=0; i < thanhToanDTOList.size() ; i++){
                int soluong = thanhToanDTOList.get(i).getSoLuong();
                int giatien = thanhToanDTOList.get(i).getGiaTien();

                tongtien += (soluong*giatien); // tongtien = tongtien + (soluong*giatien)
            }
            Locale localeEN = new Locale("en", "EN");
            NumberFormat en = NumberFormat.getInstance(localeEN);
            long longNumber = tongtien;
            ttgui=String.valueOf(tongtien);
            String str1 = en.format(longNumber);
            txtTongTien.setText(getResources().getString(R.string.tongcong) + str1 + " VND");
        }


        adapterHienThiThanhToan = new AdapterHienThiThanhToan(this,R.layout.htthanhtoan,listBA);
        gridView.setAdapter(adapterHienThiThanhToan);
        adapterHienThiThanhToan.notifyDataSetChanged();
    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id){
//            case R.id.btnThanhToan: {
//                boolean kiemtra = banAnDAO.XoaBanAnTheoMa(maban);
//                if(kiemtra){
//                    Toast.makeText(this, "Xong!!", Toast.LENGTH_SHORT).show();
//                    Intent iTrangChu = new Intent(ThanhToanActivity.this, home.class);
//                    startActivity(iTrangChu);
//                    //gửi bảng thống kê nữa
//                }else {
//                    Toast.makeText(this, "Lỗi!", Toast.LENGTH_SHORT).show();
//                }
//                ;break;
//            }
//            case  R.id.btnGuiBep: {
//                Toast.makeText(this, "Gửi nhà bếp", Toast.LENGTH_SHORT).show();
//                break;
//
//                /*
//                code
//                 */
//            }
//        }
//    }
}
