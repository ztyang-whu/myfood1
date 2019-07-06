package com.example.model;

public class FoodBean {
    private String Food_ID = ""; //菜品编号。由商家编号加4位序号组成
    private String Food_name = ""; //菜品名称
    private String Food_image = ""; //菜品图片
    private String Food_cost = ""; //菜品单价
    private String Food_profile = ""; //菜品简介
    private String Food_count = ""; //菜品销量
    private String Food_Storename = ""; //菜品的商家
    private String Food_Storeaddr = ""; //菜品的商家地址
    private String Food_Storetel = ""; //菜品商家电话

    public FoodBean() {

    }

    public FoodBean(String food_ID, String food_name, String food_image, String food_Storetel,
             String food_cost, String food_profile, String food_count, String food_Storename, String food_Storeaddr){
        this.Food_ID = food_ID;
        this.Food_name = food_name;
        this.Food_image = food_image;
        this.Food_cost = food_cost;
        this.Food_profile = food_profile;
        this.Food_count = food_count;
        this.Food_Storename = food_Storename;
        this.Food_Storeaddr = food_Storeaddr;
        this.Food_Storetel = food_Storetel;
    }

    public String getFood_ID() {
        return Food_ID;
    }

    public String getFood_name() {
        return Food_name;
    }

    public String getFood_image() {
        return Food_image;
    }

    public String getFood_cost() {
        return Food_cost;
    }

    public String getFood_profile() {
        return Food_profile;
    }

    public String getFood_count() {
        return Food_count;
    }

    public String getFood_Storename() {
        return Food_Storename;
    }

    public String getFood_Storeaddr() {
        return Food_Storeaddr;
    }

    public String getFood_Storetel() {
        return Food_Storetel;
    }

    public void setFood_ID(String food_ID) {
        this.Food_ID = food_ID;
    }

    public void setFood_name(String food_name) {
        this.Food_name = food_name;
    }

    public void setFood_image(String food_image) {
        this.Food_image = food_image;
    }

    public void setFood_cost(String food_cost) {
        this.Food_cost = food_cost;
    }

    public void setFood_profile(String food_profile) {
        this.Food_profile = food_profile;
    }

    public void setFood_count(String food_count) {
        this.Food_count = food_count;
    }

    public void setFood_Storename(String food_Storename) {
        this.Food_Storename = food_Storename;
    }

    public void setFood_Storeaddr(String food_Storeaddr) {
        this.Food_Storeaddr = food_Storeaddr;
    }

    public void setFood_Storetel(String food_Storetel) {
        this.Food_Storetel = food_Storetel;
    }
}