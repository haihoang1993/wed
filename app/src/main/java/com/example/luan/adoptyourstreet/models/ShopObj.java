package com.example.luan.adoptyourstreet.models;

/**
 * Created by Hai on 03/08/2016.
 */
public class ShopObj {
    private String Name;
    private float Money;

    public ShopObj(String name, float money) {
        Name = name;
        Money = money;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getMoney() {
        return Money;
    }

    public void setMoney(float money) {
        Money = money;
    }
}
