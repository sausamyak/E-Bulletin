package com.example.samyaksau.e_bulletin.httputil;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public interface OnHttpTaskListener {

    /**
     *
     * @param ac
     * @param param
     */
    public void sendHttpRequest(Action ac, String... param);

    /**
     *
     * @param ac
     * @param response
     */
    public void OnReceiveResponse(Action ac, Response response);
}
