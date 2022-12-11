package com.Nhom10.BTL.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.Nhom10.BTL.DAO.LoaiMonAnDAO;
import com.Nhom10.BTL.DTO.LoaiMonAnDTO;
import com.Nhom10.BTL.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterHienThiLoaiMonAnThucDon extends BaseAdapter {
    Context context;
    int layout;
    List<LoaiMonAnDTO> loaiMonAnDTOList;
    ViewHolderHienThiLoaiThucDon viewHolderHienThiLoaiThucDon;
    LoaiMonAnDAO loaiMonAnDAO;

    public AdapterHienThiLoaiMonAnThucDon(Context context, int layout, List<LoaiMonAnDTO> loaiMonAnDTOList){
        this.context = context;
        this.layout = layout;
        this.loaiMonAnDTOList = loaiMonAnDTOList;
        loaiMonAnDAO = new LoaiMonAnDAO(context);

    }

    @Override
    public int getCount() {
        return loaiMonAnDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return loaiMonAnDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return loaiMonAnDTOList.get(position).getMaLoai();
    }

    public class ViewHolderHienThiLoaiThucDon{
        ImageView imHinhLoaiThucDon;
        TextView txtTenLoaiThucDon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            viewHolderHienThiLoaiThucDon = new ViewHolderHienThiLoaiThucDon();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,parent,false);

            viewHolderHienThiLoaiThucDon.imHinhLoaiThucDon = (ImageView) view.findViewById(R.id.imHienThiMonAn);
            viewHolderHienThiLoaiThucDon.txtTenLoaiThucDon = (TextView) view.findViewById(R.id.txtTenLoai);

            view.setTag(viewHolderHienThiLoaiThucDon);
        }else{
            viewHolderHienThiLoaiThucDon = (ViewHolderHienThiLoaiThucDon) view.getTag();
        }

        LoaiMonAnDTO loaiMonAnDTO = loaiMonAnDTOList.get(position);
        int maloai = loaiMonAnDTO.getMaLoai();


        String hinhanh = loaiMonAnDTO.getHinhAnh();
        viewHolderHienThiLoaiThucDon.txtTenLoaiThucDon.setText(loaiMonAnDTO.getTenLoai());
        if(hinhanh == null || hinhanh.equals("")){
            viewHolderHienThiLoaiThucDon.imHinhLoaiThucDon.setImageResource(R.drawable.icon);
        }else{

            //Uri uri = Uri.parse(hinhanh);
            //Log.d("dsmaa",hinhanh.toString());
            loadimageinternet(hinhanh,viewHolderHienThiLoaiThucDon.imHinhLoaiThucDon);

            //Toast.makeText(context, hinhanh, Toast.LENGTH_SHORT).show();
            /*viewHolderHienThiDanhSachMonAn.imHinhMonAn.setImageURI(uri);
            // Toast.makeText(context, uri.toString(), Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse(hinhanh);
            viewHolderHienThiLoaiThucDon.txtTenLoaiThucDon.setText(loaiMonAnDTO.getTenLoai());
            viewHolderHienThiLoaiThucDon.imHinhLoaiThucDon.setImageURI(uri);*/
        }
        return view;

    }
    private void loadimageinternet(String url ,ImageView x){
        Picasso.get().load(url).placeholder(R.drawable.icon)
                .fit()
                .into(x, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError(Exception e) {
                    }
                });
    }


    public void loadanhlm(String url) {
       {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("true")){
                            }
                            else{
                                Toast.makeText(context, "Loại món này đã có rồi mà!", Toast.LENGTH_LONG).show();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(context, "Kết nối máy chủ lỗi!" + error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }

    }}
