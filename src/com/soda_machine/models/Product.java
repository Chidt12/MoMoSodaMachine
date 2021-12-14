package com.soda_machine.models;

public class Product {
    private int id;
    private String name;
    private int price;
    private int currentAmount;
    private int originalAmount;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Product (int id, String name, int price, int originalAmount){
        this.id = id;
        this.name = name;
        this.price = price;
        this.originalAmount = originalAmount;
        this.currentAmount = originalAmount;
    }
}
