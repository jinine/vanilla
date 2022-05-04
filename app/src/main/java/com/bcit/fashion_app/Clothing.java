package com.bcit.fashion_app;

import java.io.Serializable;

enum ItemType{
    SHIRT,
    PANTS,
    SHOES
}

public class Clothing implements Serializable {
    private String userId;
    private String name;
    private String size;
    private String colour;
    private String brand;
    private ItemType item;
    private String clothingImageId;

    public Clothing(String userId, String name, String size, String colour, String brand, ItemType item, String clothingImageId) {
        this.userId = userId;
        this.name = name;
        this.size = size;
        this.colour = colour;
        this.brand = brand;
        this.item = item;
        this.clothingImageId = clothingImageId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getColour() {
        return colour;
    }

    public String getBrand() {
        return brand;
    }

    public ItemType getItem() {
        return item;
    }

    public String getClothingImageId() {
        return clothingImageId;
    }
}
