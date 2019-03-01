package cn.xtits.ireport.entity;

import java.io.Serializable;

public class ResLog implements Serializable {
    private String resDate;
    private String mes;

    public String getResDate() {
        return resDate;
    }

    public void setResDate(String resDate) {
        this.resDate = resDate;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
}