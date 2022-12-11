package com.Nhom10.BTL.DTO;

public class LoaiHinhDTO {
    public String maloaihinh;
    public LoaiHinhDTO(){}


    public LoaiHinhDTO(String maloaihinh, String tenloai, String hinhLoai) {
        this.maloaihinh = maloaihinh;
        this.tenloai = tenloai;
        this.hinhLoai = hinhLoai;
    }

    public String getMaloaihinh() {
        return maloaihinh;
    }

    public void setMaloaihinh(String maloaihinh) {
        this.maloaihinh = maloaihinh;
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }

    public String getHinhLoai() {
        return hinhLoai;
    }

    public void setHinhLoai(String hinhLoai) {
        this.hinhLoai = hinhLoai;
    }

    public String tenloai;
    public String hinhLoai;
}

