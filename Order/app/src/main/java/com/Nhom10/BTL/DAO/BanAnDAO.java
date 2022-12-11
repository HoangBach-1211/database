package com.Nhom10.BTL.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.Nhom10.BTL.Database.CreateDatabase;

public class BanAnDAO extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }
    public String key;

    SQLiteDatabase database;

    public BanAnDAO(Context context){
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();

    }

    public boolean ThemBanAn(String tenban){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_BANAN_TENBAN,tenban);
        contentValues.put(CreateDatabase.TB_BANAN_TINHTRANG,"false");

        long kiemtra = database.insert(CreateDatabase.TB_BANAN,null,contentValues);
        if(kiemtra != 0){
            return true;
        }else{
            return false;
        }
    }


   /* List<BanAnDTO> listBA = new A rrayList<BanAnDTO>();

    String URL_GET_PRODUCT="http://192.168.0.101:8080/android/getalltable.php";
    public List<BanAnDTO> LayTatCaBanAn(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            // Convert json array to jsonobject
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                BanAnDTO product = new BanAnDTO();
                                product.setMaBan(item.getInt("maban"));
                                product.setTenBan(item.getString("tenban"));
                                product.setDuocChon(item.getBoolean("tinhtrang"));
                                listBA.add(product);
                            }
                            //HienThiDanhSachBanAn();

                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), "loi"+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "lll"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
        return listBA;

    }
*/


  /*public List<BanAnDTO> LayTatCaBanAn(){
        /*List<BanAnDTO> banAnDTOList = new ArrayList<BanAnDTO>();
        String truyvan = "SELECT * FROM " + CreateDatabase.TB_BANAN;
        Cursor cursor = database.rawQuery(truyvan,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            BanAnDTO banAnDTO = new BanAnDTO();
            banAnDTO.setMaBan(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_BANAN_MABAN)));
            banAnDTO.setTenBan(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_BANAN_TENBAN)));
            banAnDTOList.add(banAnDTO);
            cursor.moveToNext();
        }
        return ShowTable.LayTatCaBanAn();
    }*/

    public String LayTinhTrangBanTheoMa(int maban){
        String tinhtrang = "";
        String truyvan = "SELECT * FROM " + CreateDatabase.TB_BANAN + " WHERE " + CreateDatabase.TB_BANAN_MABAN + " = '" + maban + "'";
        Cursor cursor = database.rawQuery(truyvan,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            tinhtrang = cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_BANAN_TINHTRANG));
            cursor.moveToNext();
        }
        return tinhtrang;
    }
    public String LayTenBanAn(int maban){
        String ten = "";
        String truyvan = "SELECT * FROM " + CreateDatabase.TB_BANAN + " WHERE " + CreateDatabase.TB_BANAN_MABAN + " = '" + maban + "'";
        Cursor cursor = database.rawQuery(truyvan,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            ten = cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_BANAN_TENBAN));
            cursor.moveToNext();
        }
        return ten;
    }

    public boolean XoaBanAnTheoMa(int maban){
        long kiemtra = database.delete(CreateDatabase.TB_BANAN,CreateDatabase.TB_BANAN_MABAN + " = " + maban,null);
        if(kiemtra !=0){
            return true;
        }else{
            return false;
        }
    }

    public boolean CapNhatLaiTenBan(int maban,String tenban){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_BANAN_TENBAN,tenban);

        long kiemtra = database.update(CreateDatabase.TB_BANAN, contentValues, CreateDatabase.TB_BANAN_MABAN + " = '" + maban + "'", null);

        if(kiemtra !=0){
            return true;
        }else{
            return false;
        }
    }

    public boolean CapNhatLaiTinhTrangBan(int maban,String tinhtrang){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_BANAN_TINHTRANG,tinhtrang);

        long kiemtra = database.update(CreateDatabase.TB_BANAN,contentValues,CreateDatabase.TB_BANAN_MABAN + " = '" + maban + "'",null );

        if(kiemtra !=0){
            return true;
        }else{
            return false;
        }
    }




}
