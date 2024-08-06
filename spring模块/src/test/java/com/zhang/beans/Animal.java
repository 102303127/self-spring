package com.zhang.beans;

/**
 * @author zhang
 * @date 2024/7/5
 * @Description
 */
public class Animal {
    private String name;
    private int age;

    private Clothes clothes;


    public void setClothes(Clothes clothes) {
        this.clothes = clothes;
    }

    public Clothes getClothes() {
        return clothes;
    }

    public Animal() {
    }

    public Animal(String name, int age, Clothes clothes) {
        this.name = name;
        this.age = age;
        this.clothes = clothes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", clothes=" + clothes +
                '}';
    }
}
