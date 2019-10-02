package com.kh_sof_dev.admin.Clasess;

public class production {
    private String date;
    private Double talif,production;

    public production() {
    }

    public production(String date, Double talif, Double production) {
        this.date = date;
        this.talif = talif;
        this.production = production;
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
