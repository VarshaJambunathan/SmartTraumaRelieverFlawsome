package com.example.jaisa.smarttraumareliever_flawsome.Beans;

/**
 * Created by Varsha on 11-03-2018.
 */

public class Law {
    String name;
    String desc;

    public Law(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
