package cn.xtits.ireport.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class ResCountCode implements Serializable {
    private String url;
    private Long count;
    private Long duration;
    private Float avgDuration;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Float getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Float avgDuration) {
        this.avgDuration = avgDuration;
    }
}