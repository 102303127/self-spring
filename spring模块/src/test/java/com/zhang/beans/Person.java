package com.zhang.beans;

import com.zhang.beans.factory.annotation.Autowired;
import com.zhang.beans.factory.annotation.Value;
import com.zhang.stereotype.Component;

/**
 * 用来测试包扫描
 *
 * @author zhang
 * @date 2024/8/14
 * @Description
 */
@Component
public class Person {


    @Value("${person.name}")
    private String name;

    @Value("${person.password}")
    private String password;


    public Person() {
    }

    public Person(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ComponentBean{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
