package com.Nhom10.BTL.TimTro;


public class CustomItems {


    private String spinnerText;
    private int spinnerImage;

    public CustomItems(){}
    public CustomItems(String text)
    {
        this.spinnerText= text;
    }
    public CustomItems(String spinnerText, int spinnerImage) {
        this.spinnerText = spinnerText;
        this.spinnerImage = spinnerImage;
    }

    public String getSpinnerText() {
        return spinnerText;
    }

    public int getSpinnerImage() {
        return spinnerImage;
    }
}