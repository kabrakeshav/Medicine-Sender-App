package com.keshavoneml.oneml_customer;

import java.io.Serializable;


// helping class for VendorPage.Java

class GatterGetVendersModel implements Serializable {
    private boolean isSelected;
    private String id,name,pharm_name,store_address,average_rating,store_status,offer;

    public GatterGetVendersModel(boolean isSelected, String id, String name, String pharm_name, String store_address, String average_rating,String store_status,String offer) {
        this.isSelected = isSelected;
        this.id = id;
        this.name = name;
        this.pharm_name = pharm_name;
        this.store_address = store_address;
        this.average_rating = average_rating;
        this.store_status=store_status;
        this.offer=offer;
    }

    public GatterGetVendersModel(boolean b, String id, String name, String pharm_name, String store_address, String average_rating, String store_status) {
        this.isSelected = b;
        this.id = "";
        this.name = name;
        this.pharm_name = pharm_name;
        this.store_address = store_address;
        this.average_rating = "";
        this.store_status="";
        this.offer = "";
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPharm_name() {
        return pharm_name;
    }

    public void setPharm_name(String pharm_name) {
        this.pharm_name = pharm_name;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public String getStore_status() {
        return store_status;
    }

    public void setStore_status(String store_status) {
        this.store_status = store_status;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }
}

