package com.example.samyaksau.e_bulletin.result;

import java.util.ArrayList;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class GetAllNoticesResult {

    public String success;

    public String message;

    public ArrayList<NoticeList> notices;

    public  class NoticeList {
        public  String noticeId;

        public String title;

        public String description;

        public String date;

        public String time;
    }
}
