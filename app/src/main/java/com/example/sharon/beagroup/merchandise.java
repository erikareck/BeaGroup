package com.example.sharon.beagroup;

public class merchandise {
    public int photoID;
    public String name;
    public String discount_price;
    public String description;
    public String url;

    public merchandise(int photoID, String name, String discount_price, String description, String url){
        this.photoID = photoID;
        this.name = name;
        this.discount_price = discount_price;
        this.description = description;
        this.url = url;
    }
}
