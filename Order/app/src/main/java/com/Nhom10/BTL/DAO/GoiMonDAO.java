package com.Nhom10.BTL.DAO;

import android.content.ContentValue;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.Nhom10.BTL.DTO.ChiTietGoiMonDTO;
import com.Nhom10.BTL.DTO.GoiMonDTO;
import com.Nhom10.BTL.DTO.ThanhToanDTO;
import com.Nhom10.BTL.Database.CreateDatabase;
import com.Nhom10.BTL.config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoiMonDAO extends AppCompatActivity {

    SQLiteDatabase database;
    String urlgoimon= config.domain +"android/goimon.php";
    String urlcnhat=config.domain +"android/capnhattt.php";

    public GoiMonDAO(Context context){
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public long ThemGoiMon(GoiMonDTO goiMonDTO){
        ContentValues contentValues = new ContentValues();

        contentValues.put(CreateDatabase.TB_GOIMON_MABAN,goiMonDTO.getMaBan());
        contentValues.put(CreateDatabase.TB_GOIMON_MANV,goiMonDTO.getMaNV());
        contentValues.put(CreateDatabase.TB_GOIMON_NGAYGOI,goiMonDTO.getNgayGoi());
        contentValues.put(CreateDatabase.TB_GOIMON_TINHTRANG, goiMonDTO.getTinhTrang());

        long magoimon = database.insert(CreateDatabase.TB_GOIMON,null,contentValues);

        return magoimon;
    }

    public long LayMaGoiMonTheoMaBan(int maban,String tinhtrang){
        String truyvan = "SELECT * FROM " + CreateDatabase.TB_GOIMON + " WHERE " + CreateDatabase.TB_GOIMON_MABAN + " = '" + maban + "' AND "
                + CreateDatabase.TB_GOIMON_TINHTRANG + " = '" + tinhtrang + "'";

        long magoimon = 0;
        Cursor cursor = database.rawQuery(truyvan,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            magoimon = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_GOIMON_MAGOIMON));

            cursor.moveToNext();
        }

        return magoimon;
    }

    public boolean KiemTraMonAnDaTonTai(int magoimon, int mamonan){
        String truyvan = "SELECT * FROM " + CreateDatabase.TB_CHITIETGOIMON + " WHERE " + CreateDatabase.TB_CHITIETGOIMON_MAMONAN
                + " = " + mamonan + " AND " + CreateDatabase.TB_CHITIETGOIMON_MAGOIMON + " = " + magoimon;

        Cursor cursor = database.rawQuery(truyvan,null);
        if(cursor.getCount() != 0){
            return true;
        }else{
            return false;
        }
    }

    public int LaySoLuongMonAnTheoMaGoiMon(int magoimon,int mamonan){
        int soluong = 0;
        String truyvan = "SELECT * FROM " + CreateDatabase.TB_CHITIETGOIMON + " WHERE " + CreateDatabase.TB_CHITIETGOIMON_MAMONAN
                + " = " + mamonan + " AND " + CreateDatabase.TB_CHITIETGOIMON_MAGOIMON + " = " + magoimon;
        Cursor cursor = database.rawQuery(truyvan,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            soluong = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_CHITIETGOIMON_SOLUONG));

            cursor.moveToNext();
        }

        return soluong;
    }

    public boolean CapNhatSoLuong(ChiTietGoiMonDTO chiTietGoiMonDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_CHITIETGOIMON_SOLUONG, chiTietGoiMonDTO.getSoLuong());

        long kiemtra = database.update(CreateDatabase.TB_CHITIETGOIMON,contentValues,CreateDatabase.TB_CHITIETGOIMON_MAGOIMON + " = " + chiTietGoiMonDTO.getMaGoiMon()
                + " AND " + CreateDatabase.TB_CHITIETGOIMON_MAMONAN + " = " + chiTietGoiMonDTO.getMaMonAn(),null );

        if (kiemtra != 0){
            return true;
        }else{
            return false;
        }
    }

    public boolean ThemChiTietGoiMon(ChiTietGoiMonDTO chiTietGoiMonDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_CHITIETGOIMON_SOLUONG,chiTietGoiMonDTO.getSoLuong());
        contentValues.put(CreateDatabase.TB_CHITIETGOIMON_MAGOIMON, chiTietGoiMonDTO.getMaGoiMon());
        contentValues.put(CreateDatabase.TB_CHITIETGOIMON_MAMONAN,chiTietGoiMonDTO.getMaMonAn());

        long kiemtra = database.insert(CreateDatabase.TB_CHITIETGOIMON, null, contentValues);

        if (kiemtra != 0){
            return true;
        }else{
            return false;
        }
    }


    public List<ThanhToanDTO> getthanhtoan(int magm){
        final List<ThanhToanDTO> listBA = new ArrayList<ThanhToanDTO>();
        String URL_GET_PRODUCT=config.domain +"android/getthanhtoan.php?magoimon="+magm;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            //Toast.makeText(ThanhToanActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            // Convert json array to jsonobject
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                ThanhToanDTO product = new ThanhToanDTO();
                                product.setTenMonAn(item.getString("TENMON"));
                                product.setSoLuong(item.getInt("SOLUONG"));
                                product.setGiaTien(item.getInt("GIATIEN"));
                                listBA.add(product);
                            }

                        } catch (Exception ex) {
//                            Toast.makeText(getActivity(), ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                        //                  loading.dismiss();

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ThanhToanActivity.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
        return listBA;

    }



    public List<ThanhToanDTO> LayDanhSachMonAnTheoMaGoiMon(int magoimon){
        String truyvan = "SELECT * FROM " + CreateDatabase.TB_CHITIETGOIMON + " ct," + CreateDatabase.TB_MONAN + " ma WHERE "
                + "ct." + CreateDatabase.TB_CHITIETGOIMON_MAMONAN + " = ma." + CreateDatabase.TB_MONAN_MAMON
                + " AND " + CreateDatabase.TB_CHITIETGOIMON_MAGOIMON + " = '" + magoimon + "'";

        List<ThanhToanDTO> thanhToanDTOs = new ArrayList<ThanhToanDTO>();
        Cursor cursor = database.rawQuery(truyvan,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ThanhToanDTO thanhToanDTO = new ThanhToanDTO();
            thanhToanDTO.setSoLuong(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_CHITIETGOIMON_SOLUONG)));
            thanhToanDTO.setGiaTien(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_MONAN_GIATIEN)));
            thanhToanDTO.setTenMonAn(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_MONAN_TENMONAN)));

            thanhToanDTOs.add(thanhToanDTO);

            cursor.moveToNext();
        }

        return thanhToanDTOs;
    }

    public boolean CapNhatTrangThaiGoiMonTheoMaBan(int maban,String tinhtrang){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_GOIMON_TINHTRANG,maban);

        long kiemtra = database.update(CreateDatabase.TB_GOIMON,contentValues,CreateDatabase.TB_GOIMON_MABAN + " = '" + maban + "'",null);
        if(kiemtra != 0){
            return true;
        }else{
            return false;
        }
    }

    public void capnhattinhtrang(final int mb)
    {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlcnhat,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("true")){
                                Toast.makeText(GoiMonDAO.this, "update", Toast.LENGTH_SHORT).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(GoiMonDAO.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("maban",String.valueOf(mb));

                    return params;
                }
            };

            requestQueue.add(stringRequest);
    }
    public void goimon(final String mb, final String manv, final String ng, final String tt)
    {
        //Toast.makeText(this, "vao ham goi mon", Toast.LENGTH_LONG).show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlgoimon,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("true")){
                            Toast.makeText(GoiMonDAO.this, "Them goi mon", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(GoiMonDAO.this, "Kết nối sever thất bại! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maban",mb);
                params.put("manv",manv);
                params.put("ngaygoi",ng);
                params.put("tinhtrang",tt);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


}
