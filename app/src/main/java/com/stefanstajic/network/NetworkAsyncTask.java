package com.stefanstajic.network;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public abstract class NetworkAsyncTask<I, P, R> extends AsyncTask<I, P, R> {
    HttpURLConnection urlConnection;

    protected String getRequest(String stringUrl) {
        try {
            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);

            handleHttpStatus(urlConnection);

            return readResponseStream(urlConnection);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String postRequest(String stringUrl, JSONObject postData) {
        try {

            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    out, StandardCharsets.UTF_8));
            writer.write(postData.toString());
            writer.flush();

            handleHttpStatus(urlConnection);

            return readResponseStream(urlConnection);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String putRequest(String stringUrl, JSONObject postData) {
        try {

            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("PUT");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    out, StandardCharsets.UTF_8));
            writer.write(postData.toString());
            writer.flush();

            handleHttpStatus(urlConnection);

            return readResponseStream(urlConnection);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String deleteRequest(String stringUrl) {
        try {

            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);

            handleHttpStatus(urlConnection);

            return readResponseStream(urlConnection);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void handleHttpStatus(HttpURLConnection urlConnection) throws IOException {
        int code = urlConnection.getResponseCode();
        if (code != 200) {
            throw new IOException("Invalid response from server: " + code);
        }
    }

    private String readResponseStream(HttpURLConnection urlConnection) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream()));
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }

        return stringBuffer.toString();
    }
}
