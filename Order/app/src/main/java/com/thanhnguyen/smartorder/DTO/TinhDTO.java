package com.thanhnguyen.smartorder.DTO;

public class TinhDTO {
    public TinhDTO(String matinh, String tentinh) {
        this.matinh = matinh;
        this.tentinh = tentinh;

    }

    public String matinh;
    public TinhDTO(){}

    public String getMatinh() {
        return matinh;
    }

    public void setMatinh(String matinh) {
        this.matinh = matinh;
    }

    public String getTentinh() {
        return tentinh;
    }

    public void setTentinh(String tentinh) {
        this.tentinh = tentinh;
    }

    public String tentinh;
}
