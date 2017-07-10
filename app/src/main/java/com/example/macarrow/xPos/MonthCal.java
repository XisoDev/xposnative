package com.example.macarrow.xPos;

import java.util.List;
import java.util.Map;

public class MonthCal {

    private String day;
    private List<Map<String, Object>> list;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<Map<String, Object>> list() {
        return list;
    }

    public void list(List<Map<String, Object>> list) {
        this.list = list;
    }
}