package com.example.mountainviewcafe;

public class addProduct {

    public String title, description, image, price, discount, id, quantity;
    public static boolean isSelected = false;

    public addProduct() {

    }

    public static boolean isIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(boolean isSelected) {
        addProduct.isSelected = isSelected;
    }
//
//    public String getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(String quantity) {
//        this.quantity = quantity;
//    }

//    public static boolean isIsSelected() {
//        return isSelected;
//    }
//
//    public static void setIsSelected(boolean isSelected) {
//        addProduct.isSelected = isSelected;
//    }

    public addProduct(String title, String description, String image, String price, String discount) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
        this.discount = discount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static boolean isSelected() {
        return isSelected;
    }

    public static void setSelected(boolean selected) {
        isSelected = selected;
    }
}
