package com.kh_sof_dev.admin.Clasess;

public class Request {

    private String id,date,product;
private Double weight,price,count,talif;
private int type;

    private  String nb;

    public String getNb() {
        return nb;
    }

    public void setNb(String nb) {
        this.nb = nb;
    }
    public Request(String date, String product, Double weight, Double price, Double count, Double talif) {

        this.date = date;
        this.product = product;
        this.weight = weight;
        this.price = price;
        this.count = count;
        this.talif = talif;

    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Double getTalif() {
        return talif;
    }

    public void setTalif(Double talif) {
        this.talif = talif;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Request() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }


}
