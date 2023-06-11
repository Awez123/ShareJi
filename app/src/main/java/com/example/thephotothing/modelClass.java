package com.example.thephotothing;

import android.widget.ImageView;

public class modelClass {

    private String img;

    modelClass(String img){
        this.img=img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
