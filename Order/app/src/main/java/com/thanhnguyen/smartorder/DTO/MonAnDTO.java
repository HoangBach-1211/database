package com.thanhnguyen.smartorder.DTO;
public class MonAnDTO {

    int MaMonAn, MaLoai;
    String TenMonAn;
    String GiaTien;
    String HinhAnh;


    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public MonAnDTO(int maMonAn, int maLoai, String tenMonAn, String giaTien, String hinhAnh) {
        MaMonAn = maMonAn;
        MaLoai = maLoai;
        TenMonAn = tenMonAn;
        GiaTien = giaTien;
        HinhAnh = hinhAnh;
    }

    public MonAnDTO() {
    }

    public int getMaMonAn() {
        return MaMonAn;
    }

    public void setMaMonAn(int maMonAn) {
        MaMonAn = maMonAn;
    }

    public int getMaLoai() {
        return MaLoai;
    }

    public void setMaLoai(int maLoai) {
        MaLoai = maLoai;
    }

    public String getTenMonAn() {
        return TenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        TenMonAn = tenMonAn;
    }

    public String getGiaTien() {
        return GiaTien;
    }

    public void setGiaTien(String giaTien) {
        GiaTien = giaTien;
    }


}
