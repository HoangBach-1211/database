package com.thanhnguyen.smartorder;

public class lsgoimon {
    public String tenbanls;
    public  lsgoimon(){}
    public lsgoimon(String tenbanls, String ngaygoils, String tongtienls) {

        this.tenbanls = tenbanls;
        this.ngaygoils = ngaygoils;
        this.tongtienls = tongtienls;
    }

    public String ngaygoils;

    public String getTenbanls() {
        return tenbanls;
    }

    public void setTenbanls(String tenbanls) {
        this.tenbanls = tenbanls;
    }

    public String getNgaygoils() {
        return ngaygoils;
    }

    public void setNgaygoils(String ngaygoils) {
        this.ngaygoils = ngaygoils;
    }

    public String getTongtienls() {
        return tongtienls;
    }

    public void setTongtienls(String tongtienls) {
        this.tongtienls = tongtienls;
    }

    public String tongtienls;
   }
