package com.example.luan.adoptyourstreet.models;

/**
 * Created by Hai on 07/07/2016.
 */
public class ObjDemo  {
    private int id;
    private String name;

    public ObjDemo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ObjDemo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
