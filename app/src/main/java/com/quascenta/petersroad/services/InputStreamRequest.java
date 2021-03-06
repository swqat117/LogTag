package com.quascenta.petersroad.services;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.InputStream;

/**
 * Created by AKSHAY on 11/11/2016.
 */

public class InputStreamRequest extends Request<byte[]> {
    private final Response.Listener<byte[]> mListener;

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public InputStreamRequest(int method, String url, Response.Listener<byte[]> listener,
                              Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        // this request would never use cache.
        setShouldCache(false);
        mListener = listener;
    }

    /**
     * Creates a new GET request.
     *
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public InputStreamRequest(String url, Response.Listener<byte[]> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        if (response instanceof InputStreamNetworkResponse) {
            // take the InputStream here.
            InputStream ins = ((InputStreamNetworkResponse) response).ins;
            //return Response.success(ins, null);
        }
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }
}
