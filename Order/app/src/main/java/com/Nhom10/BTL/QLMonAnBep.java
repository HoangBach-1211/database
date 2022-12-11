package com.Nhom10.BTL;

public class QLMonAnBep {

    String tenmon;
    String soluong;

    public QLMonAnBep(){}
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

    public QLMonAnBep(String tenmon, String soluong) {
        this.tenmon = tenmon;
        this.soluong = soluong;
    }
}
