package com.Nhom10.BTL;

public class mon {

    String tenmon;
    String soluong;
    String ghichu;
    public mon(){}

    public String getTenmon() {
        return tenmon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public mon(String tenmon, String soluong, String ghichu) {
        this.tenmon = tenmon;
        this.soluong = soluong;
        this.ghichu = ghichu;
    }
}
