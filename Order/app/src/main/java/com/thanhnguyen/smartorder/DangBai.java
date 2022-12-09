package com.thanhnguyen.smartorder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thanhnguyen.smartorder.DAO.LoaiMonAnDAO;
import com.thanhnguyen.smartorder.DAO.MonAnDAO;
import com.thanhnguyen.smartorder.DTO.HuyenDTO;
import com.thanhnguyen.smartorder.DTO.LoaiHinhDTO;
import com.thanhnguyen.smartorder.DTO.TinhDTO;
import com.thanhnguyen.smartorder.DTO.XaDTO;
import com.thanhnguyen.smartorder.TimTro.CustomAdapter;
import com.thanhnguyen.smartorder.TimTro.Custom_Adapter_Huyen;
import com.thanhnguyen.smartorder.TimTro.Custom_Adapter_LH;
import com.thanhnguyen.smartorder.TimTro.Custom_Adapter_Xa;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DangBai extends AppCompatActivity implements View.OnClickListener{

    public static int REQUEST_CODE_THEMLOAITHUCDON = 113;
    public static int REQUEST_CODE_MOHINH = 123;
    ImageButton ti1, ti2, ti3, ti4,ti5,ti6;
    int flag=1;
    String matinh,mahuyen,maxa, maloaihinh="1";
    private Spinner customSpinner;
    String tendn="";
    private  Spinner spinhuyen, spinxa, spinloai;
    private boolean wc=false, dieuhoa=false, chodexe=false,wifi=false,chonauan=false,anninh=false;
    public String calogy="a";
    Spinner spinLoaiThucDon;
    TextView txtTieuDe;
    LoaiMonAnDAO loaiMonAnDAO;
    MonAnDAO monAnDAO;
    public String maloai="0";
    public String tenloai="";
    ImageView imHinhThucDon;
    Button btnDongYThemMonAn, btnThoatThemMonAn;
    String sDuongDanHinh;
    EditText edmaquan,edtenquan,edmota;
    String urlgetml= config.domain +"android/getidmon.php";
    String urlupanh= config.domain + "android/themhinhloaimon.php";
    final List<String> list = new ArrayList<String>();



    public void get(String url) {
        if(tenloai.length()!=0){
            RequestQueue requestQueue = Volley.newRequestQueue(DangBai.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            maloai="";
                            //Toast.makeText(DangBai.this, " lay ma ve:"+response, Toast.LENGTH_SHORT).show();
                            maloai=response;

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(DangBai.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("tenloai",tenloai);
                    params.put("maloai", maloai);
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }else {
            //Toast.makeText(DangBai.this,"Chưa lấy được mã loại",Toast.LENGTH_SHORT).show();
        }

    }







    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }
    String url= config.domain +"android/thembaidang.php";
    DatabaseReference mData;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public List<TinhDTO> listLM = new ArrayList<TinhDTO>();
    String URL_GET_PRODUCT= config.domain +"android/gettinhtp.php";
    public List<TinhDTO> LoadLoaiMonSpiner(){
        listLM.clear();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(DangBai.this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            //Toast.makeText(DangBai.this, response.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                TinhDTO product = new TinhDTO();
                                product.setMatinh(item.getString("matinh"));
                                product.setTentinh(item.getString("tentinh"));
                                listLM.add(product);
                            }
                            Loadtinh();


                        } catch (Exception ex) {
                            Toast.makeText(DangBai.this, ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DangBai.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(DangBai.this);
        requestQueue.add(request);
        return listLM;

    }


    public void Loadtinh()
    {
        CustomAdapter customAdapter = new CustomAdapter(this, (ArrayList<TinhDTO>) listLM);

        if (customSpinner != null) {
            customSpinner.setAdapter(customAdapter);
            customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    TinhDTO items = (TinhDTO) parent.getSelectedItem();
                    matinh=items.getMatinh();
                    Toast.makeText(DangBai.this, "matinh"+ matinh, Toast.LENGTH_SHORT).show();
                    LoadHuyen(matinh);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public List<HuyenDTO> listHuyen = new ArrayList<HuyenDTO>();
    public List<HuyenDTO> LoadHuyen(String mt){
        String URL_GET_HUYEN= config.domain +"android/gethuyen.php?matinh="+mt;
        listHuyen.clear();
        maloai="";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_HUYEN, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(DangBai.this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            //Toast.makeText(DangBai.this, response.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                HuyenDTO product = new HuyenDTO();
                                product.setMahuyen(item.getString("mahuyen"));
                                product.setTenhuyen(item.getString("tenhuyen"));
                                //calogy+=item.getString("tenloai")+ " - ";
                                //Toast.makeText(DangBai.this, "dmm>>>"+ item.getString("tenloai"), Toast.LENGTH_LONG).show();

                                listHuyen.add(product);
                            }
                            Loadhuyen();


                        } catch (Exception ex) {
                            Toast.makeText(DangBai.this, ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DangBai.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(DangBai.this);
        requestQueue.add(request);
        return listHuyen;

    }

    public void Loadhuyen()
    {

        Custom_Adapter_Huyen  custom_adapter_huyen= new Custom_Adapter_Huyen(this, (ArrayList<HuyenDTO>) listHuyen);

        if (spinhuyen != null) {
            spinhuyen.setAdapter(custom_adapter_huyen);
            spinhuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    HuyenDTO items = (HuyenDTO) parent.getSelectedItem();
                    mahuyen = items.getMahuyen();
                    LoadXa(mahuyen);
                    //Toast.makeText(DangBai.this, "ma huyen"+ mahuyen, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }




    public List<XaDTO> listxa = new ArrayList<XaDTO>();
    public List<XaDTO> LoadXa(String mh){
        listxa.clear();
        //Toast.makeText(this, "mahuyenxxx"+ mahuyen, Toast.LENGTH_SHORT).show();
        String URL_GET_HUYEN= config.domain +"android/getxa.php?mahuyen="+mh;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_HUYEN, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(DangBai.this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            //Toast.makeText(DangBai.this, response.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                XaDTO product = new XaDTO();
                                product.setMaxa(item.getString("maxa"));
                                product.setTenxa(item.getString("tenxa"));
                                listxa.add(product);
                            }
                            Loadxa();


                        } catch (Exception ex) {
                            Toast.makeText(DangBai.this, ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DangBai.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(DangBai.this);
        requestQueue.add(request);
        return listxa;

    }

    public void Loadxa()
    {

        Custom_Adapter_Xa custom_adapter_xa= new Custom_Adapter_Xa(this, (ArrayList<XaDTO>) listxa);

        if (spinxa != null) {
            spinxa.setAdapter(custom_adapter_xa);
            spinxa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    XaDTO items = (XaDTO) parent.getSelectedItem();
                    maxa = items.getMaxa();
                    //Toast.makeText(DangBai.this, "ma xa"+ maxa, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }



    public List<LoaiHinhDTO> listLoai = new ArrayList<LoaiHinhDTO>();
    public List<LoaiHinhDTO> LoadLoaiHinh(){
        String URL_GET_HUYEN= config.domain +"android/getloaihinh.php";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_HUYEN, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(DangBai.this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            //Toast.makeText(DangBai.this, response.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                LoaiHinhDTO product = new LoaiHinhDTO();
                                product.setMaloaihinh(item.getString("maloaihinh"));
                                product.setTenloai(item.getString("tenloaihinh"));
                                product.setHinhLoai(item.getString("hinhanhlh"));
                                listLoai.add(product);
                            }
                            Loadloaihinh();


                        } catch (Exception ex) {
                            Toast.makeText(DangBai.this, ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DangBai.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(DangBai.this);
        requestQueue.add(request);
        return listLoai;

    }


    public void Loadloaihinh()
    {

        Custom_Adapter_LH custom_adapter_lh= new Custom_Adapter_LH(this, (ArrayList<LoaiHinhDTO>) listLoai);

        if (spinloai != null) {
            spinloai.setAdapter(custom_adapter_lh);
            spinloai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    LoaiHinhDTO items = (LoaiHinhDTO) parent.getSelectedItem();
                    maloaihinh = items.getMaloaihinh();
                    int xx= Integer.parseInt(maloaihinh);
                    loadicon(xx);
                    //Toast.makeText(DangBai.this, "ma loai hinh"+ maloaihinh, Toast.LENGTH_SHORT).show();
                }

                private void loadicon(int xx) {
                    //Toast.makeText(DangBai.this, xx+ "ii"  , Toast.LENGTH_SHORT).show();
                    switch (xx)
                    {

                        case 5 :
                        {

                            ti1.setImageResource(R.drawable.beo);
                            flag=2;
                            break;
                        }
                        case 1 :
                        {

                            ti1.setImageResource(R.drawable.wc);
                            flag=1;
                            break;
                        }

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadLoaiMonSpiner();
        LoadLoaiHinh();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dang_bai);
        Intent intent = getIntent();
        tendn = intent.getStringExtra("tendn");
        mData= FirebaseDatabase.getInstance().getReference();
        customSpinner = findViewById(R.id.spinTinh);
        spinhuyen= findViewById(R.id.spinHuyen);
        spinxa= findViewById(R.id.spinXa);
        spinloai= findViewById(R.id.loaiHinh);
        ti1= findViewById(R.id.ti1);
        loaiMonAnDAO = new LoaiMonAnDAO(this);
        monAnDAO = new MonAnDAO(this);



        imHinhThucDon = (ImageView) findViewById(R.id.imHinhThucDon);
        btnDongYThemMonAn = (Button) findViewById(R.id.btnDongYThemMonAn);
        btnThoatThemMonAn = (Button) findViewById(R.id.btnThoatThemMonAn);
        edmaquan = (EditText) findViewById(R.id.edMaquan);
        edtenquan = (EditText) findViewById(R.id.edTenQuan);
        edmota= findViewById(R.id.motachitiet);
        imHinhThucDon.setOnClickListener(this);
        btnDongYThemMonAn.setOnClickListener(this);
        btnThoatThemMonAn.setOnClickListener(this);
        ti1.setOnClickListener(this);






        /*spinLoaiThucDon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calogy=parent.getOnItemClickListener().toString();
                ((TextView) view).setTextColor(Color.RED);
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                show();
            }
        });*/

    }







    //spinLoaiThucDon.OnItemClickListener(new )
  /*  private void HienThiSpinnerLoaiMonAn (){
        loaiMonAnDTOs = LoadLoaiMonSpiner();
        adapterHienThiLoaiMonAn = new AdapterHienThiLoaiMonAn(DangBai.this,R.layout.custom_layout_spinloaithucdon,loaiMonAnDTOs);
        spinLoaiThucDon.setAdapter(adapterHienThiLoaiMonAn);
        spinLoaiThucDon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String xx  = String.valueOf(parent.getAdapter().getItem(position));
                Toast.makeText(DangBai.this, "Ten loai"+ xx, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/

    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    Date today = new Date();
    String ngaygoi = dateFormatter.format(today);


    public void Thembaidang(String url) {
        if(edmaquan.length()!=0 && edtenquan.length()!=0){
            RequestQueue requestQueue = Volley.newRequestQueue(DangBai.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("true")){
                                Toast.makeText(DangBai.this, "Thao tác thành công", Toast.LENGTH_LONG).show();
                            }
                            else if(response.trim().equals("already"))
                            {
                                Toast.makeText(DangBai.this, "Mã nhà hàng/quán ăn này đã có rồi! Vui lòng chọn tên khác", Toast.LENGTH_LONG).show();

                            }
                            else{
                                Toast.makeText(DangBai.this, "Lỗi!" + response, Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(DangBai.this, "Bạn đã chọn hình minh họa chưa? Nhấn lần nữa để xác nhận"+ error, Toast.LENGTH_LONG).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("matinh",matinh);
                    params.put("mahuyen",mahuyen);
                    params.put("maxa",maxa);
                    params.put("maloaihinh",maloaihinh);
                    params.put("tennh",edtenquan.getText().toString().trim());
                    params.put("manh",edmaquan.getText().toString().trim());
                    params.put("hinhanh",sDuongDanHinh.trim());
                    params.put("mota",String.valueOf(edmota.getText().toString()));
                    params.put("hinhanh",sDuongDanHinh);
                    params.put("tendn",tendn);
                    params.put("wc",String.valueOf(wc));
                    params.put("thoigian",String.valueOf(ngaygoi));
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }else {
            Toast.makeText(DangBai.this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (flag==2)
        {
            switch (id){


                case R.id.imHinhThucDon:
                    Intent iMoHinh = new Intent();
                    iMoHinh.setType("image/*");
                    iMoHinh.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(iMoHinh, "Chọn hình thực đơn"), REQUEST_CODE_MOHINH);
                    ;break;

                case R.id.btnDongYThemMonAn:
                    //int vitri = spinLoaiThucDon.getSelectedItemPosition();
                    //String tenmon= spinLoaiThucDon.getSelectedItem().toString();
                    //Toast.makeText(DangBai.this, "tenloai"+ calogy, Toast.LENGTH_SHORT).show();
                    final String tieude = edmaquan.getText().toString();
                    final String giatien = edtenquan.getText().toString();
                    //final String motachitiet= edmota.getText().toString();

                    if(tieude != null && giatien != null && !tieude.equals("") && !giatien.equals("") ){


                        Thembaidang(url);
                        //Toast.makeText(this,getResources().getString(R.string.themthanhcong),Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(this,getResources().getString(R.string.loithemmonan),Toast.LENGTH_SHORT).show();
                    }


                    ;break;

                case R.id.btnThoatThemMonAn:
                    Intent intent = new Intent(DangBai.this, home.class);
                    startActivity(intent);
                    break;
            }
        }
        else
        {
            switch (id){


                case R.id.ti1:
                    if (wc==false)
                    {
                        ti1.setBackgroundResource(R.drawable.duong_vien1);
                        wc=true;
                        Toast.makeText(this, "Giao hàng cho khách ở xa", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if( wc==true)
                    {
                        ti1.setBackgroundResource(R.drawable.duong_vien);
                        wc=false;
                        Toast.makeText(this, "Chỉ bán tại cửa hàng", Toast.LENGTH_SHORT).show();
                        break;
                    }



                case R.id.imHinhThucDon:
                    Intent iMoHinh = new Intent();
                    iMoHinh.setType("image/*");
                    iMoHinh.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(iMoHinh, "Chọn hình nhà hàng"), REQUEST_CODE_MOHINH);
                    ;break;

                case R.id.btnDongYThemMonAn:
                    //int vitri = spinLoaiThucDon.getSelectedItemPosition();
                    //String tenmon= spinLoaiThucDon.getSelectedItem().toString();
                    //Toast.makeText(DangBai.this, "tenloai"+ calogy, Toast.LENGTH_SHORT).show();
                    final String tieude = edmaquan.getText().toString();
                    final String giatien = edtenquan.getText().toString();
                    //final String motachitiet= edmota.getText().toString();

                    if(tieude != null && giatien != null && !tieude.equals("") && !giatien.equals("") ){


                        Thembaidang(url);
                        //Toast.makeText(this,getResources().getString(R.string.themthanhcong),Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(this,getResources().getString(R.string.loithemmonan),Toast.LENGTH_SHORT).show();
                    }


                    ;break;

                case R.id.btnThoatThemMonAn:
                    Intent intent = new Intent(DangBai.this, home.class);
                    startActivity(intent);
                    break;
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_THEMLOAITHUCDON){
            LoadLoaiMonSpiner();
            if(resultCode == Activity.RESULT_OK){
                Intent dulieu = data;
                boolean kiemtra = dulieu.getBooleanExtra("kiemtraloaithucdon",false);
                if(kiemtra){
                    Toast.makeText(this, getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,getResources().getString(R.string.themthatbai),Toast.LENGTH_SHORT).show();
                }
            }
        }else if(REQUEST_CODE_MOHINH == requestCode){
            if(resultCode == Activity.RESULT_OK && data !=null){
                imHinhThucDon.setImageURI(data.getData());
                StorageReference storageRef = storage.getReferenceFromUrl("gs://com.nhom10.com.thanhnguyen.smartorder-13eb1.appspot.com");
                Calendar calendar= Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child("image"+ calendar.getTimeInMillis()+ ".png");

                // Get the data from an ImageView as bytes
                Bitmap bitmap = ((BitmapDrawable) imHinhThucDon.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] dataa = baos.toByteArray();

                final UploadTask uploadTask = mountainsRef.putBytes(dataa);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DangBai.this, "Loi upload hinh", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {
                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageUrl = uri.toString();
                                                sDuongDanHinh=imageUrl;
                                                //MonAnDTO monAnDTO=new MonAnDTO(1,1,String.valueOf(edGiaTien.toString()), String.valueOf(edGiaTien.getText()),String.valueOf(imageUrl));
                                                //myRef.setValue(imageUrl);
                                                //Toast.makeText(DangBai.this, imageUrl, Toast.LENGTH_SHORT).show();
                                                Log.d("cc",imageUrl.toString());
                                            }
                                        });
                                    }
                                }
                            }});
                    }
                });
            }
        }
    }


}
