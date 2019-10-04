package com.kh_sof_dev.admin.Clasess;

public class production {
    private String date;
    private Double talif=0.0,production=0.0,order=0.0;

    public production() {
    }

    public production( Double talif, Double production) {

        this.talif = talif;
        this.production = production;
    }

    public Double getOrder() {
        return order;
    }

    public void setOrder(Double order) {
        this.order = order;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTalif() {
        return talif;
    }

    public void setTalif(Double talif) {
        this.talif = talif;
    }

    public Double getProduction() {
        return production;
    }

    public void setProduction(Double production) {
        this.production = production;
    }
}
