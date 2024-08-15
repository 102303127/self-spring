package com.zhang.beans;

import com.zhang.beans.factory.annotation.Autowired;
import com.zhang.stereotype.Component;

/**
 * @author zhang
 * @date 2024/8/14
 * @Description
 */

@Component
public class Student {


    @Autowired
    private Clothes clothes;

    public Student() {
    }

    public Student(Clothes clothes) {
        this.clothes = clothes;
    }

    public Clothes getClothes() {
        return clothes;
    }

    public void setClothes(Clothes clothes) {
        this.clothes = clothes;
    }

    @Override
    public String toString() {
        return "Student{" +
                "clothes=" + clothes +
                '}';
    }
}
