package com.example.samyaksau.e_bulletin.httputil;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public enum  Action {

    LOGIN("login.php"),

    REGISTRATION("registration.php"),

    POST_NOTICE("post_notice.php"),

    GET_ALL_NOTICES("getAllNotice.php"),

    GCM_BROADCASTING("gcm_broadcasting.php"),

    SEND_PASSWORD("sendPassword.php");

    private String url;

    private Action(String s) {
        url = s;
    }

    public  String toString() {
        return HttpConst.SERVICE_URL.concat(url);
    }
}
