package com.example.model;

public class StoreBean {
    private String Store_ID = "";  //商家编号，共4位;
    private String Store_name = ""; //商家名称
    private String Store_image = ""; //商家展示图片
    private String Store_discount = ""; //商家优惠信息
    private String Store_addr = ""; //商家地址;
    private String Store_tel = ""; //商家联系方式

    public StoreBean() {

    }

    public StoreBean(String store_ID, String store_name, String store_image, String store_discount,
              String store_addr, String store_tel){
        this.Store_ID = store_ID;
        this.Store_name = store_name;
        this.Store_image = store_image;
        this.Store_discount = store_discount;
        this.Store_addr = store_addr;
        this.Store_tel = store_tel;
    }

    public String getStore_ID() {
        return Store_ID;
    }

    public String getStore_name() {
        return Store_name;
    }

    public String getStore_image() {
        return Store_image;
    }

    public String getStore_discount() {
        return Store_discount;
    }

    public String getStore_addr() {
        return Store_addr;
    }

    public String getStore_tel() {
        return Store_tel;
    }

    public void setStore_ID(String store_ID) {
        this.Store_ID = store_ID;
    }

    public void setStore_name(String store_name) {
        this.Store_name = store_name;
    }

    public void setStore_image(String store_image) {
        this.Store_image = store_image;
    }

    public void setStore_discount(String store_discount) {
        this.Store_discount = store_discount;
    }

    public void setStore_addr(String store_addr) {
        this.Store_addr = store_addr;
    }

    public void setStore_tel(String store_tel) {
        this.Store_tel = store_tel;
    }
}