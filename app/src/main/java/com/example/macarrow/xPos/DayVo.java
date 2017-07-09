package com.example.macarrow.xPos;

import java.util.List;
import java.util.Map;

public class DayVo {

    private String day;
    private List<Map<String, Object>> scheduleList;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<Map<String, Object>> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Map<String, Object>> scheduleList) {
        this.scheduleList = scheduleList;
    }
}
