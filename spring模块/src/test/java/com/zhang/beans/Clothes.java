package com.zhang.beans;

import com.zhang.stereotype.Component;

/**
 * @author zhang
 * @date 2024/8/6
 * @Description
 */
public class Clothes {

    private String color;


    public Clothes() {
    }

    public Clothes(String color){
        this.color = color;
    }



    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    @Override
    public String toString() {
        return "Clothes{" +
                "color='" + color + '\'' +
                '}';
    }
}
