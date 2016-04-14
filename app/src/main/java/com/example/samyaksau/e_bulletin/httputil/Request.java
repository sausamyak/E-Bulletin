package com.example.samyaksau.e_bulletin.httputil;

import com.example.samyaksau.e_bulletin.helper.MLog;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class Request {

    private ArrayList<NameValuePair> params;
    private ArrayList<NameValuePair> headers;

    Action action;
    OnHttpTaskListener listener;

    /**
     *
     * @param action
     * @param listener
     */
    public Request (Action action, OnHttpTaskListener listener) {
        this.action = action;
        this.listener = listener;

        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }

    /**
     *
     * @param name
     * @param value
     */
    public void addParam (String name, String value) {
        params.add(new BasicNameValuePair(name,value));
    }

    /**
     *
     * @param name
     * @param value
     */
    public void addHeader(String name, String value) {
        headers.add(new BasicNameValuePair(name, value));
    }

    /**
     *
     * @param method
     * @param url
     */
    public void execute(RequestMethod method, String url) {
        switch (method) {
            case GET: {
                // sdd parameters
                String combinedParams = "";
                if (!params.isEmpty()) {

                    combinedParams += "?";
                    for (NameValuePair p : params) {
                        String paramString = null;
                        try {
                            paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Aurto-generated catch block
                            e.printStackTrace();
                        }

                        if (combinedParams.length() > 1) {
                            combinedParams += "&" + paramString;
                        } else {
                            combinedParams += paramString;
                        }
                    }
                }
                HttpGet request = new HttpGet(url + combinedParams);
                MLog.v("HTTP REQUEST : GET ", url + combinedParams);

                // add headers
                for (NameValuePair h : headers) {
                    request.addHeader(h.getName(), h.getValue());
                }
                new HttpConnection(action, listener, request);
                break;
            }

            case DELETE: {

                // add parameters
                String combinedParams = "";
                if (!params.isEmpty()) {

                    combinedParams += "?";
                    for (NameValuePair p : params) {

                        String paramString = null;
                        try {

                            paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (combinedParams.length() > 1) {
                            combinedParams += "&" + paramString;
                        } else {
                            combinedParams += paramString;
                        }
                    }
                }
                HttpDelete request = new HttpDelete(url + combinedParams);
                MLog.v("HTTP REQUEST : DELETE ", url + combinedParams);
                // add headers
                for (NameValuePair h : headers) {
                    request.addHeader(h.getName(), h.getValue());
                }
                new HttpConnection(action, listener, request);
                break;
            }

            case POST: {

                HttpPost request = new HttpPost(url);
                MLog.v("HTTP REQUEST : POST ", url);
                // add headers
                for (NameValuePair h : headers) {

                    request.addHeader(h.getName(), h.getValue());
                }

                if (!params.isEmpty()) {
                    try {

                        request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                new HttpConnection(action, listener, request);
                break;
            }

            case PUT: {

                HttpPut request = new HttpPut(url);
                MLog.v("HTTP REQUEST : PUT ", url);
                //add headers
                for (NameValuePair h : headers) {
                    request.addHeader(h.getName(),h.getValue());
                }

                if (!params.isEmpty()) {
                    try {

                        request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    } catch (UnsupportedEncodingException e ) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                new HttpConnection(action, listener, request);
                break;
            }
        }
    }
}
