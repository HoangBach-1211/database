package com.Nhom10.BTL.DTO;

public class HuyenDTO {
    public HuyenDTO(){}



    public void setTenhuyen(String tenhuyen) {
        this.tenhuyen = tenhuyen;
    }

    public  String mahuyen;

    public  String tenhuyen;

    public HuyenDTO(String mahuyen, String tenhuyen) {
        this.mahuyen = mahuyen;
        this.tenhuyen = tenhuyen;
    }

    public String getMahuyen() {
        return mahuyen;
    }

    public void setMahuyen(String mahuyen) {
        this.mahuyen = mahuyen;
    }

    public String getTenhuyen() {
        return tenhuyen;
    }
}
