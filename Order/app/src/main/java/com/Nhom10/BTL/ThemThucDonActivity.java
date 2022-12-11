package com.Nhom10.BTL;

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
import android.widget.ArrayAdapter;
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
import com.Nhom10.BTL.CustomAdapter.AdapterHienThiLoaiMonAn;
import com.Nhom10.BTL.CustomAdapter.Custom_Adapter_LH;
import com.Nhom10.BTL.DAO.LoaiMonAnDAO;
import com.Nhom10.BTL.DAO.MonAnDAO;
import com.Nhom10.BTL.DTO.LoaiMonAnDTO;
import com.Nhom10.BTL.DTO.MonAnDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemThucDonActivity extends AppCompatActivity implements View.OnClickListener {

    public static int REQUEST_CODE_THEMLOAITHUCDON = 113;
    public static int REQUEST_CODE_MOHINH = 123;
    ImageButton imThemLoaiThucDon;
    public String calogy="a";
    Spinner spinLoaiThucDon;
    TextView txtTieuDe;
    LoaiMonAnDAO loaiMonAnDAO;
    MonAnDAO monAnDAO;
    public String maloai="0";
    public String tenloai="";
    public int n;
    List<LoaiMonAnDTO> loaiMonAnDTOs;
    AdapterHienThiLoaiMonAn adapterHienThiLoaiMonAn;
    ImageView imHinhThucDon;
    Button btnDongYThemMonAn, btnThoatThemMonAn;
    String sDuongDanHinh;
    EditText edTenMonAn,edGiaTien;
    String urlgetml=config.domain +"android/getloaimon.php";
    String urlupanh= config.domain + "android/themhinhloaimon.php";
    final List<String> list = new ArrayList<String>();



    public void get(String url) {
        if(tenloai.length()!=0){
            RequestQueue requestQueue = Volley.newRequestQueue(ThemThucDonActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            maloai="";
                            Toast.makeText(ThemThucDonActivity.this, " lay ma ve:"+response, Toast.LENGTH_SHORT).show();
                            maloai=response;

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(ThemThucDonActivity.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

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
            //Toast.makeText(ThemThucDonActivity.this,"Chưa lấy được mã loại",Toast.LENGTH_SHORT).show();
        }

    }


    public void Themhinhloaimon()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(ThemThucDonActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupanh,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(ThemThucDonActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThemThucDonActivity.this, "er"+ error, Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maloai",maloai);
                params.put("hinhanh",sDuongDanHinh);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    public void getid(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, config.domain +"android/getidmon.php", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                maloai=item.getString("maloai");
                            }
                        } catch (Exception ex) {
                            Toast.makeText(ThemThucDonActivity.this, "error getid "+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThemThucDonActivity.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(ThemThucDonActivity.this);
        requestQueue.add(request);
    }

    public void Loadloaihinh()
    {

        Custom_Adapter_LH custom_adapter_lh= new Custom_Adapter_LH(this, (ArrayList<LoaiMonAnDTO>) listLM);

        if (spinLoaiThucDon != null) {
            spinLoaiThucDon.setAdapter(custom_adapter_lh);
            spinLoaiThucDon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    LoaiMonAnDTO items = (LoaiMonAnDTO) parent.getSelectedItem();
                    tenloai=items.getTenLoai();
                    get(urlgetml);
                    //maloaihinh = items.getMaloaihinh();
                    //Toast.makeText(DangBai.this, "ma loai hinh"+ maloaihinh, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }
    public void show()
    {
        String[] mang= new String[n];
        for( int i=0; i<n; i++)
        {
            mang[i]=list.get(i);
        }

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, mang);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinLoaiThucDon.setAdapter(aa);
        spinLoaiThucDon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tenloai=parent.getItemAtPosition(position).toString();
                get(urlgetml);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               // Toast.makeText(ThemThucDonActivity.this, "da vao day", Toast.LENGTH_SHORT).show();
              show();

            }
        });
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }
    String url=config.domain +"android/themthucdon.php";
    DatabaseReference mData;
    FirebaseStorage storage = FirebaseStorage.getInstance();




    public List<LoaiMonAnDTO> listLM = new ArrayList<LoaiMonAnDTO>();
    String URL_GET_PRODUCT=config.domain +"android/getloaimon.php";
    public List<LoaiMonAnDTO> LoadLoaiMonSpiner(){
        listLM.clear();
        maloai="";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(ThemThucDonActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            //Toast.makeText(ThemThucDonActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                LoaiMonAnDTO product = new LoaiMonAnDTO();
                                product.setMaLoai(item.getInt("maloai"));
                                product.setTenLoai(item.getString("tenloai"));
                                //calogy+=item.getString("tenloai")+ " - ";
                                Toast.makeText(ThemThucDonActivity.this, "dmm>>>"+ item.getString("tenloai"), Toast.LENGTH_LONG).show();

                                listLM.add(product);
                            }
                            list.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                list.add(item.getString("tenloai"));
                            }
                            get(urlgetml);
                            n=list.size();
                            Loadloaihinh();
                            //show();


                        } catch (Exception ex) {
                            Toast.makeText(ThemThucDonActivity.this, ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThemThucDonActivity.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(ThemThucDonActivity.this);
        requestQueue.add(request);
        return listLM;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadLoaiMonSpiner();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themthucdon);
        mData= FirebaseDatabase.getInstance().getReference();
//        final StorageReference reference=storage.getReferenceFromUrl("https://smartorder-13eb1.firebaseio.com/");

        loaiMonAnDAO = new LoaiMonAnDAO(this);
        monAnDAO = new MonAnDAO(this);

        imThemLoaiThucDon = (ImageButton) findViewById(R.id.imThemLoaiThucDon);
        spinLoaiThucDon = (Spinner) findViewById(R.id.spinLoaiMonAn);
        imHinhThucDon = (ImageView) findViewById(R.id.imHinhThucDon);
        btnDongYThemMonAn = (Button) findViewById(R.id.btnDongYThemMonAn);
        btnThoatThemMonAn = (Button) findViewById(R.id.btnThoatThemMonAn);
        edTenMonAn = (EditText) findViewById(R.id.edThemTenMonAn);
        edGiaTien = (EditText) findViewById(R.id.edThemGiaTien);
        txtTieuDe= findViewById(R.id.txttd);
        imThemLoaiThucDon.setOnClickListener(this);
        imHinhThucDon.setOnClickListener(this);
        btnDongYThemMonAn.setOnClickListener(this);
        btnThoatThemMonAn.setOnClickListener(this);

        spinLoaiThucDon.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ThemThucDonActivity.this, "Xoa", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
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
        adapterHienThiLoaiMonAn = new AdapterHienThiLoaiMonAn(ThemThucDonActivity.this,R.layout.custom_layout_spinloaithucdon,loaiMonAnDTOs);
        spinLoaiThucDon.setAdapter(adapterHienThiLoaiMonAn);
        spinLoaiThucDon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String xx  = String.valueOf(parent.getAdapter().getItem(position));
                Toast.makeText(ThemThucDonActivity.this, "Ten loai"+ xx, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/

    public void themthucdon(String url) {
        if(edTenMonAn.length()!=0 && edGiaTien.length()!=0){
            RequestQueue requestQueue = Volley.newRequestQueue(ThemThucDonActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("true")){
                                Toast.makeText(ThemThucDonActivity.this, "Thêm món thành công!", Toast.LENGTH_LONG).show();
                            }
                            else if(response.trim().equals("already"))
                            {
                                Toast.makeText(ThemThucDonActivity.this, "Đã có món ăn này rồi! Vui lòng chọn tên khác", Toast.LENGTH_LONG).show();

                            }
                            else{
                                Toast.makeText(ThemThucDonActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(ThemThucDonActivity.this, "Bạn đã chọn hình cho món ăn chưa? Nhấn lần nữa để xác nhận", Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("tenmon",edTenMonAn.getText().toString().trim());
                    params.put("mal",maloai);
                    params.put("giatien",edGiaTien.getText().toString().trim());
                    params.put("hinhanh",sDuongDanHinh.trim());
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }else {
            Toast.makeText(ThemThucDonActivity.this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.imThemLoaiThucDon:
                Intent iThemLoaiMonAn = new Intent(ThemThucDonActivity.this,ThemLoaiThucDonActivity.class);
                startActivityForResult(iThemLoaiMonAn,REQUEST_CODE_THEMLOAITHUCDON);
                break;


            case R.id.imHinhThucDon:
                Intent iMoHinh = new Intent();
                iMoHinh.setType("image/*");
                iMoHinh.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(iMoHinh, "Chọn hình thực đơn"), REQUEST_CODE_MOHINH);
                ;break;

            case R.id.btnDongYThemMonAn:
                int vitri = spinLoaiThucDon.getSelectedItemPosition();
                //String tenmon= spinLoaiThucDon.getSelectedItem().toString();
                //Toast.makeText(ThemThucDonActivity.this, "tenloai"+ calogy, Toast.LENGTH_SHORT).show();
                final  String tenmonan = edTenMonAn.getText().toString();
                final String giatien = edGiaTien.getText().toString();

                if(tenmonan != null && giatien != null && !tenmonan.equals("") && !giatien.equals("") ){
                    MonAnDTO monAnDTO = new MonAnDTO();
                    monAnDTO.setGiaTien(giatien);
                    monAnDTO.setHinhAnh(sDuongDanHinh);
                    monAnDTO.setMaLoai(Integer.parseInt(maloai));
                    monAnDTO.setTenMonAn(tenmonan);
                    boolean kiemtra = monAnDAO.ThemMonAn(monAnDTO);
                    if(kiemtra){
                        themthucdon(url);
                        Themhinhloaimon();
                        //Toast.makeText(this,getResources().getString(R.string.themthanhcong),Toast.LENGTH_SHORT).show();
                    }else{
                        //Toast.makeText(this,getResources().getString(R.string.themthatbai),Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(this,getResources().getString(R.string.loithemmonan),Toast.LENGTH_SHORT).show();
                }


                ;break;

            case R.id.btnThoatThemMonAn:
                Intent intent = new Intent(ThemThucDonActivity.this, home.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_THEMLOAITHUCDON){
            LoadLoaiMonSpiner();
            show();
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
                        Toast.makeText(ThemThucDonActivity.this, "Loi upload hinh", Toast.LENGTH_SHORT).show();
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
                                                //Toast.makeText(ThemThucDonActivity.this, imageUrl, Toast.LENGTH_SHORT).show();
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
