package com.example.samyaksau.e_bulletin.details;

import java.io.Serializable;

/**
 * @Author  Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class NoticeDetail implements Serializable {

    public String noticeId;

    public String title;

    public String description;

    public String time;

    public String date;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
