package com.Nhom10.BTL.DTO;

public class XaDTO {
    public XaDTO(String maxa, String tenxa) {
        this.maxa = maxa;
        this.tenxa = tenxa;

    }

    public XaDTO(){}
    public String getMaxa() {
        return maxa;
    }

    public void setMaxa(String maxa) {
        this.maxa = maxa;
    }

    public String getTenxa() {
        return tenxa;
    }

    public void setTenxa(String tenxa) {
        this.tenxa = tenxa;
    }

    public  String maxa;
    public String tenxa;
}
