package com.jacob.springcloud.dto;

import java.text.SimpleDateFormat;
import java.util.Date;



public class UserResponse {
    private String status;
    private String error;
    private Date date = new Date();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDate() {
    	//將日期轉換為"yyyy-MM-dd HH:mm:ss"格式的時間字符串nTime
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
