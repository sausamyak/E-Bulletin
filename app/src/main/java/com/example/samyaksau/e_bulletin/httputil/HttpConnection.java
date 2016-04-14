package com.example.samyaksau.e_bulletin.httputil;

import android.os.Handler;

import com.example.samyaksau.e_bulletin.helper.AppConstants;
import com.example.samyaksau.e_bulletin.helper.MLog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class HttpConnection extends Thread {

    Action action;
    OnHttpTaskListener listener;
    android.os.Handler handler = new Handler();
    HttpUriRequest request;
    Response response;
    HttpClient client = new DefaultHttpClient();

    // Create a local instance of cookies store
    public static CookieStore cookieStore = new BasicCookieStore();

    // Create local HTTP context
    static HttpContext localContext = new BasicHttpContext();

    /**
     *
     * @param  action
     * @param listener
     * @param request
     */
    HttpConnection(Action action, OnHttpTaskListener listener, HttpUriRequest request) {

        this.listener = listener;
        this.action = action;
        this.request = request;

        // Bind custom cookies store to the local context
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        response = new Response();
    }

    @Override
    public void run(){
        super.run();
        executeRequest(request);
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.OnReceiveResponse(action, response);
            }
        });
    }

    /**
     *
     * @param request
     */
    private synchronized void executeRequest(HttpUriRequest request) {

        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 12000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        //HttpClient client = new  DefaultHttpClient(httpParameters);
        HttpResponse httpResponse;

        // Bind custom cookies store to the local context

        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        try {
            // Pass local context as a parameter
            httpResponse = client.execute(request, localContext);

            /*
             * List<Cookie> cookies = cookiesStore.getCookies();
             * for(int i = 0; i < cookies.size(); i++)
             * {
             * MLog.d("LOCAL COOKIES: ", "" + cookies.get(i));
             * }
             */

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                String res = convertStreamToString(instream);
                MLog.v("HTTP RESPONSE : " + action.toString(), res);
                response.setResult(res);
                response.setSuccess(true);

                try {
                    JSONObject object = new JSONObject(res);
                    if (!object.getBoolean("success")) {
                        response.setErrorMessage(object.getString("message"));
                        response.setSuccess(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Closing the input stream will trigger connection release
                instream.close();
            }
        } catch (Exception e) {

            client.getConnectionManager().shutdown();
            e.printStackTrace();

            response.setSuccess(false);

            response.setErrorMessage(AppConstants.NETWORK_ERROR);
            MLog.e("IN CATCH: ", "" + e.getMessage());
        }
    }
    /**
     *
     * @param  is
     * @return
     */
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

    /**
     *
     * @param  text
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public InputStream stringtoInputStream(String text) {
        try {
            return new ByteArrayInputStream(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
