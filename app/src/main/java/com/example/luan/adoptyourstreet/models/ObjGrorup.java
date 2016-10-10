package com.example.luan.adoptyourstreet.models;

/**
 * Created by Hai on 02/08/2016.
 */
public class ObjGrorup {
    private String Name;
    private String Count;

    public ObjGrorup(String name, String count) {
        Name = name;
        Count = count;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }
}
