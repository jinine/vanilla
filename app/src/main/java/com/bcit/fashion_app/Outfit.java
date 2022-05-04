package com.bcit.fashion_app;

import java.io.Serializable;

public class Outfit implements Serializable {
    private String userId;
    private Clothing shirt;
    private Clothing pants;
    private Clothing shoe;

    public Outfit(String userId, Clothing shirt, Clothing pants, Clothing shoe) {
        this.userId = userId;
        this.shirt = shirt;
        this.pants = pants;
        this.shoe = shoe;
    }

    public String getUserId() {
        return userId;
    }

    public Clothing getShirt() {
        return shirt;
    }

    public Clothing getPants() {
        return pants;
    }

    public Clothing getShoe() {
        return shoe;
    }
}
