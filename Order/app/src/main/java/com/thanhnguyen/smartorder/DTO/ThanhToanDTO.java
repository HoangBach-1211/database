package com.thanhnguyen.smartorder.DTO;
public class ThanhToanDTO {
    String TenMonAn, HinhAnh;
    int SoLuong;


    public ThanhToanDTO(String tenMonAn, String hinhAnh, int soLuong, int giaTien, int maGoiMon, int maMonAn) {
        TenMonAn = tenMonAn;
        HinhAnh = hinhAnh;
        SoLuong = soLuong;
        GiaTien = giaTien;
        MaGoiMon = maGoiMon;
        MaMonAn = maMonAn;
    }
    public ThanhToanDTO(){}

    int GiaTien;

    public String getTenMonAn() {
        return TenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        TenMonAn = tenMonAn;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }

    public int getGiaTien() {
        return GiaTien;
    }

    public void setGiaTien(int giaTien) {
        GiaTien = giaTien;
    }

    public int getMaGoiMon() {
        return MaGoiMon;
    }

    public void setMaGoiMon(int maGoiMon) {
        MaGoiMon = maGoiMon;
    }

    public int getMaMonAn() {
        return MaMonAn;
    }

    public void setMaMonAn(int maMonAn) {
        MaMonAn = maMonAn;
    }

    int MaGoiMon;
    int MaMonAn;




}
